package com.near_reality.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId

/**
 * Handles the item container actions of the Viggora's chainmace.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class ViggorasChainMacePlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap =  HashBiMap.create<Int, Int>().apply {
        put(ItemId.VIGGORAS_CHAINMACE, ItemId.VIGGORAS_CHAINMACE_U)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        ItemId.VIGGORAS_CHAINMACE_U to arrayOf(Item(ItemId.REVENANT_ETHER, 7_500))
    )
)
