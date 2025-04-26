package com.near_reality.tools

import com.near_reality.api.model.SanctionLevel
import com.near_reality.api.model.SanctionType
import com.near_reality.api.service.sanction.SanctionPlayerHandler
import com.zenyte.game.content.achievementdiary.Diary
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

/**
 * Cheap way to kick bots.
 *
 * @author Stan van der Bend
 */
object BotPrevention {

    const val MOUSE_CLICKS_ATTRIBUTE_KEY = "mouse-click-count"
    const val KEY_EVENTS_ATTRIBUTE_KEY = "key-event-count"
    const val CAMERA_X_ATTRIBUTE_KEY = "camera-x"
    const val CAMERA_Y_ATTRIBUTE_KEY = "camera-y"

    private fun getBots(threshold: Int, minCombat: Boolean = false, checkCamera: Boolean = false) = World
        .getPlayers()
        .filter {
            (!minCombat || it.combatLevel <= 3) && (
                   it.getNumericTemporaryAttribute(MOUSE_CLICKS_ATTRIBUTE_KEY).toInt() < threshold ||
                   it.getNumericTemporaryAttribute(KEY_EVENTS_ATTRIBUTE_KEY).toInt() < threshold ||
                           (checkCamera && it.temporaryAttributes.containsKey(CAMERA_X_ATTRIBUTE_KEY))
                    )
        }

    fun registerCommands() {
        GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "banbots") {player, args ->
            val (threshold, checkCombat, checkCamera) = args.getArgs()
            getBots(threshold, checkCombat, checkCamera)
                .forEach { offender ->
                    SanctionPlayerHandler.submitSanction(
                        level = SanctionLevel.ACCOUNT,
                        type = SanctionType.BAN,
                        offender = offender,
                        reporter = player,
                        reason = "Auto detected as a bot.",
                    )
                }
        }
        GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "botinfo") {player, args ->
            val (threshold, checkCombat, checkCamera) = args.getArgs()
            val strings = getBots(threshold, checkCombat, checkCamera)
                .map { "${it.username} (${it.ip}, " +
                        "clicks = ${it.getNumericTemporaryAttribute(MOUSE_CLICKS_ATTRIBUTE_KEY)}," +
                        "camera = (" +
                            "${it.getNumericTemporaryAttribute(CAMERA_X_ATTRIBUTE_KEY)}, " +
                            "${it.getNumericTemporaryAttribute(CAMERA_Y_ATTRIBUTE_KEY)}" +
                        ")," +
                        "keys = ${it.getNumericTemporaryAttribute(KEY_EVENTS_ATTRIBUTE_KEY)})" }
            Diary.sendJournal(player, "bots", strings)
        }
        GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "botips") {player, args ->
            val (threshold, checkCombat, checkCamera) = args.getArgs()
            val strings = getBots(threshold, checkCombat, checkCamera)
                .groupBy { it.ip }
                .map { "${it.key} (${it.value.size})" }
            Diary.sendJournal(player, "bot ips", strings)
        }
    }

    private fun Array<String>.getArgs() : Triple<Int, Boolean, Boolean> {
        val threshold = getOrNull(0)?.toIntOrNull()?:1
        val checkCombat = getOrNull(1)?.toIntOrNull() == 1
        val checkCamera = getOrNull(2)?.toIntOrNull() == 1
        return Triple(threshold, checkCombat, checkCamera)
    }
}
