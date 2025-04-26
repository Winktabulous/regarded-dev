package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.IronmanGroup
import com.near_reality.content.group_ironman.dialogue.CreateIronmanGroupDialogue
import com.near_reality.content.group_ironman.player.inIronManGroup
import com.near_reality.content.group_ironman.player.inIronmanGroupCreationInterface
import com.near_reality.game.model.ui.chat_channel.chatChannelInterfaceType
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.util.component
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask.*

class TabGimDefaultInterface : InterfaceScript() {
    init {
        DEFAULT_GIM_TAB {
            "Create Group"(3) {
                IronmanGroup.form(player, prestige = false)
                //player.dialogueManager.start(CreateIronmanGroupDialogue(player))
            }
            opened {
                inIronmanGroupCreationInterface = false
                if (inIronManGroup)
                    ACTIVE_GIM_TAB.open(this)
                else {
                    chatChannelInterfaceType.sendTabInterface(this, getInterface())
                    packetDispatcher.run {
                        sendClientScript(5261, id component 3, 1, id component 1, "Iron Group")
                        sendClientScript(
                            5261, id component 3, 1, id component 5,
                            "To be part of an Iron Group, you must either get invited to a group or start your own. " +
                                    "The button below will start a new group with you as the leader."
                        )
                    }
                }
            }
        }
    }
}
