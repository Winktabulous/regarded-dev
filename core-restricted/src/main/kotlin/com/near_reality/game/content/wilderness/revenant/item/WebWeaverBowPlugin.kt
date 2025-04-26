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
class WebWeaverBowPlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap =  HashBiMap.create<Int, Int>().apply {
        put(ItemId.WEBWEAVER_BOW_27655, ItemId.WEBWEAVER_BOW_U_27652)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        ItemId.WEBWEAVER_BOW_U_27652 to arrayOf(
            Item(ItemId.FANGS_OF_VENENATIS),
            Item(ItemId.CRAWS_BOW_U)
        )
    )
)
