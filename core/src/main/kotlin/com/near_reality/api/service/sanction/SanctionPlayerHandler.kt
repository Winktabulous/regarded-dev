package com.near_reality.api.service.sanction

import com.near_reality.api.model.*
import com.near_reality.api.responses.SanctionRevokeResponse
import com.near_reality.api.responses.SanctionSubmitResponse
import com.near_reality.api.service.store.notify
import com.near_reality.api.util.usernameSanitizer
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.cores.CoresManager
import com.zenyte.game.GameConstants
import com.zenyte.game.content.achievementdiary.Diary
import com.zenyte.game.content.grandexchange.GrandExchangeHandler
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.OptionsBuilder
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.dialogue.OptionsMenuD
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.slf4j.LoggerFactory
import kotlin.jvm.optionals.getOrNull
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Handles the submission and revocation of [sanctions][com.near_reality.api.model.Sanction].
 *
 * @author Stan van der Bend
 */
object SanctionPlayerHandler {

    private val logger = LoggerFactory.getLogger(SanctionPlayerHandler::class.java)

    @JvmStatic
    val INFINITE_DURATION : Duration? = null

    /**
     * Prompts the [reporter] to enter the name of the player
     * to apply the sanction to and a reason for the sanction.
     *
     * If no player by the username is online, but the player exists,
     * the player file will be loaded and the sanction will be applied.
     *
     * @param reporter the player applying the sanction
     * @param type the type of sanction to apply
     * @param level the level of the sanction to apply
     *
     * @see submitSanction for the actual submission of the sanction
     */
    @JvmStatic
    fun startSanctionSubmission(
        reporter: Player,
        type: SanctionType,
        level: SanctionLevel
    ) {
        if (sanctionsDisabled(reporter)) return
        reporter.promptPlayerName {  offender ->
            reporter.sendInputName("Enter reason for the sanction") { reason ->
                reporter.dialogue {
                    options("Select the duration of the sanction") {
                        dialogueOption("Permanent", action = {
                            submitSanction(level, type, offender, reporter, reason)
                        })
                        durationPromptOption(level, type, offender, reporter, reason, DurationUnit.MINUTES)
                        durationPromptOption(level, type, offender, reporter, reason, DurationUnit.HOURS)
                        durationPromptOption(level, type, offender, reporter, reason, DurationUnit.DAYS)
                    }
                }
            }
        }
    }

    private fun OptionsBuilder.durationPromptOption(
        level: SanctionLevel,
        type: SanctionType,
        offender: Player,
        reporter: Player,
        reason: String?,
        durationUnit: DurationUnit
    ) {
        dialogueOption("Enter $durationUnit", noPlayerMessage = true, action = {
            it.sendInputInt("Enter number of $durationUnit until expiration") { duration ->
                submitSanction(level, type, offender, reporter, reason, duration.toDuration(durationUnit))
            }
        })
    }

    /**
     * Prompts the [revoker] to enter the name of the player to revoke a sanction from.
     */
    @JvmStatic
    fun startSanctionRevoke(revoker: Player) {
        if (sanctionsDisabled(revoker)) return
        revoker.promptPlayerName { offender ->
            val sanctions = offender.activeSanctions
            val sanctionsAsText = sanctions.map { it.format() }.toTypedArray()
            revoker.dialogueManager.start(object : OptionsMenuD(revoker, "Select the punishment to revoke", *sanctionsAsText) {
                override fun handleClick(slotId: Int) {
                    val sanction = sanctions[slotId]
                    if (sanction.eligible(revoker.privilege))
                        revokeSanction(revoker, sanction)
                    else
                        revoker.notify("You do not have the required rank to revoke this sanction.")
                }
            })
        }
    }

    private fun sanctionsDisabled(reporter: Player?): Boolean {
        if (GameConstants.WORLD_PROFILE.isBeta() || GameConstants.WORLD_PROFILE.isDevelopment()) {
            reporter?.notify("Sanctions are disabled on this world.")
            return true
        }
        return false
    }

