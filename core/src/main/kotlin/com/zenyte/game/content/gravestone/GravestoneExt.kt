@file:Suppress("DuplicatedCode")

package com.zenyte.game.content.gravestone

import com.near_reality.game.model.item.protectionValue
import com.zenyte.game.GameInterface
import com.zenyte.game.content.ItemRetrievalService
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.content.trouver.TrouverData
import com.zenyte.game.format
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.containers.LootingBag
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import kotlin.math.min

/**
 * @author Kris | 13/06/2022
 */
object GravestoneExt {

    private fun Player.reclaim() {
        for (slotId in 0 until retrievalService.container.size) {
            val item = retrievalService.container[slotId] ?: continue
            inventory.container.deposit(this, retrievalService.container, slotId, item.amount)
        }
        retrievalService.container.shift()
        inventory.refresh()
        retrievalService.container.refresh(this)
        if (retrievalService.container.isEmpty) removeGravestone()
    }

    fun Player.removeGravestone() {
        sendMessage("You successfully retrieved everything from your gravestone.")
        interfaceHandler.closeInterface(GameInterface.GRAVESTONE_RECLAIM)
        packetDispatcher.sendClientScript(3478, -1, -1)
        varManager.sendBitInstant(10465, 0)
        varManager.sendBitInstant(10464, 0)
        gravestone.removeGravestone()
    }

    fun Player.informGravestone() {
        sendMessage("A <col=ef1020>gravestone</col> contains some of your recently dropped items; you can retrieve them by visiting it. The <col=ef1020>World Map</col> will show its location if the area has a map.")
        val remaining = varManager.getBitValue(10465)
        val minutes = remaining / 100
        val seconds = ((remaining % 100) * 0.6).toInt()
        val minString = if (minutes == 1) "1 minute" else "$minutes minutes"
        val secString = if (seconds == 1) "1 second" else "$seconds seconds"
        sendMessage("In <col=ef1020> $minString, $secString</col> it will expire, after which your items can be retrieved from <col=ef1020>Death's Office</col> instead.")
    }

    fun Player.reclaimGravestoneItems() {
        if (!retrievalService.isLocked) return reclaim()
        if (unlockGravestone()) reclaim()
    }

    fun Player.unlockGravestone(): Boolean {
        val coinsInBank = bank.getAmountOf(ItemId.COINS_995)
        val coinsInInventory = inventory.getAmountOf(ItemId.COINS_995)
        val coinsInRetrievalService = retrievalService.container.getAmountOf(ItemId.COINS_995)
        val total = coinsInBank + coinsInInventory + coinsInRetrievalService
        val cache = getRetrievalServiceCache(retrievalService.container.items.values)
        val cost = cache.unlockCost
        if (cost > total) {
            sendMessage(
                "You do not have enough coins to pay for the fee. " +
                        "You may discard some items to reduce the cost by dragging them into the incinerator."
            )
            return false
        }
        var required = cost
        if (coinsInInventory > 0) {
            val toRemove = min(required, coinsInInventory)
            required -= toRemove
            inventory.deleteItem(Item(ItemId.COINS_995, toRemove))
            val amount = toRemove.format()
            sendMessage("Payment has been taken from your inventory: $amount x Coins")
        }
        if (required > 0 && coinsInBank > 0) {
            val toRemove = min(required, coinsInBank)
            bank.remove(Item(ItemId.COINS_995, toRemove))
            val amount = toRemove.format()
            sendMessage("Payment has been taken from your bank: $amount x Coins")
        }
        if (required > 0 && coinsInRetrievalService > 0) {
            val toRemove = min(required, coinsInRetrievalService)
            retrievalService.container.remove(Item(ItemId.COINS_995, toRemove))
            retrievalService.container.refresh(this)
            val amount = toRemove.format()
            sendMessage("Payment has been taken from your gravestone: $amount x Coins")
        }
        retrievalService.isLocked = false
        return true
    }

    fun Player.createGravestone(location: Location, killer: Player? = null): Pair<List<Item>, List<Item>> {
        val (itemsLost, gravestoneItems) = calculateGravestoneItems(killer != null)
        gravestone.createGravestone(location, gravestoneItems.subList(0, min(120, gravestoneItems.size)))
        retrievalService.isLocked = true
        retrievalService.container.addAll(gravestoneItems)
        retrievalService.type = ItemRetrievalService.RetrievalServiceType.GRAVESTONE
        return itemsLost to gravestoneItems
    }

