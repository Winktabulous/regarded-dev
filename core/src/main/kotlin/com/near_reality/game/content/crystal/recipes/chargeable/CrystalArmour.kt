package com.near_reality.game.content.crystal.recipes.chargeable

import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.CrystalSeed
import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.degradableitems.DegradeType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * Represents a type of armour that can be created following the implemented [recipe][CrystalRecipe].
 *
 * @author Stan van der Bend
 */
sealed class CrystalArmour(
    val damageBonus: Double,
    val accuracyBonus: Double,
    override val productItemId: Int,
    override val inactiveId: Int,
    override val crystalShardCost: Int,
    override val materials: List<Item>,
    override val requiredCrafting: Int,
    override val requiredSmithing: Int,
    override val craftingExperience: Int,
    override val smithingExperience: Int,
) : CrystalChargeable() {

    companion object {

        private val bowItems = intArrayOf(
            ItemId.CRYSTAL_BOW_BASIC,
            ItemId.CRYSTAL_BOW_ATTUNED,
            ItemId.CRYSTAL_BOW_PERFECTED,

            ItemId.NEW_CRYSTAL_BOW_I,
            ItemId.CRYSTAL_BOW_FULL_I,
            ItemId.CRYSTAL_BOW_910_I,
            ItemId.CRYSTAL_BOW_810_I,
            ItemId.CRYSTAL_BOW_710_I,
            ItemId.CRYSTAL_BOW_610_I,
            ItemId.CRYSTAL_BOW_510_I,
            ItemId.CRYSTAL_BOW_410_I,
            ItemId.CRYSTAL_BOW_310_I,
            ItemId.CRYSTAL_BOW_210_I,
            ItemId.CRYSTAL_BOW_110_I,

            ItemId.NEW_CRYSTAL_BOW,
            ItemId.CRYSTAL_BOW_FULL,
            ItemId.CRYSTAL_BOW_910,
            ItemId.CRYSTAL_BOW_810,
            ItemId.CRYSTAL_BOW_710,
            ItemId.CRYSTAL_BOW_610,
            ItemId.CRYSTAL_BOW_510,
            ItemId.CRYSTAL_BOW_410,
            ItemId.CRYSTAL_BOW_310,
            ItemId.CRYSTAL_BOW_210,
            ItemId.CRYSTAL_BOW_110,

            ItemId.BOW_OF_FAERDHINEN_C,
            ItemId.BOW_OF_FAERDHINEN_C_25884,
            ItemId.BOW_OF_FAERDHINEN_C_25886,
            ItemId.BOW_OF_FAERDHINEN_C_25888,
            ItemId.BOW_OF_FAERDHINEN_C_25890,
            ItemId.BOW_OF_FAERDHINEN_C_25892,
            ItemId.BOW_OF_FAERDHINEN_C_25894,
            ItemId.BOW_OF_FAERDHINEN_C_25896,

            CustomItemId.ARMADYL_BOW,
            CustomItemId.ZAMORAK_BOW,
            CustomItemId.SARADOMIN_BOW,
            CustomItemId.BANDOS_BOW
        )

        val all by lazy { listOf(Helm, Body, Legs) }

        private fun appliesTo(player: Player) =
            CrystalWeapon.Bow.hasEquippedActive(player) ||
                    CrystalWeapon.BowOfFaerdhinen.hasEquippedActive(player) ||
                    player.equipment.containsAnyOf(EquipmentSlot.WEAPON.slot, bowItems)

        fun getTotalAccuracyBonus(player: Player) = if (appliesTo(player))
            all.filter { it.hasEquippedActive(player) }.sumOf { it.accuracyBonus }
        else
            0.0

        fun getTotalDamageBonus(player: Player) = if (appliesTo(player))
            all.filter { it.hasEquippedActive(player) }.sumOf { it.damageBonus }
        else
            0.0
    }

    override val startCharges = 20_000

    override val maximumCharges = 20_000

    override val type: DegradeType = DegradeType.CUSTOM

    object Helm : CrystalArmour(
        productItemId = ItemId.CRYSTAL_HELM,
        inactiveId = ItemId.CRYSTAL_HELM_INACTIVE,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object Legs : CrystalArmour(
        productItemId = ItemId.CRYSTAL_LEGS,
        inactiveId = ItemId.CRYSTAL_LEGS_INACTIVE,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object Body : CrystalArmour(
        productItemId = ItemId.CRYSTAL_BODY,
        inactiveId = ItemId.CRYSTAL_BODY_INACTIVE,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )
}
