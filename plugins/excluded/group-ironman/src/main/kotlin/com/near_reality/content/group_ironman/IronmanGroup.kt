package com.near_reality.content.group_ironman

import com.near_reality.content.group_ironman.area.TheNodeArea
import com.near_reality.content.group_ironman.player.*
import com.near_reality.game.content.challenges.ChallengeType
import com.near_reality.game.packet.out.chat_channel.ChatChannelVar
import com.near_reality.game.util.invoke
import com.near_reality.game.world.entity.player.UsernameProvider
import com.zenyte.game.GameInterface
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.game.world.region.GlobalAreaManager
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

/**
 * Represents a group of ironman players.
 *
 * @author Stan van der Bend
 * @author Leanbow
 *
 * @param creationTime      The [time][Instant] of this group's creation.
 * @param sharedStorage     The [shared storage][IronmanGroupStorage] available to members of this group.
 * @param leaderUsername    The [Player.username] of the group leader.
 * @param ranked            `true` if this group is listed under the GIM hiscores and has various restrictions applied.
 * @param type              The [IronmanGroupType] of this group.
 */
data class IronmanGroup(
    val creationTime: Instant,
    val sharedStorage: IronmanGroupStorage,
    var leaderUsername: String,
    var ranked: Boolean,
    var type: IronmanGroupType,
) : ChallengeType {

    /**
     * The name of this group, is set during group formation.
     */
    override lateinit var name: String

    /**
     * A unique id for this group.
     */
    override lateinit var uuid: UUID

    /**
     * Whether this group is created, thus finalised and added to [IronmanGroupModule.groups].
     */
    var created: Boolean = false

    /**
     * Represents the [IronmanGroupTasks]
     */
    var tasksCompleted = IntArray(2)

    /**
     * Returns a [Set] of all the [IronmanGroupMember] that are in this group,
     * which are either not leaving or are leaving but within the [gracePeriod].
     */
    val allMembers: Set<IronmanGroupMember>
        get() {
            val now = Clock.System.now()
            return activeMembers.toSet() +
                    keysInGracePeriod(now, kickedMembers) +
                    keysInGracePeriod(now, voluntarilyLeftMembers)
        }

    /**
     * A set of the currently active members of the group.
     */
    internal val activeMembers = mutableListOf<IronmanGroupMember>()

    /**
     * A collection of former group members that left on their own accord
     * associated with the [time][Instant] that they left the group.
     */
    internal val voluntarilyLeftMembers = mutableMapOf<IronmanGroupMember, Instant>()

    /**
     * A collection of former group members that were kicked by the [leaderUsername]
     * associated with the [time][Instant] that they were kicked from the group.
     */
    internal val kickedMembers = mutableMapOf<IronmanGroupMember, Instant>()

    /**
     * Only applicable for [hardcore groups][IronmanGroupType.HARDCORE].
     */
    internal var remainingLives = INFINITE_LIVES

    @Transient
    internal lateinit var channel: IronmanGroupChannel

    /**
     * A map of [ChatChannelVar] associated with an [Int] value.
     */
    internal val variables = mutableMapOf<ChatChannelVar, Int>()

    init {
        initVars()
    }

    fun initVars() {
        channel = IronmanGroupChannel(this)
        if (!this::uuid.isInitialized) {
            uuid = UUID.randomUUID()
        }
    }

    /**
     * Finalise the creation of this group, saves it and sets [created] to `true`.
     */
    fun finalizeCreation() {
        activeMembers.forEach {
            it.ifOnline {
                finalisedIronmanGroup = this@IronmanGroup
                inIronmanGroupCreationInterface = false
                GameInterface.ACTIVE_GIM_TAB.open(this)
                initPlayer(this, false)
            }
        }
        if (type == IronmanGroupType.HARDCORE) {
            setRemainingLives(activeMembers.size)
        }
        updateMembersSize()
        channel.refreshChannel()
        created = true
    }

    fun initPlayer(player: Player, updateChannel: Boolean = true) {
        if (isDeparting(player)) {
            val timePassed = timePassedSinceDeparture(player)
            val reason = if (isKicked(player)) "be kicked from" else "leave"
            if (timePassed < gracePeriod)
                player.sendMessage(
                    "You are currently due to ${Colour.RS_RED(reason)} " +
                            "the Ironman group in ${Colour.RS_RED("${(gracePeriod - timePassed).inWholeDays} days")}"
                )
            else {
                player.sendDeveloperMessage("Your grace period is over, you will now be removed from the group.")
                if (!activeMembers.removeIf { it.username == player.username })
                    player.sendDeveloperMessage("Failed to remove you from the group???")
                return
            }
        }
        player.correctXpRates()
        if (type == IronmanGroupType.HARDCORE)
            player.setHardcoreDeathHandler(this)
        else if (player.gameMode == GameMode.GROUP_HARDCORE_IRON_MAN)
            player.revertHardcoreStatus()
        player.setTradeRestrictions(this)
        player.updateInviteButton()
        player.setGroupMessaging(this)
        player.sendChatChannelSettingsPacket(this)
        player.finishLoadingGIM()
        channel.add(player, updateChannel)
    }



    /**
     * Adds the [player] to the [activeMembers].
     */
    fun join(player: Player, bypassCheck: Boolean = false): Boolean {

        if (!bypassCheck && !checkIfCanJoin(player))
            return false

        val previousGroup = player.finalisedIronmanGroup
        previousGroup?.remove(player)
        player.finalisedIronmanGroup = this
        activeMembers += IronmanGroupMember(player.username, Clock.System.now(), 0L)
        if (created) {
            initPlayer(player, true)
        }
        updateMembersSize()
        updateInterfaces()
        trySave()
        return true
    }

    fun remove(player: Player) {
        activeMembers.removeIf { it.username == player.username }
        findKickedMemberWithTime(player)?.first?.run(kickedMembers::remove)
        findVoluntarilyLeavingMemberWithTime(player)?.first?.run(voluntarilyLeftMembers::remove)
        updateMembersSize()
        updateInterfaces()
        trySave()
    }

    private fun checkIfCanJoin(player: Player, fromLeave: Boolean = false): Boolean {

        if (activeMembers.size >= MAX_GROUP_SIZE) {
            player.sendMessage("This group is currently full.")
            return false
        }

        if (player.ironmanGroupType == null) {
            player.sendMessage("Only group ironman members can join groups.")
            return false
        }

        if (GlobalAreaManager.getArea(player) !is TheNodeArea) {
            player.sendMessage("You can only join groups from within The Node.")
            return false
        }
//
//        if (!fromLeave && player.leftTheNode) {
//            player.sendMessage("Only new players can join groups.")
//            return false
//        }

        /*
        Check if we're in a different group already,
         if we're leaving and grace period is over we can join,
         if we're leaving and grace period is not over we cannot join,
         if we're not leaving then we cannot join.
         */
        val otherGroup = player.finalisedIronmanGroup?.takeIf { it != this }
        if (otherGroup != null) {
            if (otherGroup.isDeparting(player)) {
                val timeSinceDeparture = otherGroup.timePassedSinceDeparture(player)
                if (timeSinceDeparture < gracePeriod) {
                    player.sendMessage("You must wait ${gracePeriod - timeSinceDeparture} more time before you can join groups.")
                    return false
                }
            } else {
                player.sendMessage("You're already in group ${otherGroup.name}")
                return false
            }
        }

        if (isDeparting(player)) {
            if (isKicked(player)) {
                player.sendMessage("Cannot join since you were previously kicked by this group's leader.")
                return false
            } else {
                /*
                Check if we're still in the grace period of our voluntary leave request,
                if the grace period is over, we can no longer join the group,
                if the grace period is not over, we can still join the group.
                 */
                val timeSinceDeparture = timePassedSinceDeparture(player)
                if (timeSinceDeparture >= gracePeriod) {
                    player.sendMessage("Your ${gracePeriod.inWholeDays} day grace period is over, you can no longer re-join the group.")
                    return false
                }
                return true
            }
        }
        return true
    }

    /**
     * Removes the [player] from [activeMembers], the [player] can still re-join the group if within [gracePeriod] since leaving.
     */
    fun leaveVoluntarily(player: Player) {
        val member = findMember(player)
        if (member == null) {
            player.sendDeveloperMessage("You are not in this group and can therefore not leave.")
            return
        }

        if (isDeparting(player)) {
            if (isVoluntarilyLeaving(player))
                player.sendDeveloperMessage("You're already pending for voluntary leave.")
            else
                player.sendDeveloperMessage("You've been kicked and thus cannot leave voluntarily.")
            return
        }
        activeMembers -= member
        //Removed for now.
        /*voluntarilyLeftMembers[member] = Clock.System.now()
        player.dialogue {
            plain(
                "You are now setup to leave the group in ${gracePeriod.inWholeDays} days. You can cancel " +
                        "this at any time before that happens by selecting 'Cancel Leave'."
            )
        }*/
        player.dialogue {
            plain("You've left the group.")
        }
        onLeave(member)
    }

    fun cancelLeave(player: Player) {
        if (isKicked(player)) {
            if (isVoluntarilyLeaving(player)) {
                player.sendMessage(
                    Colour.TURQOISE(
                        "Your group leader has chosen to kick you from the Ironman group. " +
                                "You were already due to leave voluntarily."
                    )
                )
            } else
                player.dialogue { plain("You cannot cancel your leave request as it was set by your Team Group's leader.") }
            return
        }
        val voluntarilyLeft = voluntarilyLeftMembers.keys.find { it.username == player.username }
        if (voluntarilyLeft == null) {
            player.sendDeveloperMessage("Cannot cancel leave because you're not pending to leave")
            return
        }
        if (checkIfCanJoin(player, true)) {
            player.dialogue { plain("You have successfully cancelled your leave request.") }
            voluntarilyLeftMembers -= voluntarilyLeft
            activeMembers += voluntarilyLeft
            if (type == IronmanGroupType.HARDCORE)
                adjustRemainingLives(1)
        }
    }

    /**
     * Kicks the argued [player] from this group.
     */
    fun kick(player: Player) {
        val member = activeMembers.find { it.username == player.username }?:return
        kick(member)
    }

    /**
     * Kicks the argued [member] from this group.
     */
    fun kick(member: IronmanGroupMember) {
        activeMembers -= member
        kickedMembers[member] = Clock.System.now()
        member.ifOnline {
            sendDeveloperMessage("You were kicked from the ironman group by the group leader.")
        }
        onLeave(member)
    }

    /**
     * Unkicks the argued [member] from this group.
     */
    fun unKick(member: IronmanGroupMember) {
        if (type == IronmanGroupType.HARDCORE)
            adjustRemainingLives(1)
        activeMembers += member
        kickedMembers -= member
        trySave()
    }

    private fun onLeave(member: IronmanGroupMember) {
        member.ifOnline {
            channel.remove(this)
            resetGIM()
            updateInterface(this)
        }
        updateInterfaces()
        trySave()
        if (type == IronmanGroupType.HARDCORE)
            deductLivesAndCheck()
    }

    internal fun trySave() {
        if (created)
            IronmanGroupModule.requestSave()
    }

    private fun updateMembersSize() {
        channel.setVariableInt(ChatChannelVar.GIM_MEMBERS, allMembers.size)
    }

    private fun updateInterfaces() {
        activeMembers.forEach {
            it.ifOnline {
                updateInterface(this)
            }
        }
    }

    private fun updateInterface(player: Player) {
        for (gameInterface in gameInterfaces) {
            if (player.interfaceHandler.isPresent(gameInterface)) {
                player.sendDeveloperMessage("Re-opening interface $gameInterface due to change detected.")
                gameInterface.open(player)
            }
        }
    }

    fun setLeader(member: IronmanGroupMember) {
        leaderUsername = member.username
        IronmanGroupModule.write()
    }

    fun isLeader(usernameProvider: UsernameProvider) =
        leaderUsername == usernameProvider.username

    fun isDeparting(usernameProvider: UsernameProvider) =
        isKicked(usernameProvider) || isVoluntarilyLeaving(usernameProvider)

    fun isKicked(usernameProvider: UsernameProvider) =
        kickedMembers.keys.any { it.username == usernameProvider.username }

    fun isVoluntarilyLeaving(usernameProvider: UsernameProvider) =
        voluntarilyLeftMembers.keys.any { it.username == usernameProvider.username }

    fun getCancellableKickedMembers(): List<IronmanGroupMember> {
        val now = Clock.System.now()
        return kickedMembers.entries.filter { now - it.value < gracePeriod }.map { it.key }
    }

    fun getKickableMembers(): List<IronmanGroupMember> =
        activeMembers.filterNot(::isKicked)

    private fun findKickedMemberWithTime(usernameProvider: UsernameProvider) =
        kickedMembers.entries.find { it.key.username == usernameProvider.username }?.let { it.key to it.value }

    private fun findVoluntarilyLeavingMemberWithTime(usernameProvider: UsernameProvider) =
        voluntarilyLeftMembers.entries.find { it.key.username == usernameProvider.username }?.let { it.key to it.value }

    private fun findDepartingMemberWithTime(usernameProvider: UsernameProvider) =
        findKickedMemberWithTime(usernameProvider)
            ?: findVoluntarilyLeavingMemberWithTime(usernameProvider)

    private fun timePassedSinceDeparture(usernameProvider: UsernameProvider): Duration {
        val currentTime = Clock.System.now()
        val kickTime = kickedMembers.entries.find { it.key.username == usernameProvider.username }?.value
        val leaveTime = voluntarilyLeftMembers.entries.find { it.key.username == usernameProvider.username }?.value
        return (kickTime ?: leaveTime)?.let { time -> currentTime - time } ?: error("Not departing")
    }

    /**
     * Check whether the argued [usernameProvider] is an active member of this group,
     * an active member is one that is either not leaving or kicked, or within the grace period.
     */
    fun isMemberAndInGracePeriod(usernameProvider: UsernameProvider) =
        isMember(usernameProvider) &&
                findDepartingMemberWithTime(usernameProvider)
                    ?.let { (_, time) -> Clock.System.now() - time < gracePeriod }
                ?: true

    private fun keysInGracePeriod(now: Instant, mutableMap: MutableMap<IronmanGroupMember, Instant>) =
        mutableMap.filterValues { now - it < gracePeriod }.keys

    fun isMember(playerName: String) = allMembers.any { it.username == playerName }
    fun isMember(usernameProvider: UsernameProvider) = isMember(usernameProvider.username)
    fun isActiveMember(usernameProvider: UsernameProvider) = activeMembers.any { it.username == usernameProvider.username }
    fun findMember(usernameProvider: UsernameProvider) = activeMembers.find { it.username == usernameProvider.username }
    fun hasName() = this::name.isInitialized

    fun getMemberNamesAsString() : MutableList<String> {
        val list = mutableListOf<String>()
        activeMembers.forEach { list.add(it.username) }
        return list
    }


    companion object {

        internal const val JOIN_GIM_CHANNEL_MESSAGE =
            "|To talk in your Iron Group's channel, start each line of chat with //// or /g."
        internal const val BASE_SHARED_STORAGE_SPACES = 80

        internal const val INFINITE_LIVES = -1
        internal const val MAX_GROUP_SIZE = 5

        internal val gameInterfaces = arrayOf(
            GameInterface.SETTINGS_GIM,
            GameInterface.SETTINGS_GIM_LEAVE,
            GameInterface.SETTINGS_GIM_OPTIONS,
            GameInterface.SETTINGS_GIM_RAFFLE,
            GameInterface.SETTINGS_GIM_STORAGE,
            GameInterface.ACTIVE_GIM_TAB,
            GameInterface.DEFAULT_GIM_TAB,
            GameInterface.FORM_GIM_TAB
        )

        /**
         * Players leaving the group voluntarily will have a 2-day grace period in which they can change their mind.
         *
         * This grace period serves two purposes:
         * 1. allowing players to change their mind
         * 2. provide a safety net for mis-clicks and account hijacking.
         *
         * Members that are kicked from the group will have a 2-day grace period to settle their affairs with the group.
         * During both grace periods, the player can still trade normally with other members of the group
         */
        internal val gracePeriod = 2.days

        fun form(player: Player, prestige: Boolean) {
            val groupType = player.ironmanGroupType
            if (groupType == null) {
                player.sendMessage("Only group ironman players can form a group.")
                return
            }
            val group = IronmanGroup(
                creationTime = Clock.System.now(),
                sharedStorage = IronmanGroupStorage(),
                leaderUsername = player.username,
                ranked = prestige,
                type = groupType,
            )
            if (prestige) {
                group.channel.setVariableInt(ChatChannelVar.GIM_PRESTIGE, 1)
            }
            player.pendingIronmanGroup = group
            group.join(player)
            GameInterface.FORM_GIM_TAB.open(player)
        }

        fun finaliseCreation(group: IronmanGroup) {
            IronmanGroupModule.groups[group.uuid] = group
            group.finalizeCreation()
            IronmanGroupModule.write()
        }

        /**
         * Finds a [IronmanGroup] that the argued [usernameProvider] is a [member][IronmanGroupMember] of
         * or `null` if the [usernameProvider] does not belong to any [IronmanGroup].
         */
        fun find(usernameProvider: UsernameProvider) =  IronmanGroupModule.groups.values.find { it.isMember(usernameProvider) }

        fun find(uuid: UUID) =  IronmanGroupModule.groups[uuid]
    }
}
