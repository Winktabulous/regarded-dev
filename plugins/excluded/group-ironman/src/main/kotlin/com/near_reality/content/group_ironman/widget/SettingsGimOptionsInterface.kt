package com.near_reality.content.group_ironman.widget

import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface

class SettingsGimOptionsInterface : InterfaceScript() {
    init {
        GameInterface.SETTINGS_GIM_OPTIONS {
            "Back"(5) {
                GameInterface.SETTINGS_GIM.open(player)
            }
        }
    }
}
