package com.near_reality.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId

/**
 * Handles the item container actions of the craw's bow.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class CrawsBowPlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap = HashBiMap.create<Int, Int>().apply {
        put(ItemId.CRAWS_BOW, ItemId.CRAWS_BOW_U)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        ItemId.CRAWS_BOW_U to arrayOf(Item(ItemId.REVENANT_ETHER, 7_500))
    )
)
