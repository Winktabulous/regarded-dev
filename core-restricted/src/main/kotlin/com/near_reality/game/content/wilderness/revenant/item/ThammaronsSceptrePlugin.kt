package com.near_reality.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*


/**
 * Handles the item container actions of the Thammaron's sceptre.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class ThammaronsSceptrePlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap = HashBiMap.create<Int, Int>().apply {
        put(THAMMARONS_SCEPTRE, THAMMARONS_SCEPTRE_U)
        put(THAMMARONS_SCEPTRE_A, THAMMARONS_SCEPTRE_AU)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        THAMMARONS_SCEPTRE_U to arrayOf(
            Item(REVENANT_ETHER, 7_500),
        ),
        THAMMARONS_SCEPTRE_AU to arrayOf(
            Item(REVENANT_ETHER, 7_500),
        )
    )
) {
    override fun handle() {
        super.handle()
        bind("Swap") { player, item, container, slotId ->
            val otherId = when(item.id) {
                THAMMARONS_SCEPTRE -> THAMMARONS_SCEPTRE_A
                THAMMARONS_SCEPTRE_A -> THAMMARONS_SCEPTRE
                THAMMARONS_SCEPTRE_U -> THAMMARONS_SCEPTRE_AU
                THAMMARONS_SCEPTRE_AU -> THAMMARONS_SCEPTRE_U
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
