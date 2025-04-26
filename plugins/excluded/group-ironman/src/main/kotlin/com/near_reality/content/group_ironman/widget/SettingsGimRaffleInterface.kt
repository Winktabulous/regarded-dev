package com.near_reality.content.group_ironman.widget

import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.PaneType
import com.zenyte.game.util.component

class SettingsGimRaffleInterface : InterfaceScript() {
    init {
        GameInterface.SETTINGS_GIM_RAFFLE {
            "Close"(14) {

            }
            "Join Raffle"(15) {

            }
            opened {
                interfaceHandler.sendInterface(id, 21, PaneType.IRON_GROUP_SETTINGS, true)
                packetDispatcher.sendClientScript(3651, 730 component 21, 730 component 1, 1)
            }
        }
    }
}
