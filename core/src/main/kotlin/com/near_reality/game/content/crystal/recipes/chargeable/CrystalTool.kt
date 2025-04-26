package com.near_reality.game.content.crystal.recipes.chargeable

import com.near_reality.game.content.crystal.CrystalSeed
import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.near_reality.game.content.skills.mining.PickAxeDefinition
import com.near_reality.game.content.skills.woodcutting.AxeDefinition
import com.zenyte.game.content.skills.fishing.FishingTool
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.degradableitems.DegradeType
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.SkillConstants.*
import net.runelite.api.Skill

/**
 *  Represents a crystal item that can be used as a skilling tool for.
 *
 * @author Stan van der Bend
 */
sealed class CrystalTool(
    override val productItemId: Int,
    override val inactiveId: Int,
    val skillRequirement: Pair<Int, Int>,
    vararg val additionalMaterial: Item,
) : CrystalChargeable() {

    companion object { val all by lazy { listOf(Axe, Harpoon, Pickaxe) } }

    override val crystalShardCost: Int = 120
    override val materials: List<Item> = CrystalSeed.TOOL * 1 + additionalMaterial
    override val startCharges = 20_000
    override val maximumCharges = 20_000
    override val requiredCrafting = 76
    override val requiredSmithing = 76
    override val craftingExperience = 6000
    override val smithingExperience = 6000
    override val type: DegradeType = DegradeType.CUSTOM

    object Axe : CrystalTool(
        productItemId = ItemId.CRYSTAL_AXE,
        inactiveId = ItemId.CRYSTAL_AXE_INACTIVE,
        skillRequirement = WOODCUTTING to 71,
        Item(ItemId.DRAGON_AXE)
    ), AxeDefinition {
        override val cutTime: Int
            get() = if (Utils.random(0, 100) >= 45) 1 else 2
        override val levelRequired: Int = 71
        override val treeCutAnimation = Animation(8324)
        override val trunkCutAnimation = Animation(8326)
        override val canoeCutAnimation = Animation(8327)
    }

    /**
     * TODO: 35% increased catch rate compared to a regular harpoon and 12.5% increased compared to dragon harpoon
     * TODO: The Crystal harpoon also has a 1/3 chance of catching a Crystallised harpoonfish when fishing in Tempoross Cove.
     */
    object Harpoon : CrystalTool(
        productItemId = ItemId.CRYSTAL_HARPOON,
        inactiveId = ItemId.CRYSTAL_HARPOON_INACTIVE,
        skillRequirement = FISHING to 71,
        Item(ItemId.DRAGON_HARPOON)
    ) {
        fun `is`(tool: FishingTool.Tool) =
            productItemId == tool.id || inactiveId == tool.id
    }

    /**
     * TODO: Excluding waiting for ores to respawn or having to move to a new resource,
     *       the crystal pickaxe is effectively a 3.03% increase in overall mining speed compared to a dragon pickaxe
     *       or 9.09% compared to a rune pickaxe.
     * TODO: A charged crystal pickaxe provides a chance to mine an ore every three ticks
     *       (with an additional 1/4 chance to reduce this to two ticks);
     *       an uncharged crystal pickaxe has the same mining capabilities as a dragon pickaxe.
     */
    object Pickaxe : CrystalTool(
        productItemId = ItemId.CRYSTAL_PICKAXE,
        inactiveId = ItemId.CRYSTAL_PICKAXE_INACTIVE,
        skillRequirement = MINING to 71,
        Item(ItemId.DRAGON_PICKAXE)
    ), PickAxeDefinition {
        override val id: Int = productItemId
        override val level: Int = 71
        override val mineTime: Int
            get() = if (Utils.random(0, 100) >= 47) 1 else 2
        override val anim = Animation(8347)
        override val alternateAnimation = Animation(8345)
    }

    object CelestialSignet : CrystalTool(
        productItemId = ItemId.CELESTIAL_SIGNET_UNCHARGED,
        inactiveId = ItemId.CELESTIAL_RING_UNCHARGED,
        skillRequirement = MINING to 70,
        Item(ItemId.CELESTIAL_RING_UNCHARGED),
        Item(ItemId.STARDUST, 1_000),
    ) {
        override val crystalShardCost: Int = 100

        override val materials: List<Item> = additionalMaterial.toList()

        override val smithingExperience: Int = 5000

        override val craftingExperience: Int = 5000
    }
}
