package com.near_reality.plugins.item.customs

import com.near_reality.api.service.vote.totalVoteCredits
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ItemOnPlayerPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Use a vote shard on a player to transfer the vote points.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class VoteShardOnPlayer : ItemOnPlayerPlugin {

    override fun handleItemOnPlayerAction(player: Player?, item: Item?, slot: Int, target: Player?) {
        if (player == null || item == null || target == null) return
        player.dialogue {
            options("Are you sure you wish to transfer ${item.amount} vote shard${if(item.amount > 1) "s" else ""} to ${target.name}?") {
                dialogueOption("Yes", noPlayerMessage = true) {
                    transferVoteShards(player, target, item)
                }
                dialogueOption("No")
            }
        }
    }

    private fun transferVoteShards(player: Player, target: Player, item: Item) {
        if (player.inventory.deleteItem(item).result != RequestResult.SUCCESS) {
            player.sendMessage("You do not have enough vote shards to transfer.")
            return
        }
        target.totalVoteCredits += item.amount
        player.sendMessage("You have transferred ${item.amount} vote shard${if(item.amount > 1) "s" else ""} to ${target.name}.")
        target.sendMessage("${player.name} has transferred ${item.amount} vote point${if(item.amount > 1) "s" else ""} to you.")
    }

    override fun getItems(): IntArray = intArrayOf(CustomItemId.NR_VOTE_SHARD)
}
