package com.near_reality.plugins.item.customs

import com.near_reality.game.content.custom.PolyporeStaff
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Handles the charging and uncharing of the polypore staff.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class PolyporeStaffPlugin : ItemPlugin(), PairedItemOnItemPlugin {

    override fun handle() = bind("Uncharge") { player, item, container, slotId ->
        if (container.freeSlotsSize <= 0 && !container.contains(CustomItemId.POLYPORE_SPORES, 1)){
            player.sendMessage("You need some more free space to uncharge the staff.")
            return@bind
        }
        player.dialogue {
            doubleItem(CustomItemId.POLYPORE_STAFF, CustomItemId.POLYPORE_STAFF_DEG,
                "Are you sure you wish to release all spores from your ${item.name}?")
            options("release all spores from your ${item.name}?") {
                "Yes." {
                    val charges = item.charges
                    if (charges > 0)
                        container.add(Item(CustomItemId.POLYPORE_SPORES, charges))
                    item.id = CustomItemId.POLYPORE_STAFF_DEG
                    item.charges -= charges
                    container.refresh(slotId)
                    container.refresh(player)
                }
                "No." {}
            }
        }
    }

    override fun getItems() = intArrayOf(CustomItemId.POLYPORE_STAFF)

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val sporesItem = when {
            from.id == CustomItemId.POLYPORE_SPORES -> from
            to.id == CustomItemId.POLYPORE_SPORES -> to
            else -> error("Did not find spores (from=$from, to=$to)")
        }
        val staff = if (from == sporesItem) to else from
        val maxToAdd = (PolyporeStaff.maximumCharges - staff.charges).coerceAtLeast(0)
        if (maxToAdd == 0) {
            player.dialogue {
                plain("Your ${staff.name} is already fully charged!")
            }
            return
        }
        val chargesToAdd = sporesItem.amount.coerceAtMost(maxToAdd)
        if (chargesToAdd > 0) {
            if (player.inventory.deleteItem(CustomItemId.POLYPORE_SPORES, chargesToAdd).result == RequestResult.SUCCESS) {
                staff.charges += chargesToAdd
                if (staff.id == CustomItemId.POLYPORE_STAFF_DEG)
                    staff.id = CustomItemId.POLYPORE_STAFF
                player.inventory.refresh(fromSlot, toSlot)
            }
        }
    }

    override fun getMatchingPairs() = arrayOf(
        ItemOnItemAction.ItemPair(CustomItemId.POLYPORE_SPORES, CustomItemId.POLYPORE_STAFF),
        ItemOnItemAction.ItemPair(CustomItemId.POLYPORE_SPORES, CustomItemId.POLYPORE_STAFF_DEG),
    )
}
