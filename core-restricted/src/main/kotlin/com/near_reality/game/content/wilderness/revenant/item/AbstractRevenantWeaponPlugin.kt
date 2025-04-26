package com.near_reality.game.content.wilderness.revenant.item

import com.google.common.collect.BiMap
import com.zenyte.game.content.boons.impl.LethalAttunement
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.model.item.degradableitems.DegradableItem
import com.zenyte.game.model.item.pluginextensions.ChargeExtension
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerWrapper
import com.zenyte.game.world.entity.player.dialogue.dialogue
import kotlin.math.max
import kotlin.math.min

/**
 * Handles the item container actions of the revenant items and their charging mechanics.
 *
 * @author Stan van der Bend
 */
abstract class AbstractRevenantWeaponPlugin(
    private val chargedToUnchargedIdMap: BiMap<Int, Int>,
    private val dismantleIngredientsByUnchargedIdMap: Map<Int, Array<Item>>? = null,
) : ItemPlugin(), PairedItemOnItemPlugin, ChargeExtension {

    final override fun getItems(): IntArray =
        chargedToUnchargedIdMap.keys.toIntArray() + chargedToUnchargedIdMap.values.toIntArray()

    final override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> = chargedToUnchargedIdMap.entries.flatMap { (charged, uncharged) ->
        listOf(
            ItemOnItemAction.ItemPair(charged, ItemId.REVENANT_ETHER),
            ItemOnItemAction.ItemPair(uncharged, ItemId.REVENANT_ETHER)
        )
    }.toTypedArray()

    override fun handle() {
        bind("Uncharge") { player: Player, item: Item, container: Container, slotId: Int ->
            val name: String = item.getName()
            if (removeChargesManually(player, item, container, slotId))
                player.sendMessage("You remove the ether from the $name.")
        }
        if (dismantleIngredientsByUnchargedIdMap != null) {
            bind("Dismantle") { player: Player, item: Item, container: Container, slotId: Int ->
                if (chargedToUnchargedIdMap.containsKey(item.id)) {
                    if (removeChargesManually(player, item, container, slotId))
                        player.sendMessage("You remove all charges prior to attempting to dismantle it.")
                }
                if (!player.inventory.hasFreeSlots()) {
                    player.sendMessage("You need at least one free space in your inventory first.")
                    return@bind
                }
                val unchargedId = item.id
                val ingredients = dismantleIngredientsByUnchargedIdMap[unchargedId]
                if (ingredients == null) {
                    player.sendDeveloperMessage("No dismantle ingredients found for item: $unchargedId")
                    return@bind
                }
                player.dialogue {
                    destroyItem(item,
                        "Are you sure you want to dismantle this weapon?",
                        "You will receive its components back."
                    ).onYes {
                        if (container[slotId]?.id == unchargedId) {
                            container.remove(slotId, 1)
                            container.refresh(slotId)
                            player.inventory.addItems(*ingredients)
                        } else
                            player.sendMessage("It appears you no longer that item with you.")
                    }
                }
            }
        }
    }

    /**
     * Remove charges through item container options.
     */
    private fun removeChargesManually(
        player: Player,
        item: Item,
        container: Container,
        slotId: Int,
    ) : Boolean {
        player.inventory
            .addItem(Item(ItemId.REVENANT_ETHER, item.charges))
            .onFailure { it: Item? -> World.spawnFloorItem(it, player) }
        val unchargedId = chargedToUnchargedIdMap[item.id]
        if (unchargedId != null) {
            item.id = unchargedId
            item.charges = 0
            container.refresh(slotId)
            return true
        } else {
            player.sendDeveloperMessage("No uncharged id found for item: ${item.id}")
            return false
        }
    }

    /**
     * Charges removed through usage of the item.
     */
    final override fun removeCharges(player: Player, item: Item, wrapper: ContainerWrapper, slotId: Int, amount: Int) {
        if (amount == 1 && player.boonManager.hasBoon(LethalAttunement::class.java))
            return
        val previousCharges = item.charges
        val unchargedId = chargedToUnchargedIdMap[item.id]
        if (unchargedId == null) {
            player.sendDeveloperMessage("No uncharged id found for item: ${item.id}")
            return
        }
        item.charges = previousCharges - amount
        if (item.charges <= 0) {
            item.id = unchargedId
            wrapper.refresh(slotId)
        } else if (previousCharges > 1000 && item.charges <= 1000)
            player.sendMessage("Your " + item.name + " has ran out of charges.")
    }

    /**
     * Check how many revenant ether charges the revenant item has left.
     */
    final override fun checkCharges(player: Player, item: Item) {
        val name = item.name
        val minimumCharge = if (player.boonManager.hasBoon(LethalAttunement::class.java)) 0 else 1000

        if (item.charges <= minimumCharge) {
            val activationString = if (item.charges > 0) "has been activated" else "has not been activated"
            player.sendMessage("Your " + item.name + " " + activationString + " and is out of charges.")
            return
        }
        val deg = DegradableItem.ITEMS[item.id] ?: return
        val percentage = ChargeExtension.FORMATTER
            .format((max(0.0, (item.charges - 1000).toDouble()) / (deg.maximumCharges - 1000).toFloat() * 100))
        player.sendMessage(
            buildString {
                append("Your $name ")
                append((if (name.contains("legs")) "have " else "has "))
                append(percentage.replace(".0", ""))
                append("% charges remaining.")
            }
        )
    }

    /**
     * Handles the charging of revenant items with revenant ether.
     */
    final override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        val ether = if (from!!.id == ItemId.REVENANT_ETHER) from else to!!
        val weapon = if (ether === from) to!! else from
        val existing = weapon.charges
        val toAdd = min((17000 - existing).toDouble(), ether.amount.toDouble()).toInt()
        val minimumCharge = if (player!!.boonManager.hasBoon(LethalAttunement::class.java)) 1 else 1000
        if (toAdd < minimumCharge && existing == 0) {
            player.sendMessage("You need to charge the " + weapon.name + " with at least $minimumCharge revenant ether.")
            return
        }
        val chargedId = chargedToUnchargedIdMap.inverse()[weapon.id]
        if (chargedId == null) {
            player.sendDeveloperMessage("No charged id found for item: ${weapon.id}")
            return
        }
        val deleted = player.inventory.deleteItem(Item(ether.id, toAdd))
        weapon.charges = existing + deleted.succeededAmount
        weapon.id = chargedId
        player.inventory.refresh(fromSlot, toSlot)
        player.sendMessage("You charge the " + weapon.name + " with " + toAdd + " revenant ether.")
    }
}
