package com.near_reality.game.model.item

import com.near_reality.game.model.item.protection.ItemProtectionValueManager
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.container.impl.death.ItemMapping
import com.zenyte.game.world.entity.player.container.impl.death.ItemVariationMapping
import mgi.types.config.items.ItemDefinitions
import kotlin.math.max

fun getItemValue(itemId: Int): Int {
//    val notNotedId = itemId.unnotedOrDefault
//    val newValue = try {
//        val itemConfig = ItemConfigManager[notNotedId]
//        itemConfig.ecoValue?.takeIf { it > 0 }
//            ?:itemConfig.grandExchangeAverage?.takeIf { it > 0 }
//    } catch (e: Exception) {
//        null
//    }
    return ItemDefinitions.getSellPrice(itemId)
}
fun getItemHighAlchValue(itemId: Int): Int {
    return (ItemDefinitions.get(itemId).price * 0.6).toInt()
}
fun getItemLowAlchValue(itemId: Int): Int {
    return (ItemDefinitions.get(itemId).price * 0.4).toInt()
}


fun getProtectionValue(itemId: Int): Long {
    return ItemMapping.map(ItemVariationMapping.map(itemId))
        .sumOf { ItemProtectionValueManager.getProtectionValue(it) }
        .toLong()
}

val Item.protectionValue get() = getProtectionValue(id)
fun Item.getValue(): Long = getItemValue(id).toLong() * amount

fun Iterable<Item>.sortedByProtectionValue() = sortedByDescending {
    max(getItemValue(it.id).toLong(), getProtectionValue(it.id))
}

fun MutableList<Item>.sortByProtectionValue() = sortByDescending {
    max(getItemValue(it.id).toLong(), getProtectionValue(it.id))
}

private val Int.unnotedOrDefault: Int
    get() = ItemDefinitions.get(this).unnotedOrDefault
