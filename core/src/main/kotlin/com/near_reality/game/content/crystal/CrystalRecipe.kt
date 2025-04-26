package com.near_reality.game.content.crystal

import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.near_reality.game.content.crystal.recipes.CrystalCorrupted
import com.near_reality.game.content.crystal.recipes.EnhancedCrystalKeyRecipe
import com.near_reality.game.content.custom.GodBow
import com.zenyte.game.world.entity.player.Player

/**
 * Represents a recipe for crafting a crystal item.
 *
 * @author Stan van der Bend
 */
interface CrystalRecipe {

    companion object { val all by lazy { CrystalChargeable.all + CrystalCorrupted.all + GodBow.all + EnhancedCrystalKeyRecipe } }

    /**
     * Item id of the active variant.
     */
    val productItemId: Int

    /**
     * The number of required crystal shards.
     */
    val crystalShardCost: Int

    /**
     * The required crafting level to craft the item.
     */
    val requiredCrafting: Int

    /**
     * The required smithing level to craft the item.
     */
    val requiredSmithing: Int

    /**
     * The crafting experience gained by crafting the item.
     */
    val craftingExperience: Int

    /**
     * The smithing experience gained by crafting the item.
     */
    val smithingExperience: Int

    /**
     * Checks if the [player] has the materials required to craft this recipe.
     *
     * @return `true` if player has the required materials,
     *          `false` if not.
     */
    fun hasMaterials(player: Player): Boolean

    /**
     * Deletes the required materials to craft this recipe from the [player].
     *
     * @return `true` if successfully deleted the required materials,
     *          `false` if not.
     */
    fun deleteMaterials(player: Player): Boolean

    /**
     * Calculates how many of a specific recipe can be created, used exclusively by
     * [EnhancedCrystalKeyRecipe] creation
     */
    fun hasMaterialsForX(player: Player): Int
}
