@file:Suppress("DuplicatedCode")

package com.near_reality.plugins.interfaces.death

import com.near_reality.game.model.item.protectionValue
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.content.gravestone.GravestoneExt.getItemReclaimCost
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.format
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import java.util.*

/**
 * @author Kris | 11/06/2022
 */
class ItemsKeptOnDeathInterface : InterfaceScript() {
    private val keepDowngradedWithoutOrnamentKit = ItemId.MUDSKIPPER_HAT
    private val keepDowngraded = ItemId.BURNT_FISH_343
    private val deleted = ItemId.BURNT_FISH_357
    private val kept = ItemId.BURNT_FISH
    private val gravestoneDowngraded = ItemId.BURNT_FISH_369
    private val goToGravestone = ItemId.BURNT_FISH_367
    private val turnToCoins = ItemId.BURNT_FISHCAKE
    private val lostToKiller = ItemId.JUG_OF_BAD_WINE

    private var Player.protectItemSetting by attribute("ikod_protect_item") {
        prayerManager.isActive(Prayer.PROTECT_ITEM)
    }
    private var Player.skulledSetting by attribute("ikod_skulled") {
        variables.isSkulled
    }
    private var Player.wildernessLevelSetting by attribute("ikod_deep_wilderness") {
        WildernessArea.getWildernessLevel(location).orElse(0)
    }
    private var Player.isPvPDeath by attribute("ikod_pvp_death") {
        false
    }
    private var Player.itemsKeptOnDeathContainer by attribute("ikod_container") {
        Container(ContainerPolicy.ALWAYS_STACK, ContainerType.ITEMS_KEPT_ON_DEATH, Optional.of(this))
    }
    private var Player.itemsKeptOnDeathDescriptionsContainer by attribute("ikod_container_desc") {
        Container(ContainerPolicy.ALWAYS_STACK, ContainerType.ITEMS_LOST_ON_DEATH, Optional.of(this))
    }

    fun Player.clearAttributes() {
        temporaryAttributes.remove("ikod_protect_item")
        temporaryAttributes.remove("ikod_skulled")
        temporaryAttributes.remove("ikod_deep_wilderness")
        temporaryAttributes.remove("ikod_pvp_death")
        itemsKeptOnDeathContainer.clear()
        itemsKeptOnDeathDescriptionsContainer.clear()
    }

    val showInfoButtons = "Show Info Buttons"(12) {
        player.run {
            when (slotID) {
                0 -> reopenInterface(!protectItemSetting, skulledSetting, isPvPDeath, wildernessLevelSetting)
                1 -> reopenInterface(protectItemSetting, !skulledSetting, isPvPDeath, wildernessLevelSetting)
                2 -> reopenInterface(protectItemSetting, skulledSetting, !isPvPDeath, wildernessLevelSetting)
                else -> reopenInterface(
                    protectItemSetting,
                    skulledSetting,
                    isPvPDeath,
                    if (wildernessLevelSetting >= 20) 0 else 21
                )
            }
        }
    }
    val guideRiskValue = "Guide Risk Value"(18)

    fun Player.reopenInterface(protectItem: Boolean, skulled: Boolean, pvp: Boolean, wildernessLevel: Int) {
        protectItemSetting = protectItem
        skulledSetting = skulled
        isPvPDeath = pvp
        wildernessLevelSetting = wildernessLevel
        showInfoButtons.sendComponentSettings(this, 3, AccessMask.CONTINUE)
        val total = updateItemsKeptInventories(skulled, protectItem, wildernessLevel, pvp)
        guideRiskValue.sendComponentText(this, "Guide risk value:<br><col=ffffff>${total.format()}</col>")
    }