    fun Player.calculateGravestoneItems(pvp: Boolean, bypassGravestone: Boolean = false): Pair<List<Item>, List<Item>> {
        var keptCount = 0
        if (prayerManager.isActive(Prayer.PROTECT_ITEM)) keptCount++
        if (!variables.isSkulled) keptCount += 3
        val wildernessLevel = WildernessArea.getWildernessLevel(location).orElse(0)
        val gravestoneItems = mutableListOf<Item>()
        if(!bypassGravestone) {
            val retrievalServiceItems =
                if (!retrievalService.`is`(ItemRetrievalService.RetrievalServiceType.GRAVESTONE)) {
                    emptySequence()
                } else {
                    retrievalService.container.items.values.map(Item::copy).asSequence()
                }
            gravestoneItems += retrievalServiceItems
        }

        val inventoryItems = inventory.container.items.values.map(Item::copy).asSequence()
        val equipmentItems = equipment.container.items.values.map(Item::copy).asSequence()

        val deepWilderness = wildernessLevel > 20
        var items = inventoryItems +
                equipmentItems

        val itemsLost = mutableListOf<Item>()

        if (inventory.containsAnyOf(LootingBag.OPENED.id, LootingBag.CLOSED.id)) {
            val lootingBagItems = lootingBag.container.items.values.map(Item::copy).asSequence()
            lootingBag.container.clear()
            itemsLost += lootingBagItems
        }

        if(inventory.containsAnyOf(26302, 26301)) {
            val dragonHidePouchItems = dragonhidePouch.container.items.values.map(Item::copy).asSequence()
            dragonhidePouch.container.clear()
            items += dragonHidePouchItems
        }
        if(inventory.containsAnyOf(26306, 26304)) {
            val bonePouchItems = bonePouch.container.items.values.map(Item::copy).asSequence()
            bonePouch.container.clear()
            items += bonePouchItems
        }

        if (inventory.containsItem(ItemId.RUNE_POUCH) || inventory.containsItem(ItemId.DIVINE_RUNE_POUCH)) {
            val runePouchItems = runePouch.container.items.values.map(Item::copy).asSequence()
            runePouch.container.clear()
            items += runePouchItems
        }

        if (inventory.containsItem(ItemId.RUNE_POUCH_L)) {
            val secondaryRunePouchItems = secondaryRunePouch.container.items.values.map(Item::copy).asSequence()
            secondaryRunePouch.container.clear()
            items += secondaryRunePouchItems
        }

        retrievalService.container.clear()
        inventory.container.clear()
        equipment.container.clear()

        val (alwaysKept, notAlwaysKept) = items.filterByPredicate {
            ItemPlugin.getDeathPlugin(it.id).itemDeathHandler.handle(this, it, keptCount, deepWilderness, pvp).isAlwaysKeptOnDeath
        }
        val (alwaysLost, notAlwaysLost) = notAlwaysKept.filterByPredicate {
            ItemPlugin.getDeathPlugin(it.id).itemDeathHandler.handle(this, it, keptCount, deepWilderness, pvp).isAlwaysLostOnDeath
        }
        val itemsToInventory = mutableListOf<Item>()

        val sorted = notAlwaysLost.sortedByDescending { item ->
            val result = ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, keptCount, deepWilderness, pvp)
            result.kept.separated.sumOf { it.protectionValue } + result.lost.separated.sumOf { it.protectionValue }
        }
        val (protected, notProtected) = sorted.filterKept(keptCount)
        itemsToInventory += protected
        for (item in alwaysKept) {
            val result = ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, keptCount, deepWilderness, pvp)
            itemsToInventory += result.kept.separated
            itemsToInventory += result.lost.separated
        }
        for (item in alwaysLost + notProtected) {
            val result = ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, keptCount, deepWilderness, pvp)
            val status = result.status.status
            if (status == ItemDeathStatus.GO_TO_GRAVESTONE || status == ItemDeathStatus.GRAVESTONE_DOWNGRADED) {
                gravestoneItems += result.lost.separated
            } else {
                val isTrev = TrouverData.getByProtectedId(item.id);
                if(isTrev.isPresent) {
                    itemsToInventory += Item(isTrev.get().originalItemId)
                } else {
                    itemsLost += result.lost.separated
                }
            }
            itemsToInventory += result.kept.separated
        }
        for (item in itemsToInventory)
            inventory.container.add(item)
        inventory.refresh()
        equipment.refresh()

        val sortedGravestoneItems = gravestoneItems.sortedByDescending { item ->
            val result = ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, keptCount, deepWilderness, pvp)
            result.kept.separated.sumOf { it.protectionValue } + result.lost.separated.sumOf { it.protectionValue }
        }
        if(bypassGravestone) {
            // send straight to death
            retrievalService.container.addAll(sortedGravestoneItems)
            retrievalService.type = ItemRetrievalService.RetrievalServiceType.GRAVESTONE
        }
        return itemsLost to sortedGravestoneItems
    }


    fun Sequence<Item>.filterKept(count: Int): Pair<Sequence<Item>, Sequence<Item>> {
        if (count <= 0) return emptySequence<Item>() to this
        val keep = ArrayList<Item>(count)
        val drop = ArrayList<Item>(100)
        for (item in this) {
            var amount = item.amount
            if (keep.size < count) {
                for (i in 0 until item.amount) {
                    keep += item.copy(1)
                    amount--
                    if (keep.size >= count) break
                }
            }
            if (amount <= 0) continue
            val remainder = item.copy(amount)
            drop += remainder
        }
        return keep.asSequence() to drop.asSequence()
    }

    fun Player.moveItemsToDeathsOffice() {
        for (i in 0 until 120) {
            val item = retrievalService.container.get(i) ?: continue
            gravestone.container.deposit(this, retrievalService.container, i, item.amount)
        }
        gravestone.container.refresh(this)
        retrievalService.container.refresh(this)
    }

    private inline fun Sequence<Item>.filterByPredicate(crossinline predicate: (Item) -> Boolean): Pair<Sequence<Item>, Sequence<Item>> {
        val alwaysLost = filter { predicate(it) }
        val notAlwaysLost = filterNot { predicate(it) }
        return alwaysLost to notAlwaysLost
    }


    fun Player.getRetrievalServiceCache(allItems: Collection<Item> = retrievalService.container.items.values): RetrievalServiceStatus {
        val freeItems = allItems.filter { getItemReclaimCost(it) == 0 }
        val paidItems = allItems.filter { getItemReclaimCost(it) > 0 }
        val sorted = paidItems.sortedByDescending { getItemReclaimCost(it) }
        val highCost = sorted.filter { getItemReclaimCost(it) == 100_000 }
        val mediumCost = sorted.filter { getItemReclaimCost(it) == 10_000 }
        val lowCost = sorted.filter { getItemReclaimCost(it) == 1_000 }
        val totalSum = highCost.sumOf { it.amount * 100_000 } + mediumCost.sumOf { it.amount * 10_000 } + lowCost.sumOf { it.amount * 1_000 }
        return RetrievalServiceStatus(
            freeItems,
            highCost,
            mediumCost,
            lowCost,
            totalSum
        )
    }

    data class RetrievalServiceStatus(
        val freeItems: Collection<Item>,
        val highCostItems: Collection<Item>,
        val mediumCostItems: Collection<Item>,
        val lowCostItems: Collection<Item>,
        val unlockCost: Int
    )


    fun Player.getItemReclaimCost(item: Item): Int {
        val multiplier = if (isIronman) 2 else 1
//        val price = when(item.manualReclaimCost()) {
//            0L -> getItemValue(item)
//            else -> { return (multiplier * item.manualReclaimCost()).toInt()}
//        }
        val price = getItemValue(item)

        if (price < 100_000 * multiplier) {
            return 0
        }
        if (price < 1_000_000 * multiplier) {
            return 1_000
        }
        if (price < 10_000_000 * multiplier) {
            return 10_000
        }
        return 100_000
    }

    private fun Item.manualReclaimCost() : Long {
        return when(this.id) {
            32243, 32245, 32247, 32249, 32251, 32253,
            32255, 32257, 32259, 32261, 32263, 32265
            -> 500_000

            13342, 13329, 13331, 13333, 13335, 21285,
            21776, 21780, 21784, 21898, 20760, 13337,
            ItemId.ASSEMBLER_MAX_CAPE_L,
            ItemId.INFERNAL_MAX_CAPE_L,
            ItemId.FIRE_MAX_CAPE_L,
            ItemId.IMBUED_SARADOMIN_MAX_CAPE_L,
            ItemId.IMBUED_ZAMORAK_MAX_CAPE_L,
            ItemId.IMBUED_GUTHIX_MAX_CAPE_L,
            ItemId.INFERNAL_CAPE,
            ItemId.MASORI_ASSEMBLER,
            ItemId.AVERNIC_DEFENDER
            -> 300_000

            ItemId.FIRE_CAPE,
            ItemId.DRAGON_DEFENDER,
            ItemId.AVAS_ASSEMBLER,
            ItemId.VOID_MELEE_HELM,
            26463, 26465, 26467, 26469, 26471, 26473, 26475, 26477,
            ItemId.VOID_KNIGHT_GLOVES,
            ItemId.ELITE_VOID_TOP,
            ItemId.ELITE_VOID_ROBE,
            ItemId.VOID_KNIGHT_TOP,
            ItemId.VOID_KNIGHT_ROBE,
            ItemId.VOID_RANGER_HELM,
            ItemId.VOID_MAGE_HELM,
            ItemId.RUNE_POUCH
            -> 150_000

            else -> 0L
        }
    }


    fun Player.getItemValue(item: Item): Long {
        val split = ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, 0, true, true)
        /* Need to get the price of the broken-down version for a true estimate. */
        return split.kept.separated.sumOf { it.protectionValue } + split.lost.separated.sumOf { it.protectionValue }
    }
}
