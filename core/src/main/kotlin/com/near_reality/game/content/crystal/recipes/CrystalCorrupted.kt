package com.near_reality.game.content.crystal.recipes

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.game.content.crystal.CRYSTAL_SHARD_CHARGES_RATIO
import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult

/**
 * Represents a kind of crystal weapon that has the same effect as its degrade-able equivalent,
 * but do not cost charges and are non-trade-able.
 *
 * These variants may also be re-coloured.
 *
 * @author Stan van der Bend
 */
sealed class CrystalCorrupted : CrystalRecipe {

    companion object { val all by lazy { listOf(BowOfFaerdhinen, BladeOfSaeldor) } }

    override val requiredCrafting: Int = 82
    override val requiredSmithing: Int = 82
    override val craftingExperience: Int = 0
    override val smithingExperience: Int = 0
    abstract val weaponToCorrupt : CrystalWeapon

    override fun hasMaterials(player: Player): Boolean {
        findCrystalWeaponForRecipe(player) ?: return false
        val crystalShards = getShardCost(player)
        return crystalShards == 0 || player.inventory.containsItem(CRYSTAL_SHARD, crystalShards)
    }

    override fun deleteMaterials(player: Player): Boolean {
        val baseWeapon = findCrystalWeaponForRecipe(player)
            ?: error("Player does not have crystal weapon for recipe {$this}")
        val crystalShards = getShardCost(player)
        if (crystalShards > 0 && player.inventory.deleteItem(CRYSTAL_SHARD, crystalShards).result != RequestResult.SUCCESS)
            return false
        return player.inventory.deleteItem(baseWeapon).result == RequestResult.SUCCESS
    }

    /**
     * Get the total cost of shard items the [player] has to pay to craft this item,
     * the number of charges from the [weaponToCorrupt] are subtracted from the [crystalShardCost].
     */
    fun getShardCost(player: Player): Int {
        val baseWeapon = findCrystalWeaponForRecipe(player) ?: return crystalShardCost
        return crystalShardCost - (baseWeapon.charges.takeIf { it > 0 }
            ?.div(CRYSTAL_SHARD_CHARGES_RATIO)
            ?.coerceAtLeast(0)
            ?: 0)
    }

    /**
     * Find the item in the inventory of the [player] that represents the [weaponToCorrupt].
     */
    fun findCrystalWeaponForRecipe(player: Player) =
        player.inventory.container.items.values
            .filter { item -> item.id == weaponToCorrupt.productItemId || item.id == weaponToCorrupt.inactiveId }
            .maxByOrNull { it.charges }

    data object BowOfFaerdhinen : CrystalCorrupted() {

        override val productItemId: Int = ItemId.BOW_OF_FAERDHINEN_C
        override val crystalShardCost: Int = 2000
        override val weaponToCorrupt = CrystalWeapon.BowOfFaerdhinen
    }

    data object BladeOfSaeldor : CrystalCorrupted() {

        override val productItemId: Int = ItemId.BLADE_OF_SAELDOR_C
        override val crystalShardCost: Int = 1000
        override val weaponToCorrupt = CrystalWeapon.BladeOfSaeldor
    }

    override fun hasMaterialsForX(player: Player): Int {
        return if(hasMaterials(player))
            1
        else
            0
    }
}

