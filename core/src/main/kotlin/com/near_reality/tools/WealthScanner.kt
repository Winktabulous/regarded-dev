package com.near_reality.tools

import com.zenyte.CacheManager
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.login.LoginManager
import com.zenyte.game.world.region.XTEALoader
import mgi.tools.jagcached.cache.Cache
import mgi.types.Definitions
import java.math.BigInteger
import java.nio.file.Paths
import kotlin.math.max

/**
 * @author Jire
 */
object WealthScanner {

    val charactersPath = Paths.get("data", "characters")

    fun loadDefault() {
        val cache = Cache.openCache("cache/data/cache", true)

        CacheManager.loadCache(cache)
        CacheManager.loadDefinitions()

        XTEALoader.load("cache/data/objects/xteas.json")

        Definitions.loadDefinitions(Definitions.lowPriorityDefinitions)
    }

    fun allAccounts() = charactersPath.toFile().walkTopDown()
        .mapNotNull { file -> LoginManager.deserializePlayerFromFile(file.nameWithoutExtension) }

    fun forAllAccounts(use: Player.() -> Unit) = allAccounts().forEach(use)

    @JvmStatic
    fun main(args: Array<String>) {
        loadDefault()

        allAccounts()
            .map { p ->
                var value = p.bank.container.value()
                value += p.inventory.container.value()
                value += p.equipment.container.value()
                value += p.lootingBag.container.value()
                value += p.gravestone.container.value()
                p to value
            }
            .filter { it.second > BigInteger.ZERO }
            .sortedBy { it.second }
            .forEach { (p, value) -> println("\"${p.username}\" had ${"%,d".format(value)} value") }
    }

    private fun Container.value(): BigInteger {
        var value = BigInteger.valueOf(0L)
        for ((_, item) in items) {
            item ?: continue
            val amount = BigInteger.valueOf(item.amount.toLong())
            val itemValue = BigInteger.valueOf(
                max(1, max(item.definitions.price, item.sellPrice)).toLong()
            ) * amount
            value += itemValue
        }
        return value
    }

}
