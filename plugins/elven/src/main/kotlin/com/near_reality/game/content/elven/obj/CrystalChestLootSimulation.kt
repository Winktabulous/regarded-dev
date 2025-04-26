package com.near_reality.game.content.elven.obj

import com.near_reality.cache_tool.cacheTo
import com.zenyte.CacheManager
import com.zenyte.game.content.skills.thieving.PocketData
import com.zenyte.game.item.Item
import mgi.types.config.items.ItemDefinitions
import mgi.utilities.StringFormatUtil

fun main() {
    CacheManager.loadCache(cacheTo)
    ItemDefinitions().load()

    val times = 100_000

//    simulateLoot(times) { ElvenCrystalChestLoot[null]}
    for (pocketData in PocketData.values()) {

        println("-------------------------------------------")
        println("$pocketData TABLE:")
        println("-------------------------------------------")
        simulateLoot(times) { PocketData.ELF.generateRandomLoot(false) }
        println("-------------------------------------------")
        println()
    }
//    simulateLoot(times) {CrystalLoot[null]}
}

private fun simulateLoot(times: Int, provider: () -> List<Item>) {
    val loot = mutableListOf<Item>()
    repeat(times) {
        loot.addAll(provider())
    }
    loot.groupBy { it.id }.entries
        .sortedBy { it.value.size }
        .forEach {
            println("${ItemDefinitions.nameOf(it.key).padEnd(30)} has a ${it.value.size.toDouble().div(times).times(100).toFloat().toString().padEnd(6) }% chance of dropping" +
                    "(total amount dropped = ${StringFormatUtil.format(it.value.sumOf { it.amount })})"
            )
        }
}
