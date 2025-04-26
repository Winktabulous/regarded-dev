package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.game.content.crystal.CrystalSeed
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.dialogue.SkillDialogue


fun openSeedTradeWindow(player: Player) {

    val seedTypes = CrystalSeed.values().filter { player.inventory.containsItem(it.itemId) }
    val seedItems = seedTypes.map { Item(it.itemId) }.toTypedArray()

    if (seedItems.isEmpty()) {
        player.dialogue(NpcId.AMROD) {
            npc("You do not seem to have any crystal seeds adventurer.")
        }
        return
    }

    player.dialogueManager.start(object :
        SkillDialogue(player, "How many do you wise to trade?", *seedItems) {
        override fun run(slotId: Int, amount: Int) {
            val seed = seedTypes[slotId]
            val seedItem = seedItems[slotId]
            val seedName = seedItem.name.replaceFirstChar { it.uppercaseChar() }
            val amountToRemove = player.inventory.getAmountOf(seedItem.id).coerceAtMost(amount)
            if (amountToRemove > 0) {
                player.options("Trade $amountToRemove x $seedName for ${seed.shardReturnRate.times(amountToRemove)} crystal shards?") {
                    "Yes." {
                        val result = player.inventory.deleteItem(seed.itemId, amountToRemove)
                        val shardsItem = Item(CRYSTAL_SHARD, result.succeededAmount.times(seed.shardReturnRate))
                        if (shardsItem.amount > 0) {
                            player.inventory.addItem(shardsItem)
                            player.dialogue {
                                doubleItem(
                                    seedItem.id,
                                    shardsItem.id,
                                    "You trade in your $seedName for ${shardsItem.amount} crystal shards."
                                )
                            }
                        }
                    }
                    "No." {}
                }
            }
        }
    })
}
