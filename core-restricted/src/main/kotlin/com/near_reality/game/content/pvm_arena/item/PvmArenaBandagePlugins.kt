package com.near_reality.game.content.pvm_arena.item

import com.near_reality.game.content.pvm_arena.area.PvmArenaFightArea
import com.zenyte.game.content.consumables.drinks.Potion
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnPlayerPlugin
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.Toxins
import com.zenyte.game.world.entity.player.Player

/*
Bandages are a consumable item obtainable during the PVM Arena activity.
They restore 20 hitpoints, 29 prayer points, and 20% run energy when consumed.
They also cure poison, boost the player's combat stats, and provide the effect of a stamina potion.
 */

/**
 * Handles the "Heal" action for the PVM Arena Bandages.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class PvmArenaBandageHealPlugin : ItemPlugin() {

    override fun handle() {
        bind("Heal") { player, item, _, slot ->
            if (deleteBandagesIfUsedOutsideOfPvmArena(player, item))
                return@bind
            applyBandage(player, player, slot, item)
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.BANDAGES_25730)
}

/**
 * Handles the "Use" on player action for the PVM Arena Bandages.
 * Applies the bandage's effects to the target player.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class PvmArenaBandageOnPlayerPlugin : ItemOnPlayerPlugin {

    override fun handleItemOnPlayerAction(player: Player?, item: Item?, slot: Int, target: Player?) {
        player?:return
        item?:return
        target?:return
        if (deleteBandagesIfUsedOutsideOfPvmArena(player, item))
            return
        applyBandage(player, target, slot, item)
    }


    override fun getItems(): IntArray =
        intArrayOf(ItemId.BANDAGES_25730)
}

private fun deleteBandagesIfUsedOutsideOfPvmArena(player: Player, item: Item): Boolean {
    if (!PvmArenaFightArea.inAnyFightArea(player.location)) {
        player.inventory.deleteItem(item.id, player.inventory.getAmountOf(item.id))
        player.sendDeveloperMessage("You can only use this item in the PVM Arena.")
        return true
    }
    return false
}

fun applyBandage(
    player: Player,
    target: Player,
    slot: Int,
    item: Item,
) {
    if (player.inventory.deleteItem(slot, Item(item.id, 1)).succeededAmount >= 1) {
        if (!PvmArenaFightArea.inAnyFightArea(target.location)) {
            player.sendDeveloperMessage("You can only use this item in the PVM Arena.")
            return
        }
        if (player != target)
            player.sendMessage("You apply the bandage to ${target.name}.")
        else
            player.sendMessage("You apply the bandage to yourself.")
        target.heal(20)
        target.prayerManager.restorePrayerPoints(29)
        target.toxins.cureToxin(Toxins.ToxinType.POISON)
        Potion.STAMINA_POTION.onConsumption(target)
        listOf(
            Potion.MAGIC_POTION,
            Potion.RANGING_POTION,
            Potion.SUPER_ATTACK_POTION,
            Potion.SUPER_STRENGTH_POTION,
            Potion.SUPER_DEFENCE_POTION,
        ).flatMap { it.boosts().toList() }.forEach {
            it.apply(target)
        }
    }
}
