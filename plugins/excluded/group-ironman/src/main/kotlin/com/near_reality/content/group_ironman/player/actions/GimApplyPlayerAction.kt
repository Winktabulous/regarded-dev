package com.near_reality.content.group_ironman.player.actions

import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.content.group_ironman.player.pendingIronmanGroup
import com.near_reality.content.group_ironman.player.pendingIronmanGroupApplication
import com.near_reality.scripts.player.actions.PlayerActionScript
import com.zenyte.game.world.entity.player.MessageType

class GimApplyPlayerAction : PlayerActionScript() {
    init {
        "Apply" {
            if (player.name == other.pendingIronmanGroupApplication) {
                val otherGroup = other.finalisedIronmanGroup ?: other.pendingIronmanGroup
                if (otherGroup != null) {
                    player.sendMessage("${other.name} has already joined a group.")
                } else {
                    val myGroup = player.finalisedIronmanGroup ?: other.pendingIronmanGroup
                    if (myGroup == null)
                        player.sendMessage("You do not currently have a group.")
                    else if (myGroup.join(other)) {
                        player.sendMessage("You accept ${other.name}'s application.")
                        other.sendMessage("You join the group creation process")
                    }
                }
            } else {
                val group = other.pendingIronmanGroup
                if (group == null) {
                    player.sendMessage("${other.name} is not currently creating a group")
                } else {
                    player.sendMessage("Applying to create a group with ${other.name}.")
                    other.packetDispatcher.sendMessage("|${player.name} wishes to help create your group.", MessageType.CLAN_GIM_FORM_GROUP, player.username)
                }
            }
            true
        }
    }
}
