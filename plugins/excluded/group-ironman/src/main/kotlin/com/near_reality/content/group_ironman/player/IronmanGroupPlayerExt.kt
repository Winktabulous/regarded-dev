package com.near_reality.content.group_ironman.player

import com.near_reality.api.service.user.UserPlayerHandler
import com.near_reality.content.group_ironman.*
import com.near_reality.game.packet.out.chat_channel.ChatChannelSettings
import com.near_reality.game.packet.out.chat_channel.ChatChannelType
import com.near_reality.game.packet.out.chat_channel.ChatChannelUpdate
import com.near_reality.game.world.entity.player.hardcoreIronGroupDeathHandlingOverride
import com.near_reality.game.world.entity.player.ironGroupMessageHandler
import com.near_reality.game.world.entity.player.ironGroupTradeAddItemCheck
import com.zenyte.game.world.entity.player.LogLevel
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.GameMode


/**
 * Gets the [IronmanGroupType] for the [player's game mode][Player.gameMode] or `null` if none applies.
 */
val Player.ironmanGroupType
    get() = IronmanGroupType.values.find { it.gameMode == gameMode }

/**
 * Returns `true` when the player is in a finalised iron-man group.
 */
val Player.inIronManGroup
    get() = finalisedIronmanGroup != null

/**
 * Sets the "Invite" or "Apply" player option used during group creation.
 */
fun Player.trySetApplyOrInvitePlayerOption() {
    if (ironmanGroupType != null) {
        if (ironmanGroupInvitingMode) {
            setIronGroupInvitable(true)
            return
        } else if (!inIronManGroup) {
            if(inIronmanGroupCreationInterface) setIronGroupInvitable(true)
            else setIronGroupApplyable(true)
            return
        }
    }
    if(findPlayerOption("Apply").isPresent && findPlayerOption("Apply").asInt == 1)
        setIronGroupApplyable(false)
    if(findPlayerOption("Invite").isPresent && findPlayerOption("Invite").asInt == 1)
        setIronGroupInvitable(false)
}

/**
 * Sends the [ChatChannelSettings] packet for the [group] to this [Player].
 */
fun Player.sendChatChannelSettingsPacket(group: IronmanGroup) {
    send(group.channel.toSettingsPacket())
}

fun Player.removeFromChatChannel() {
    send(ChatChannelUpdate.Remove(ChatChannelType.GIM))
}

fun Player.sendChatChannelPacket(group: IronmanGroup) {
    send(group.channel.toUpdatePacket())
}

fun Player.finishLoadingGIM() {
    varManager.sendBit(13060, 3)
}

fun Player.updateInviteButton() {
    if (finalisedIronmanGroup?.leaderUsername == username) {
        if (ironmanGroupInvitingMode) {
            varManager.sendBit(13063, 1)
        } else {
            varManager.sendBit(13063, 2)
        }
    }
}

/**
 * Set the [hardcoreIronGroupDeathHandlingOverride] attribute for this [Player].
 */
fun Player.setHardcoreDeathHandler(group: IronmanGroup) {
    hardcoreIronGroupDeathHandlingOverride = { p, cause -> group.onDeath(p, cause) }
}

/**
 * Set the game mode of this [Player] to [GameMode.GROUP_IRON_MAN]
 * and removes [hardcoreIronGroupDeathHandlingOverride].
 */
fun Player.revertHardcoreStatus() {
    hardcoreIronGroupDeathHandlingOverride = null
    UserPlayerHandler.updateGameMode(this, GameMode.GROUP_IRON_MAN) { success ->
        if (!success) {
            log(
                LogLevel.ERROR,
                "Failed to update game-mode " + GameMode.GROUP_IRON_MAN + " in API, setting anyways."
            )
        }
        gameMode = GameMode.GROUP_IRON_MAN
        sendMessage("Your group lost Hardcore status.")
    }
}

/**
 * Sets the [ironGroupTradeAddItemCheck] handler for this [Player],
 * make sure trade value restrictions are applied as defined in the argued [group].
 */