    fun Player.updateItemsKeptInventories(
        skulled: Boolean,
        protectItem: Boolean,
        wildernessLevel: Int,
        pvp: Boolean
    ): Long {
        var keptCount = 0
        if (protectItem) keptCount++
        if (!skulled) keptCount += 3
        val inventoryItems = inventory.container.items.values.map(Item::copy).asSequence()
        val equipmentItems = equipment.container.items.values.map(Item::copy).asSequence()
        val deepWilderness = wildernessLevel > 20
        val items = inventoryItems + equipmentItems
        val (alwaysKept, notAlwaysKept) = items.filterByPredicate {
            ItemPlugin.getDeathPlugin(it.id).itemDeathHandler.handle(
                this,
                it,
                keptCount,
                deepWilderness,
                pvp
            ).isAlwaysKeptOnDeath
        }
        val (alwaysLost, notAlwaysLost) = notAlwaysKept.filterByPredicate {
            ItemPlugin.getDeathPlugin(it.id).itemDeathHandler.handle(
                this,
                it,
                keptCount,
                deepWilderness,
                pvp
            ).isAlwaysLostOnDeath
        }
        val sorted = notAlwaysLost.sortedByDescending { item ->
            val result = ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, keptCount, true, true)
            result.kept.separated.sumOf { it.protectionValue } + result.lost.separated.sumOf { it.protectionValue }
        }
        val (protected, notProtected) = sorted.filterKept(keptCount)
        val msg =
            if (varManager.getBitValue(10465) > 0) "Some of your items are being held in a gravestone. If you die again before collecting them, in a place where gravestones are used, your items will usually be added to <col=ffffff>the same gravestone</col>, with any overflow <col=ff0000>permanently lost</col>." else null
        refreshItemsKeptOnDeath(
            skulled = skulled,
            prayer = protectItem,
            wildernessLevel = wildernessLevel,
            killedByPlayer = pvp,
            message = msg,
            protectedItems = protected
        )
        val placeholderInv = itemsKeptOnDeathDescriptionsContainer
        val inv = itemsKeptOnDeathContainer
        var slot = 0
        for (item in alwaysKept) {
            /* CS2 exception */
            if (item.id == ItemId.OLD_SCHOOL_BOND_UNTRADEABLE || item.id == ItemId._50_DONATOR_SCROLL) {
                continue
            }
            inv[slot] = item
            placeholderInv[slot] = Item(kept)
            slot++
        }
        for (item in alwaysLost) {
            inv[slot] = item
            val result =
                ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, keptCount, deepWilderness, pvp)
            when (result.status.status) {
                ItemDeathStatus.DELETE -> {
                    placeholderInv[slot] = Item(deleted)
                }
                ItemDeathStatus.DROP_ON_DEATH -> {
                    placeholderInv[slot] = Item(lostToKiller)
                }
                ItemDeathStatus.GO_TO_GRAVESTONE -> {
                    val fee = getItemReclaimCost(item)
                    placeholderInv[slot] = Item(goToGravestone, fee + 1)
                }
                else -> {
                    throw IllegalStateException("Unknown behavior for item $item")
                }
            }
            slot++
        }
        for (item in notProtected) {
            inv[slot] = item
            val result =
                ItemPlugin.getDeathPlugin(item.id).itemDeathHandler.handle(this, item, keptCount, deepWilderness, pvp)
            when (result.status.status) {
                ItemDeathStatus.KEEP_ON_DEATH -> {
                    placeholderInv[slot] = Item(kept)
                }
                ItemDeathStatus.GO_TO_GRAVESTONE, ItemDeathStatus.GO_TO_GRAVESTONE_OR_DROP_ON_GROUND -> {
                    val fee = getItemReclaimCost(item)
                    placeholderInv[slot] = Item(goToGravestone, fee + 1)
                }
                ItemDeathStatus.KEEP_DOWNGRADED -> {
                    placeholderInv[slot] = Item(keepDowngraded)
                }
                ItemDeathStatus.GRAVESTONE_DOWNGRADED -> {
                    val fee = getItemReclaimCost(item)
                    placeholderInv[slot] = Item(gravestoneDowngraded, fee + 1)
                }
                ItemDeathStatus.DOWNGRADED_WITHOUT_ORNAMENT_KIT -> {
                    placeholderInv[slot] = Item(keepDowngradedWithoutOrnamentKit)
                }
                ItemDeathStatus.TURNED_TO_COINS -> {
                    val price = (item.sellPrice * 0.12).toInt()
                    if (price <= 0) {
                        placeholderInv[slot] = Item(deleted)
                    } else {
                        placeholderInv[slot] = Item(turnToCoins, price + 1)
                    }
                }
                ItemDeathStatus.DROP_ON_DEATH -> {
                    placeholderInv[slot] = Item(lostToKiller)
                }
                ItemDeathStatus.DELETE -> placeholderInv[slot] = Item(deleted)
                else -> throw IllegalArgumentException("Status is null.")
            }
            slot++
        }
        inv.isFullUpdate = true
        inv.refresh(this)
        placeholderInv.isFullUpdate = true
        placeholderInv.refresh(this)
        return notProtected.sumOf { it.protectionValue * it.amount.toLong() }
    }

    fun Player.refreshItemsKeptOnDeath(
        skulled: Boolean,
        prayer: Boolean,
        wildernessLevel: Int,
        killedByPlayer: Boolean,
        message: String?,
        protectedItems: Sequence<Item>
    ) {
        val protected = ArrayList<Int>(4)
        for (item in protectedItems) {
            protected += item.id
        }
        val count = protected.size
        while (protected.size < 4) {
            protected += -1
        }
        packetDispatcher.sendClientScript(
            972,
            if (skulled) 1 else 0,
            if (prayer) 1 else 0,
            wildernessLevel,
            if (killedByPlayer) 1 else 0,
            message ?: "",
            count,
            protected[0],
            protected[1],
            protected[2],
            protected[3]
        )
    }

    inline fun Sequence<Item>.filterByPredicate(crossinline predicate: (Item) -> Boolean): Pair<Sequence<Item>, Sequence<Item>> {
        val alwaysLost = filter { predicate(it) }
        val notAlwaysLost = filterNot { predicate(it) }
        return alwaysLost to notAlwaysLost
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

    init {
        GameInterface.ITEMS_KEPT_ON_DEATH {
            opened {
                clearAttributes()
                val protectItem = protectItemSetting
                val skulled = skulledSetting
                val wildernessLevel = wildernessLevelSetting
                val pvp = isPvPDeath
                sendInterface()
                reopenInterface(protectItem, skulled, pvp, wildernessLevel)
            }
        }
    }
}
