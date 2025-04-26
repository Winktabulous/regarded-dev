package com.near_reality.content.group_ironman.widget

import com.near_reality.content.group_ironman.getUnlockedSpaces
import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.PaneType
import com.zenyte.game.util.component

class SettingsGimStorageInterface : InterfaceScript() {
    init {
        GameInterface.SETTINGS_GIM_STORAGE {
            "Close"(8) {
                if (player.interfaceHandler.isPresent(GameInterface.SETTINGS_GIM)) {
                    player.packetDispatcher.closeInterface(730 component 21)
                } else {
                    player.packetDispatcher.closeInterface(724 component 47)
                }
            }
            opened {
                val group = finalisedIronmanGroup?: return@opened
                varManager.sendVarInstant(261, group.tasksCompleted[0])
                varManager.sendVarInstant(262, group.tasksCompleted[1])
                varManager.sendVarInstant(263, group.getUnlockedSpaces())
                varManager.sendVarInstant(264, 200)
                varManager.sendVarInstant(265, 1)//how many weeks until storage unlocks
                if (interfaceHandler.isPresent(GameInterface.SETTINGS_GIM)) {
                    interfaceHandler.sendInterface(id, 21, PaneType.IRON_GROUP_SETTINGS, true)
                    packetDispatcher.sendClientScript(3127, 730 component 21, 730 component 1, 1)
                } else {
                    interfaceHandler.sendInterface(id, 47, PaneType.IRON_BANK, true)
                    packetDispatcher.sendClientScript(3127, 724 component 47, 724 component 1, 1)
                }
            }
        }
    }
}