fun Player.setTradeRestrictions(group: IronmanGroup) {
    ironGroupTradeAddItemCheck = { item ->
        val member = group.findMember(this@setTradeRestrictions)
        if (member == null) {
            sendDeveloperMessage("Did not find group member instance, cannot trade")
            false
        } else {
            val value = item.sellPrice.toLong().times(item.amount)
            val restriction = member.tradeRestriction
            sendDeveloperMessage("Attempting to trade value $value with trade restriction $restriction")
            if (!restriction.canTradeValue(member, value)) {
                sendMessage("You cannot trade this much yet.")
                false
            } else
                true
        }
    }
}

fun Player.setGroupMessaging(group: IronmanGroup) {
    ironGroupMessageHandler = group.channel::sendPlayerMessage
}

fun Player.resetGIM() {
    ironGroupTradeAddItemCheck = null
    ironGroupMessageHandler = null
    varManager.sendBit(13063, 0)
    varManager.sendBit(13060, 0)
    removeFromChatChannel()
    send(ChatChannelSettings.Remove(ChatChannelType.GIM))
    hardcoreIronGroupDeathHandlingOverride = null
}

fun Player.accessSharedStorage(handler: IronmanGroupStorage.() -> Unit) {
    val group = finalisedIronmanGroup
    if (group == null)
        sendDeveloperMessage("Could not do bank action because not in a group.")
    else {
        val member = group.findMember(this)
        if (member == null)
            sendDeveloperMessage("Could not do bank action because not member of the group ${group.name}")
        else
            handler(group.sharedStorage)
    }
}

fun Player.leaveGroup() {
    val group = finalisedIronmanGroup
    if (group == null) {
        sendDeveloperMessage("Could not (cancel) leave because you're not in a group.")
        return
    }
    when {
        group.isDeparting(this) -> group.cancelLeave(this)
        group.isLeader(this) -> promptOtherLeader(group, group.allMembers.filter { it.username != username })
        else -> group.leaveVoluntarily(this)
    }
}

private fun Player.promptOtherLeader(
    group: IronmanGroup,
    candidates: List<IronmanGroupMember>
) {
    val nextLeaderCandidate = candidates.maxByOrNull { it.rank }
    if (nextLeaderCandidate == null)
        noOtherLeaderDialogue()
    else {
        val formattedOtherName = nextLeaderCandidate.username.uppercase().replace("_", " ")
        options("Make $formattedOtherName the next group leader?") {
            "Yes." {
                options("Are you sure you wish to make $formattedOtherName the Leader?") {
                    "No. I've changed my mind." {}
                    "Yes. Make $formattedOtherName the leader." {
                        group.setLeader(nextLeaderCandidate)
                        group.leaveVoluntarily(this@promptOtherLeader)
                    }
                }
            }
            "No." {
                promptOtherLeader(group, candidates-nextLeaderCandidate)
            }
        }
    }
}


fun Player.noOtherLeaderDialogue() = dialogue {
    plain(
        "No one in your group was eligible to be leader, because they are " +
                "already flagged to leave the Iron group."
    )
}

/**
 * At launch, it was possible to get a different XP rate for GIM,
 * this code corrects that.
 */
fun Player.correctXpRates() {
    if (skillingXPRate != 20 || combatXPRate != 20) {
        sendMessage("Your skill experience has been adjusted due to invalid xp-rate.")
        for (skill in 0 until SkillConstants.COUNT) {
            val mod = if (Skills.isCombatSkill(skill)) 0.125 else 0.25
            val newExperience = skills.experience[skill]
                .times(mod)
                .coerceAtLeast(Skills.getDefaultExperience(skill))
            skills.experience[skill] = newExperience
            skills.level[skill] = Skills.getLevelForXP(newExperience)
        }
        attributes["skilling_xp_rate"] = 20
        attributes["combat_xp_rate"] = 20
    }
}
