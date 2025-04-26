package com.near_reality.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId

/**
 * Handles the item container actions of the Ursine chainmace.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class UrsineChainMacePlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap =  HashBiMap.create<Int, Int>().apply {
        put(ItemId.URSINE_CHAINMACE_27660, ItemId.URSINE_CHAINMACE_U_27657)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        ItemId.URSINE_CHAINMACE_U_27657 to arrayOf(
            Item(ItemId.CLAWS_OF_CALLISTO),
            Item(ItemId.VIGGORAS_CHAINMACE_U)
        )
    )
)
