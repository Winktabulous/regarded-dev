package com.near_reality.game.content.gauntlet.`object`

import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

@Suppress("UNUSED")
class GauntletEgniolPotions : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        if (option == "Read") {
            player.interfaceHandler.run {
                sendInterface(InterfacePosition.CENTRAL, 95)
            }
            player.packetDispatcher.run {
                for ((componentId, line) in strings.withIndex()) {
                    sendComponentText(95, componentId + 1, line)
                }
            }
        }
    }

    override fun getObjects() = arrayOf(
        ObjectId.EGNIOL_POTIONS, // corrupted
        ObjectId.EGNIOL_POTIONS_36076
    )

    private companion object {
        val strings = listOf(
            "Those hoping to survive the Gauntlet will need to",
            "take advantage of the Grym roots found within",
            "the dungeon. The leaves that grow on these roots",
            "can be used to create Egniol potions, which are",
            "able to restore both energy an divinity.",
            "",
            "To create an Egniol potion, follow these steps:",
            "",
            "Fill a vial with water.",
            "Add a Grym leaf to the vial.",
            "Crush ten crystal shards.",
            "Add the crystal dust to the vial."
        )
    }
}
