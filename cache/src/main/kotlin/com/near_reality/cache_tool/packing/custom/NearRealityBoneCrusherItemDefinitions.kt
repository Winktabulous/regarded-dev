package com.near_reality.cache_tool.packing.custom

import com.zenyte.game.item.ItemId
import mgi.types.config.items.ItemDefinitions

object NearRealityBoneCrusherItemDefinitions {

    private val bonecrusherItemIds = listOf(
        ItemId.BONECRUSHER,
        ItemId.BONECRUSHER_NECKLACE,
        ItemId.DRAGONBONE_NECKLACE
    )

    private val options = listOf("Check", "Uncharge", "Charge", "Check/Uncharge")

    @JvmStatic
    fun removeChargingOptions() {
        bonecrusherItemIds.forEach {
            val itemDef = ItemDefinitions.get(it)
            itemDef.inventoryOptions.withIndex().filter { options.contains(it.value) }.forEach {
                itemDef.setOption(it.index, "")
            }

            itemDef.parameters?.int2ObjectEntrySet()?.filter { options.contains(it.value) }?.forEach {
                itemDef.parameters.remove(it.intKey)
            }
            itemDef.pack()
        }
    }
}
