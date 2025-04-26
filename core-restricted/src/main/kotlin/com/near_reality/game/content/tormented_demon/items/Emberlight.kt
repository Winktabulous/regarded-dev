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
class Emberlight : ItemOnObjectAction {
    override fun handleItemOnObjectAction(player: Player?, item: Item?, slot: Int, furnace: WorldObject?) {
        if (player == null || item == null) return
        val arclight = player.inventory.getItemById(ARCLIGHT)
        val tormentedSynapse = player.inventory.getItemById(TORMENTED_SYNAPSE)
        val hammer = player.inventory.getItemById(HAMMER)

        if (arclight == null) {
            player.sendMessage("You need an Arclight to smith the Emberlight.")
            return
        }
        if (tormentedSynapse == null) {
            player.sendMessage("You need a Tormented Synapse to smith the Emberlight.")
            return
        }
        if (player.skills.getLevel(Skills.SMITHING) < 74) {
            player.sendMessage("You need a smithing level of 74 to smith the Emberlight.")
            return
        }
        if (hammer == null) {
            player.sendMessage("You need a Hammer to smith the Purging Staff.")
            return
        }

        if (player.inventory.deleteItem(arclight).result == RequestResult.SUCCESS &&
            player.inventory.deleteItem(tormentedSynapse).result == RequestResult.SUCCESS) {
            player seq Animation.SMITH.id
            player.inventory.addItem(Item(EMBERLIGHT, 1))
            player.skills.addXp(Skills.SMITHING, 730.0)
            player.dialogue {
                item(
                    EMBERLIGHT,
                    "The synapse writhes as you hammer it into the sword, flaring fire throughout it. Shards of crystal break off, but what remains hardens into a burning, obsidian-like material."
                )
            }
        }
    }

    override fun getItems(): Array<Any> =
        arrayOf(ARCLIGHT, TORMENTED_SYNAPSE)

    override fun getObjects(): Array<Any> =
        arrayOf(ObjectId.ANVIL)
}