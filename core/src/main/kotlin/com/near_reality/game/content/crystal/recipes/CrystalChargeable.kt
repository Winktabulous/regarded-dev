package com.near_reality.game.content.crystal.recipes

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalArmour
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalDegradeable
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import java.util.function.Function

/**
 * Represents a type of crystal item that can be worn by a player.
 *
 * @author Stan van der Bend
 */
abstract class CrystalChargeable : CrystalRecipe, CrystalDegradeable {

    companion object { val all by lazy { CrystalArmour.all + CrystalWeapon.all + CrystalTool.all } }

    /**
     * A list of additionally required materials.
     */
    abstract val materials: List<Item>

    override val itemId: Int
        get() = productItemId

    override val nextId: Int
        get() = inactiveId

    override val minimumCharges: Int
        get() = 0

    override val function: Function<Item, Array<Item>>?
        get() = null

    override fun hasMaterials(player: Player): Boolean =
        player.inventory.containsItems(ingredients())

    override fun deleteMaterials(player: Player): Boolean =
        player.inventory.deleteItems(*ingredients().toTypedArray()).result == RequestResult.SUCCESS

    fun hasEquippedActive(player: Player) =
        player.equipment.containsItem(productItemId)

    fun `is`(itemId: Int) = itemId == productItemId || itemId == inactiveId

    private fun CrystalRecipe.ingredients(): List<Item> {
        if (this is CrystalTool.CelestialSignet) {
            println(materials)
        }
        return materials + Item(CRYSTAL_SHARD, crystalShardCost)
    }

    override fun hasMaterialsForX(player: Player): Int {
        return if(hasMaterials(player))
            1
        else
            0
    }
}
