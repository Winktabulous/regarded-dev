package com.near_reality.game.content.elven.obj

import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool
import com.near_reality.game.content.elven.canMake
import com.near_reality.game.content.elven.item.MakeCrystalItemDialogue
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import mgi.types.config.items.ItemDefinitions

/**
 * Represents the [Dialogue] opened when interacting with the [SingingBowl].
 *
 * @author Stan van der Bend
 */
class SingCrystalDialogue(player: Player) : Dialogue(player) {

    override fun buildDialogue() {
        // Add Signet manually to dialogue so all the other useless plugins don't get registered...
        val all = CrystalRecipe.all + CrystalTool.CelestialSignet
        val recipesCarried = all
            .filter { it.hasMaterials(player) }

        val recipesAvailable = recipesCarried
            .filter { player.canMake(it) }

        if (recipesAvailable.isEmpty()) {
            if (recipesCarried.isEmpty()) {
                player.dialogue {
                    plain("You do not have all of the required materials or requirements to make anything.")
                }
            } else {
                val recipe = recipesCarried.first()
                val productName = ItemDefinitions.nameOf(recipe.productItemId)
                player.skills.checkLevel(SkillConstants.CRAFTING, recipe.requiredCrafting, "make a $productName")
                player.skills.checkLevel(SkillConstants.SMITHING, recipe.requiredSmithing, "make a $productName")
            }
        } else
            player.dialogueManager.start(MakeCrystalItemDialogue(player, recipesAvailable))
    }
}
