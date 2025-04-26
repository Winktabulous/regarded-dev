package com.near_reality.game.content.dt2.plugins

import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.theduke.DukeSucellusEntity
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue

@Suppress("unused")
class PoisonOnDukeItemAction : ItemOnNPCAction {
    private val orb = Item(ItemId.AWAKENERS_ORB, 1)
    private val poisonNormal = Item(28351, 1)
    private val poisonAwakened = Item(28351, 2)
    private val p2wPoisonNormal = Item(CustomItemId.POTENT_ARDER_MUSCA_POISON, 1)
    private val p2wPoisonAwakened = Item(CustomItemId.POTENT_ARDER_MUSCA_POISON, 2)


    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        if(npc is DukeSucellusEntity && npc.slumbering) {
            if(player.inventory.containsItems(*npc.difficulty.getRequiredItems())) {
                if(player.inventory.deleteItems(*npc.difficulty.getRequiredItems()).result == RequestResult.SUCCESS) {
                    npc.disturb(player)
                    return
                }
            } else if (player.inventory.containsItems(*npc.difficulty.getAlternateItems())) {
                if(player.inventory.deleteItems(*npc.difficulty.getAlternateItems()).result == RequestResult.SUCCESS) {
                    npc.disturb(player)
                    return
                }
            }
        } else {
            player.dialogue {
                plain("This wouldn't have any effect")
            }
        }

    }

    override fun getItems(): Array<Any> {
        return arrayOf(28351, CustomItemId.POTENT_ARDER_MUSCA_POISON, ItemId.AWAKENERS_ORB)
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(12166)
    }

    private fun DT2BossDifficulty.getRequiredItems() : Array<Item> {
        return when (this) {
            DT2BossDifficulty.QUEST -> emptyArray<Item>()
            DT2BossDifficulty.NORMAL -> arrayOf(poisonNormal)
            DT2BossDifficulty.AWAKENED -> arrayOf(orb, poisonAwakened)
        }
    }

    private fun DT2BossDifficulty.getAlternateItems() : Array<Item> {
        return when (this) {
            DT2BossDifficulty.QUEST -> emptyArray<Item>()
            DT2BossDifficulty.NORMAL -> arrayOf(p2wPoisonNormal)
            DT2BossDifficulty.AWAKENED -> arrayOf(orb, p2wPoisonAwakened)
        }
    }
}