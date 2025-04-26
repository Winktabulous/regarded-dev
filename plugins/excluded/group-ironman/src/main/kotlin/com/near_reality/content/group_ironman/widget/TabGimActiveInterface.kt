package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.player.*
import com.near_reality.game.model.ui.chat_channel.chatChannelInterfaceType
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask.*

class TabGimActiveInterface : InterfaceScript() {
    init {
        ACTIVE_GIM_TAB {
            "Refresh"(2) {
                val group = player.finalisedIronmanGroup
                if (group != null) {
                    player.sendChatChannelSettingsPacket(group)
                    player.sendChatChannelPacket(group)
                }
            }
            "Inviting"(7) {
                val group = player.finalisedIronmanGroup
                if (group != null) {
                    if (group.leaderUsername == player.username) {
                        player.ironmanGroupInvitingMode = !player.ironmanGroupInvitingMode
                        if (!player.ironmanGroupInvitingMode) {
                            player.sendMessage("You've closed applications to your group.")
                        }
                        player.updateInviteButton()
                        player.trySetApplyOrInvitePlayerOption()
                    } else {
                        player.sendMessage("Only the group leader can do that.")
                    }
                }
            }
            "Settings"(8) {
                val group = player.finalisedIronmanGroup
                if (group != null) {
                    SETTINGS_GIM.open(player)
                }
            }
            opened {
                chatChannelInterfaceType.sendTabInterface(this, getInterface())
            }
        }
    }
}
