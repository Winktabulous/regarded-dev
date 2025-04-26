package com.near_reality.game.content.elven.item

import com.near_reality.game.content.crystal.recipes.CrystalCorrupted
import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.recipes.EnhancedCrystalKeyRecipe
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.dialogue.MakeType
import com.zenyte.plugins.dialogue.SkillDialogue
import mgi.utilities.StringFormatUtil

/**
 * Represents a [SkillDialogue] to handle the creation interface,
 * lists all possible recipes the player can make.
 *
 * @author Stan van der Bend
 */
class MakeCrystalItemDialogue(
    player: Player,
    private val recipes: List<CrystalRecipe>,
) : SkillDialogue(player, "What would you like to make?", *recipes.map { Item(it.productItemId) }.toTypedArray()) {

    override fun type() = MakeType.MAKE

    override fun getMaximumAmount() = 1

    override fun run(slotId: Int, amount: Int) {

        val recipe = recipes[slotId]

        if (recipe is CrystalCorrupted) {
            player.dialogue {
                val baseWeapon = recipe.findCrystalWeaponForRecipe(player) ?: return@dialogue
                val prefix = if (baseWeapon.charges > 0) "charged" else ""
                val name = "$prefix ${baseWeapon.name}"
                val shardCost =
                    Colour.RED.wrap(StringFormatUtil.formatNumberUS(recipe.getShardCost(player)) + " Crystal Shards")
                doubleItem(
                    recipe.weaponToCorrupt.productItemId, recipe.productItemId,
                    "Corrupting your $name will cost $shardCost, any crystal shards currently stored in your bow have been used to reduce the cost."
                )
                options("Corrupt your $name?") {
                    "Yes" { player.actionManager.action = MakeCrystalItemAction(recipe, amount) }
                    "No" {}
                }
            }
        } else if (recipe is EnhancedCrystalKeyRecipe) {
            player.actionManager.action = MakeCrystalKeyAction(recipe, amount)
        } else {
            player.actionManager.action = MakeCrystalItemAction(recipe, amount)
        }
    }
}
