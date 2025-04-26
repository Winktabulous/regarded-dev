package com.near_reality.cache_tool.packing.custom

import com.zenyte.game.item.ItemId
import mgi.types.config.items.ItemDefinitions

object NearRealityRaidsItemDefinitions {

    @JvmStatic
    fun makeCavernGrubsStackable() {
        ItemDefinitions.get(ItemId.CAVERN_GRUBS).apply {
            setIsStackable(1)
            pack()
        }
    }

    @JvmStatic
    fun makeKindlingStackable() {
        ItemDefinitions.get(ItemId.KINDLING_20799).apply {
            setIsStackable(1)
            pack()
        }
    }
}
