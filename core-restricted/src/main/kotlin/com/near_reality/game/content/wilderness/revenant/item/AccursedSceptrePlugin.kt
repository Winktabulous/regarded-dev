package com.near_reality.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*


/**
 * Handles the item container actions of the accursed sceptre.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class AccursedSceptrePlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap = HashBiMap.create<Int, Int>().apply {
        put(ACCURSED_SCEPTRE_27665, ACCURSED_SCEPTRE_U_27662)
        put(ACCURSED_SCEPTRE_A_27679, ACCURSED_SCEPTRE_AU_27676)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        ACCURSED_SCEPTRE_U_27662 to arrayOf(
            Item(SKULL_OF_VETION),
            Item(THAMMARONS_SCEPTRE_U)
        ),
        ACCURSED_SCEPTRE_AU_27676 to arrayOf(
            Item(SKULL_OF_VETION),
            Item(THAMMARONS_SCEPTRE_AU)
        )
    )
){

    override fun handle() {
        super.handle()
        bind("Swap") { player, item, container, slotId ->
            val otherId = when(item.id) {
                ACCURSED_SCEPTRE_27665 -> ACCURSED_SCEPTRE_A_27679
                ACCURSED_SCEPTRE_A_27679 -> ACCURSED_SCEPTRE_27665
                ACCURSED_SCEPTRE_U_27662 -> ACCURSED_SCEPTRE_AU_27676
                ACCURSED_SCEPTRE_AU_27676 -> ACCURSED_SCEPTRE_U_27662
                else -> null
            }
            if (otherId != null) {
                if (player.inventory.container == container) {
                    container[slotId].id = otherId
                    player.inventory.refresh(slotId)
                } else {
                    player.equipment.set(slotId, Item(otherId).apply { charges = item.charges })
                    player.equipment.refresh(slotId)
                }
            }
        }
    }
}
