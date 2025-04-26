package com.near_reality

import com.zenyte.CacheManager
import com.zenyte.game.content.grandexchange.GrandExchangePriceManager
import com.zenyte.game.content.grandexchange.JSONGEItemDefinitionsLoader
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.items.ItemDefinitions
import java.io.File

fun main() {
    CacheManager.loadCache(Cache.openCache("cache/data/cache/"))

    ItemDefinitions().load()
    JSONGEItemDefinitionsLoader().parse()
    GrandExchangePriceManager().apply {
        read()
        write()
    }

    val protectionValuesFile = File("data/items/protection_values.csv")
    val printWriter = protectionValuesFile.printWriter()
    for (itemDef in ItemDefinitions.getDefinitions()) {
        if (itemDef != null) {
            if (!itemDef.isNoted) {
                printWriter.println("${itemDef.id},${itemDef.name},${ItemDefinitions.getSellPrice(itemDef.id)}")
            }
        }
    }
    printWriter.flush()
    printWriter.close()
}
