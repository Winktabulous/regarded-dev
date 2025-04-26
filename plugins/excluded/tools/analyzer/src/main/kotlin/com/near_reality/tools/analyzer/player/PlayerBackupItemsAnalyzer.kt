package com.near_reality.tools.analyzer.player

import com.near_reality.tools.backups.player.PlayerBackup
import com.near_reality.tools.backups.player.PlayerItems
import com.near_reality.tools.backups.player.PlayerItemsDecoder
import com.zenyte.CacheManager
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.items.ItemDefinitions
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.zip.ZipInputStream

fun main() {
    val cache = Cache.openCache("./cache/data/cache")
    CacheManager.loadCache(cache)
    ItemDefinitions().load(cache)
    val backupsDir = Paths.get(
        "./data/backups",
        "10-09-2022/ItemContainers"
    ).toFile()

    val backups = PlayerBackup.ItemContainers
    val playerItemsByTime = backupsDir.listFiles()
        .filter { it.nameWithoutExtension.endsWith(backups.zipFileSuffix) }
        .associate { file ->
            val timeString = file.nameWithoutExtension.removeSuffix(backups.zipFileSuffix)
            val dateTime = LocalDateTime.parse(timeString, backups.dateFormatter)
            val map = mutableMapOf<String, PlayerItems>()
            file.inputStream().use { inputStream ->
                val zis = ZipInputStream(inputStream)
                while (true) {
                    val entry = zis.nextEntry ?: break
                    val bytes = zis.readBytes()
                    val name = entry.name.removeSuffix(".${backups.subfileExtension}")
                    map[name] = PlayerItemsDecoder().decode(bytes)
                    zis.closeEntry()
                }
                zis.close()
            }
            dateTime to map
        }

    val itemsByName= mutableMapOf<String, MutableMap<LocalDateTime, PlayerItems>>()
    for ((time, playerItems) in playerItemsByTime) {
        for ((name, items) in playerItems) {
            itemsByName.getOrPut(name) { mutableMapOf() }[time] = items
        }
    }

    for ((name, itemsByTime) in itemsByName) {
        println(name)
        for ((time, items) in itemsByTime.toSortedMap()) {
            val timeString = backups.dateFormatter.format(time)
            println("\t$timeString: ")
            println("\t\tinventory:   ${items.inventoryContainer.values.map { "${it.amount}x ${it.name}" }}")
            println("\t\tequipment:   ${items.equipmentContainer.values.map { "${it.amount}x ${it.name}" }}")
            println("\t\tlooting bag: ${items.lootingBagContainer.values.map { "${it.amount}x ${it.name}" }}")
            println("\t\tbank:        ${items.bankContainer.values.map { "${it.amount}x ${it.name}" }}")
            println("\t\tretrieval:   ${items.retrievalContainer.values.map { "${it.amount}x ${it.name}" }}")
        }
    }
}
