package com.near_reality.game.content.tormented_demon.items

import com.near_reality.game.content.seq
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
class ScorchingBow : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        if (player == null) return
        if (player.skills.getLevel(Skills.FLETCHING) < 74) {
            player.sendMessage("You need a Fletching level of 74 to smith the Scorching Bow.")
            return
        }
        if (player.inventory.deleteItem(from).result == RequestResult.SUCCESS &&
            player.inventory.deleteItem(to).result == RequestResult.SUCCESS) {
            player seq Animation.SCORCHING_BOW_FLETCH.id
            player.inventory.addItem(Item(SCORCHING_BOW, 1))
            player.skills.addXp(Skills.FLETCHING, 730.0)
            player.dialogue {
                item(
                    SCORCHING_BOW,
                    "You string the synapse onto the bow. The magical wood burns from the inside, twisting and cracking before settling into a gently smouldering state."
                )
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(TORMENTED_SYNAPSE, MAGIC_LONGBOW_U)
}