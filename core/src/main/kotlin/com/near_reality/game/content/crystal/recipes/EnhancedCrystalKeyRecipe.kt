package com.near_reality.game.content.crystal.recipes

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.game.content.crystal.CrystalRecipe
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult

object EnhancedCrystalKeyRecipe : CrystalRecipe {

    override val productItemId: Int = ItemId.ENHANCED_CRYSTAL_KEY
    override val crystalShardCost: Int = 10
    override val requiredCrafting: Int = 80
    override val requiredSmithing: Int = 80
    override val craftingExperience: Int = 500
    override val smithingExperience: Int = 500

    override fun hasMaterials(player: Player): Boolean =
        player.inventory.containsItems(ingredients())

    override fun hasMaterialsForX(player: Player): Int {
        val keyCount = player.inventory.getAmountOf(ItemId.CRYSTAL_KEY)
        val shardSets = player.inventory.getAmountOf(CRYSTAL_SHARD) / 10

        return minOf(keyCount, shardSets)
    }

    override fun deleteMaterials(player: Player): Boolean =
        player.inventory.deleteItems(*ingredients().toTypedArray()).result == RequestResult.SUCCESS

    private fun CrystalRecipe.ingredients() =
        listOf(Item(ItemId.CRYSTAL_KEY), Item(CRYSTAL_SHARD, crystalShardCost))
}
