package com.near_reality.tools

import com.zenyte.game.world.entity.player.container.Container

/**
 * @author Jire
 */
object EcoSearch {

    @JvmStatic
    fun main(args: Array<String>) {
        val itemID = args[0].toInt()

        WealthScanner.loadDefault()

        WealthScanner.allAccounts()
            .map { p ->
                var count = p.bank.container.countOf(itemID)
                count += p.inventory.container.countOf(itemID)
                count += p.equipment.container.countOf(itemID)
                count += p.lootingBag.container.countOf(itemID)
                count += p.gravestone.container.countOf(itemID)
                p to count
            }
            .filter { it.second > 0 }
            .sortedBy { it.second }
            .forEach { (p, count) -> println("\"${p.username}\" had ${"%,d".format(count)}") }
    }

    private fun Container.countOf(itemID: Int) = getAmountOf(itemID)

}