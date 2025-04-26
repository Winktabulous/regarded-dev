package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.GameInterface
import com.zenyte.game.world.`object`.ObjectId

class AncientPlaqueObjectAction : ObjectActionScript() {
    init {
        ObjectId.ANCIENT_PLAQUE_42935 {
            when (option) {
                "Read" -> {
                    player.interfaceHandler.sendInterface(GameInterface.STONE_TEXT_INTERFACE)
                    player.packetDispatcher.sendComponentText(
                        GameInterface.STONE_TEXT_INTERFACE,
                        5,
                        "<br><br><br><br><br><col=D1C06E>Vita brevis breviter in brevi finietur,<br><col=D1C06E>Mors venit velociter quae neminem veretur,<br><col=D1C06E>Omnia mors perimit et nulli miseretur."
                    )
                }
            }
        }
    }
}
