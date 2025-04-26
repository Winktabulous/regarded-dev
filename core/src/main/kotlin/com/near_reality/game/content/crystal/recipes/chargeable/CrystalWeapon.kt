package com.near_reality.game.content.crystal.recipes.chargeable

import com.near_reality.game.content.crystal.CrystalSeed
import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.degradableitems.DegradeType

/**
 * Represents a type of crystal item that is intended for use in combat.
 *
 * @author Stan van der Bend
 */
sealed class CrystalWeapon(
    override val productItemId: Int,
    override val inactiveId: Int,
    override val startCharges: Int,
    override val maximumCharges: Int,
    override val crystalShardCost: Int,
    override val materials: List<Item>,
    override val requiredCrafting: Int,
    override val requiredSmithing: Int,
    override val craftingExperience: Int,
    override val smithingExperience: Int
) : CrystalChargeable() {

    companion object {
        val all by lazy { listOf( BowOfFaerdhinen, BladeOfSaeldor, Halberd, Shield, Bow) }
    }

    override val type: DegradeType = DegradeType.CUSTOM

    object BladeOfSaeldor : CrystalWeapon(
        productItemId = ItemId.BLADE_OF_SAELDOR,
        inactiveId = ItemId.BLADE_OF_SAELDOR_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 100,
        materials = CrystalSeed.ENHANCED_WEAPON * 1,
        requiredCrafting = 80,
        requiredSmithing = 80,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object BowOfFaerdhinen : CrystalWeapon(
        productItemId = ItemId.BOW_OF_FAERDHINEN,
        inactiveId = ItemId.BOW_OF_FAERDHINEN_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 100,
        materials = CrystalSeed.ENHANCED_WEAPON * 1,
        requiredCrafting = 80,
        requiredSmithing = 80,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object Bow : CrystalWeapon(
        productItemId = ItemId.CRYSTAL_BOW,
        inactiveId = ItemId.CRYSTAL_BOW_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 40,
        materials = CrystalSeed.WEAPON * 1,
        requiredCrafting = 78,
        requiredSmithing = 78,
        craftingExperience = 2000,
        smithingExperience = 2000
    )

    object Halberd : CrystalWeapon(
        productItemId = ItemId.CRYSTAL_HALBERD,
        inactiveId = ItemId.CRYSTAL_HALBERD_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 40,
        materials = CrystalSeed.WEAPON * 1,
        requiredCrafting = 78,
        requiredSmithing = 78,
        craftingExperience = 2000,
        smithingExperience = 2000
    )

    object Shield : CrystalWeapon(
        productItemId = ItemId.CRYSTAL_SHIELD,
        inactiveId = ItemId.CRYSTAL_SHIELD_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 40,
        materials = CrystalSeed.WEAPON * 1,
        requiredCrafting = 78,
        requiredSmithing = 78,
        craftingExperience = 2000,
        smithingExperience = 2000
    )
}
