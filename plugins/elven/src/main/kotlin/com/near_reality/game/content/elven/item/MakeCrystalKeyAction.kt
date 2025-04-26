package com.near_reality.game.content.elven.item

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.elven.canMake
import com.near_reality.game.content.elven.produce
import com.zenyte.game.item.ItemId.CRYSTAL_KEY
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Action
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue

class MakeCrystalKeyAction(
    private val recipe: CrystalRecipe,
    private val amount: Int,
) : Action() {

    private var remaining = amount

    override fun start() =
        player.canMake(recipe) && recipe.hasMaterials(player)

    override fun process() =
        remaining > 0 && recipe.hasMaterials(player)

    override fun processWithDelay(): Int {
        if (!recipe.hasMaterials(player))
            return -1
        player.run {
            val quantity = recipe.hasMaterialsForX(player)
            if (deleteMaterialsForX(player, quantity)) {
                animation = Animation(8459)
                sendSound(SoundEffect(2256, 1, 8))
                val product = recipe.produce()
                product.amount = quantity
                inventory.addItem(product)
                skills.addXp(SkillConstants.CRAFTING, recipe.craftingExperience.toDouble() * quantity)
                skills.addXp(SkillConstants.SMITHING, recipe.smithingExperience.toDouble() * quantity)
                dialogue {
                    item(product, "With the help of the crystal bowl, " +
                            "you sing a beautiful song and shape the crystals")
                }
            }
        }
        remaining--
        return 1
    }


    private fun deleteMaterialsForX(player: Player, count: Int) : Boolean {
        val crystalShards = count * 10
        if (crystalShards > 0 && player.inventory.deleteItem(CRYSTAL_SHARD, crystalShards).result != RequestResult.SUCCESS)
            return false
        return player.inventory.deleteItem(CRYSTAL_KEY, count).result == RequestResult.SUCCESS
    }
}