    private fun Player.promptPlayerName(onFound: (Player) -> Unit) {
        sendInputName("Enter name of the subject player") { offenderNameInput ->
            val offenderName = usernameSanitizer(offenderNameInput)
            val onlineOffender = World.getPlayer(offenderName).getOrNull()
            if (onlineOffender != null)
                onFound(onlineOffender)
            else {
                CoresManager.getLoginManager().load(true, offenderName) { optionalOffender ->
                    if (optionalOffender.isEmpty)
                        notify("No account found for username ${Colour.RS_GREEN.wrap(offenderName)}.")
                    else
                        onFound(optionalOffender.get())
                }
            }
        }
    }

    @JvmStatic
    fun submitSanction(
        level: SanctionLevel,
        type: SanctionType,
        offender: Player,
        reporter: Player?,
        reason: String?,
    ) {
        submitSanction(level, type, offender, reporter, reason, INFINITE_DURATION)
    }

    @JvmStatic
    fun submitSanction(
        level: SanctionLevel,
        type: SanctionType,
        offender: Player,
        reporter: Player?,
        reason: String? = null,
        duration: Duration? = INFINITE_DURATION,
    ) {
        if (sanctionsDisabled(reporter)) return
        fun checkAffectedPlayers(predicate: (Player) -> Boolean, onSuccess: () -> Unit) {
            val affectedPlayers = World.getPlayers().filter(predicate)
            if (affectedPlayers.size > 1) {
                if (reporter == null) {
                    logger.error("Multiple players would be affected by the sanction, but no reporter to handle the sanction, so ignoring $level $type for $offender.")
                    return
                }
                reporter.dialogue {
                    plain("${affectedPlayers.size} player would be affected by this sanction.<br>Do you want to continue?")
                    options {
                        dialogueOption("Yes, apply sanction to ${affectedPlayers.size} players") {
                            onSuccess()
                        }
                        dialogueOption("No")
                        dialogueOption("View affected players") {
                            Diary.sendJournal(reporter, "Affected players", affectedPlayers.map { it.name })
                        }
                    }
                }
            } else
                onSuccess()
        }

        val time = Clock.System.now()
        val offenderIp = offender.ip
        when (level) {
            SanctionLevel.ACCOUNT -> {
                val sanction = AccountSanction(offender.name, null, time, type, reporter?.name, reason, duration)
                submitSanctionToAPI(reporter, sanction, offender, type, level, offenderIp, reason, duration, time)
            }
            SanctionLevel.IP -> {
                checkAffectedPlayers({ it.ip == offenderIp }) {
                    val sanction = IPSanction(offenderIp, null, time, type, reporter?.name, reason, duration)
                    submitSanctionToAPI(reporter, sanction, offender, type, level, offenderIp, reason, duration, time)
                }
            }
            SanctionLevel.UUID -> {
                val uuid = offender.playerInformation.uuid
                checkAffectedPlayers({ it.playerInformation.uuid.contentEquals(uuid) }) {
                    val sanction = UUIDSanction(uuid, null, time, type, reporter?.name, reason, duration)
                    submitSanctionToAPI(reporter, sanction, offender, type, level, offenderIp, reason, duration, time)
                }
            }
        }
    }

    private fun submitSanctionToAPI(
        reporter: Player?,
        sanction: Sanction,
        offender: Player,
        type: SanctionType,
        level: SanctionLevel,
        offenderIp: String?,
        reason: String?,
        duration: Duration?,
        time: Instant,
    ) {
        reporter?.notify("Submitting sanction...", loading = true)
        SanctionAPIService.submit(sanction) { response ->
            WorldTasksManager.schedule {
                reporter?.notify(
                    when (response) {
                        is SanctionSubmitResponse.Success -> "You have successfully submitted the sanction."
                        is SanctionSubmitResponse.OffenderAccountNotFound -> "Failed to submit sanction<br>The offender's account was not found."
                        is SanctionSubmitResponse.Error -> "Failed to submit sanction<br>${response.message}"
                    }
                )
                offender.user = offender.user?.copy(sanctions = offender.activeSanctions + sanction)
                if (type == SanctionType.BAN)
                    offender.logout(true)
                GameLogger.log {
                    GameLogMessage.Sanction(
                        username = reporter?.name ?: "AUTO",
                        otherUsername = offender.name,
                        type = type,
                        level = level,
                        offenderIp = offenderIp,
                        reason = reason ?: "AUTO",
                        expiresAt = duration?.let { time.plus(it) },
                    )
                }
            }
        }
    }

