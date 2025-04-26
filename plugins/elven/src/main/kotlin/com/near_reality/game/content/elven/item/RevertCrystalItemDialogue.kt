package com.near_reality.game.content.elven.item

import com.near_reality.game.content.crystal.recipes.CrystalCorrupted
import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult.SUCCESS
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents a [Dialogue] for reverting the [recipe] back into its build materials,
 * excluding the crystal shards it cost to make the item.
 *
 * @author Stan van der Bend
 */
class RevertCrystalItemDialogue(
    player: Player,
    private val slot: Int,
    private val item: Item,
    private val recipe: CrystalRecipe,
) : Dialogue(player) {

    override fun buildDialogue() {
        val materials = when (recipe) {
            is CrystalChargeable -> recipe.materials
            is CrystalCorrupted -> listOf(Item(recipe.weaponToCorrupt.inactiveId, 1))
            else -> error("Unsupported recipe $recipe")
        }
        val materialsString = materials.joinToString(separator = " and ") {
            val amountString = if (it.amount == 1)
                "one"
            else
                it.amount.toString()
            var finalString = "$amountString ${it.name.lowercase()}"
            if (it.amount > 1)
                finalString += "s"
            finalString
        }
        plain("When you dismantle this you will receive $materialsString but ${Colour.RED.wrap("no crystal shards")}.")
        options("Are you sure you want to dismantle this?") {
            "Yes, dismantle it." {
                player.inventory.run {
                    val requiredSpace = materials.count { !it.isStackable || !containsItem(it.id) }
                    if (checkSpace(requiredSpace - 1)) { // - 1 because we also delete an item!
                        if (deleteItem(slot, item).result == SUCCESS) {
                            addItems(*materials.map { Item(it) }.toTypedArray())
                            player.sendMessage("You revert the ${item.name} back into its raw materials.")
                        }
                    } else
                        player.dialogue { plain("You do not have enough space in your inventory to do this!") }
                }
            }
            "No, I want to keep it" {}
        }
    }
}
