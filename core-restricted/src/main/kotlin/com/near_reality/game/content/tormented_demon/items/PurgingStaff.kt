package com.near_reality.game.content.tormented_demon.items

import com.near_reality.game.content.seq
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
class PurgingStaff : ItemOnObjectAction {
    override fun handleItemOnObjectAction(player: Player?, item: Item?, slot: Int, furnace: WorldObject?) {
        if (player == null || item == null) return
        val battlestaff = player.inventory.getItemById(BATTLESTAFF)
        val tormentedSynapse = player.inventory.getItemById(TORMENTED_SYNAPSE)
        val hammer = player.inventory.getItemById(HAMMER)
        val ironBar = player.inventory.getItemById(IRON_BAR)

        if (battlestaff == null) {
            player.sendMessage("You need a battle Staff to smith the Purging Staff.")
            return
        }
        if (tormentedSynapse == null) {
            player.sendMessage("You need a Tormented Synapse to smith the Purging Staff.")
            return
        }
        if (hammer == null) {
            player.sendMessage("You need a Hammer to smith the Purging Staff.")
            return
        }
        if (ironBar == null) {
            player.sendMessage("You need an Iron Bar to smith the Purging Staff.")
            return
        }

        if (player.skills.getLevel(Skills.SMITHING) < 55) {
            player.sendMessage("You need a smithing level of 55 to smith the Purging Staff.")
            return
        }
        if (player.skills.getLevel(Skills.CRAFTING) < 74) {
            player.sendMessage("You need a Crafting level of 74 to make the Purging Staff.")
            return
        }

        if (player.inventory.deleteItem(ironBar).result == RequestResult.SUCCESS &&
            player.inventory.deleteItem(tormentedSynapse).result == RequestResult.SUCCESS &&
            player.inventory.deleteItem(battlestaff).result == RequestResult.SUCCESS) {
            player seq Animation.SMITH.id
            player.inventory.addItem(Item(PURGING_STAFF, 1))
            player.skills.addXp(Skills.SMITHING, 13.0)
            player.skills.addXp(Skills.CRAFTING, 730.0)
            player.dialogue {
                item(
                    PURGING_STAFF,
                    "You hammer the metal onto the battlestaff's tip, forming a brazier. You then insert the synapse into the brazier. The metal turns red hot and the wood smoulders into hardened ash."
                )
            }
        }
    }

    override fun getItems(): Array<Any> =
        arrayOf(BATTLESTAFF, TORMENTED_SYNAPSE)

    override fun getObjects(): Array<Any> =
        arrayOf(ObjectId.ANVIL)
}