    @JvmStatic
    fun revokeSanction(revoker: Player?, sanction: Sanction) {
        val sanctionId = sanction.id?: error("The id of the sanction $sanction is null, this should never happen, contact a developer immediately.")
        revoker?.notify("Revoking sanction...", loading = true)
        SanctionAPIService.revoke(sanctionId.toLong(), sanction.level) { response ->
            WorldTasksManager.schedule {
                revoker?.notify(
                    when (response) {
                        is SanctionRevokeResponse.Success -> "You have successfully revoked the sanction."
                        is SanctionRevokeResponse.SanctionNotFound -> "Failed to revoke sanction<br>The sanction was not found."
                        is SanctionRevokeResponse.Error -> "Failed to revoke sanction<br>${response.message}"
                    }
                )
            }
        }
    }

    internal fun onSanctionReceived(sanction: Sanction) {
        val reporter = sanction.reporter?.let(World::getPlayer)?.getOrNull()
        fun applyAffectedPlayers(predicate: (Player) -> Boolean) {
            val applicablePlayers = World.getPlayers().filter(predicate)
            applicablePlayers.forEach { player ->
                player.user = player.user?.copy(sanctions = player.activeSanctions + sanction)
                player.notify("You have been ${sanction.format()} ${if (sanction.duration != null) "for ${sanction.duration!!.inWholeMinutes} minutes" else ""}." )
                if (sanction.type == SanctionType.BAN) {
                    player.logout(true)
                    GrandExchangeHandler.quarantineOffers(player)
                }

            }
            if (applicablePlayers.isEmpty())
                reporter?.notify("You have successfully submitted the sanction, but none of the affected players were online.")
            else
                reporter?.notify("You have successfully submitted the sanction, ${applicablePlayers.size} online players were affected.")
        }
        when(sanction) {
            is AccountSanction -> applyAffectedPlayers { it.name == sanction.offender }
            is IPSanction -> applyAffectedPlayers { it.ip == sanction.ip }
            is UUIDSanction -> applyAffectedPlayers { it.playerInformation.uuid.contentEquals(sanction.uuid) }
        }
    }

    internal fun onSanctionRevoked(sanction: Sanction) {
        val reporter = sanction.reporter?.let(World::getPlayer)?.getOrNull()
        fun applyAffectedPlayers(predicate: (Player) -> Boolean) {
            val applicablePlayers = World.getPlayers().filter(predicate)
            applicablePlayers.forEach { player ->
                player.user = player.user?.copy(sanctions = player.activeSanctions - sanction)
                player.notify("Your ${sanction.type.formattedName} has been revoked.")
            }
            if (applicablePlayers.isEmpty())
                reporter?.notify("You have successfully revoked the sanction, but none of the affected players were online.")
            else
                reporter?.notify("You have successfully revoked the sanction, ${applicablePlayers.size} online players were affected.")
        }
        when(sanction) {
            is AccountSanction ->  {
                applyAffectedPlayers { it.name == sanction.offender }
                GrandExchangeHandler.restoreOffers(sanction.offender)
            }
            is IPSanction -> applyAffectedPlayers { it.ip == sanction.ip }
            is UUIDSanction -> applyAffectedPlayers { it.playerInformation.uuid.contentEquals(sanction.uuid) }
        }
    }
}
