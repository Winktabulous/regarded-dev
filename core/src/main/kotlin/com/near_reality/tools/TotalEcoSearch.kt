package com.near_reality.tools

import com.zenyte.game.world.entity.player.container.Container

/**
 * @author Jire
 */
object TotalEcoSearch {

    @JvmStatic
    fun main(args: Array<String>) {
        val itemID = args[0].toInt()

        WealthScanner.loadDefault()

        val sum = WealthScanner.allAccounts()
            .sumOf { p ->
                var count = p.bank.container.countOf(itemID)
                count += p.inventory.container.countOf(itemID)
                count += p.equipment.container.countOf(itemID)
                count += p.lootingBag.container.countOf(itemID)
                count += p.gravestone.container.countOf(itemID)
                count
            }
        println("There are ${"%,d".format(sum)} of item ID $itemID in the whole economy")
    }

    private fun Container.countOf(itemID: Int) = getAmountOf(itemID).toBigInteger()

}