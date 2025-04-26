package com.near_reality.content.group_ironman.player.actions

import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.content.group_ironman.player.pendingIronmanGroup
import com.near_reality.scripts.player.actions.PlayerActionScript
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class GimInvitePlayerAction : PlayerActionScript() {
    init {
        "Invite" {
            if (player.gameMode != other.gameMode) {
                player.sendMessage("${other.name}'s Hardcore status is different to yours. " +
                        "They can change their mode by collecting a different iron helmet from the crate near the GIM tutor.")
            } else {
                val group = player.finalisedIronmanGroup ?: player.pendingIronmanGroup
                if (group == null)
                    player.sendDeveloperMessage("Cannot invite players because you are not currently in a group")
                else if (!group.isLeader(player))
                    player.sendMessage("Only group leaders can invite players to their group.")
                else if (other.finalisedIronmanGroup != null || other.pendingIronmanGroup != null)
                    player.sendMessage("${other.name} is already creating a group.")
                else {
                    val name = player.name
                    val newGroup = group == player.pendingIronmanGroup
                    player.sendMessage("Inviting to ${if(newGroup) "create" else "join"} a group.")
                    other.dialogue {
                        plain(
                            "You have been invited to join $name's group. Once the group is " +
                                    "final you cannot change your group for a while, and your hardcore " +
                                    "status will be lost upon leaving the group."
                        )
                        options("Really accept invitation to group?") {
                            dialogueOption("Yes.", true) {
                                if (group.join(other)) {
                                    if (newGroup) {
                                        other.pendingIronmanGroup = group
                                        group.allMembers.forEach {
                                            it.ifOnline {
                                                GameInterface.FORM_GIM_TAB.open(this)
                                            }
                                        }
                                    }
                                }
                            }
                            dialogueOption("No.", true)
                        }
                    }
                }
            }
            true
        }
    }
}
