package com.near_reality.game.content.gauntlet.interfaces.user

import com.near_reality.scripts.interfaces.user.UserInterfaceScript
import com.zenyte.game.model.ui.InterfacePosition

/**
 * @author Andys1814
 * @author Jire
 */
class GauntletRecipeUserInterface : UserInterfaceScript() {
    init {
        640 {
            option["Close"] = {
                player.interfaceHandler.closeInterface(InterfacePosition.CENTRAL)
            }
        }
    }
}
