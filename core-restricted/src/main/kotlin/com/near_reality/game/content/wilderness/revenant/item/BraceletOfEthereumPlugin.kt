package com.near_reality.game.content.wilderness.revenant.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult

/**
 * @author Kris | 25. aug 2018 : 19:16:07
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)}
 */
@Suppress("unused")
class BraceletOfEthereumPlugin : ItemPlugin() {

    override fun handle() {
        bind("Toggle-absorption") { player: Player, _: Item?, _: Int ->
            toggleAbsorption(player)
        }
        bind("Toggle absorption") { player: Player, _: Item?, _: Int ->
            toggleAbsorption(player)
        }
        bind("Uncharge") { player: Player, item: Item, slotId: Int ->
            val ether = item.charges
            if (player.inventory.checkSpace()) {
                if (player.inventory.addItem(Item(ItemId.REVENANT_ETHER, ether)).result != RequestResult.OVERFLOW) {
                    item.charges -= ether
                    if (item.id == ItemId.BRACELET_OF_ETHEREUM) {
                        item.id = ItemId.BRACELET_OF_ETHEREUM_UNCHARGED
                        player.inventory.refresh(slotId)
                    }
                    player.sendMessage("You successfully uncharge your bracelet of ethereum.")
                } else
                    player.sendMessage("You have too much revenant ether in your inventory.")
            }
        }
        bind("Dismantle") { player: Player, item: Item?, slotId: Int ->
            player.inventory.deleteItem(item)
            player.inventory.addOrDrop(Item(ItemId.REVENANT_ETHER, 250))
            player.sendMessage("You successfully dismantle your bracelet of ethereum.")
        }
    }

    private fun toggleAbsorption(player: Player) {
        val oldValue = player.getNumericAttribute("ethereum absorption").toInt()
        player.addAttribute("ethereum absorption", if (oldValue == 1) 0 else 1)
        player.sendMessage("Absorption on the bracelet has been " + (if (oldValue == 1) "disabled." else "enabled."))
    }

    override fun getItems(): IntArray=
        intArrayOf(ItemId.BRACELET_OF_ETHEREUM, ItemId.BRACELET_OF_ETHEREUM_UNCHARGED)
}
