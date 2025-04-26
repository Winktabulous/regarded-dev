package com.near_reality.game.content.gauntlet.rewards

import com.near_reality.scripts.npc.drops.table.DropTableType
import com.near_reality.scripts.npc.drops.table.chance.RollAlways
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.content.boons.impl.CrystalCatalyst
import com.zenyte.game.content.xamphur.XamphurBoost
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player

sealed class GauntletRewards(
    private val mainTableRolls: Int,
    private val main: StandaloneDropTableBuilder,
    private val tertiary: StandaloneDropTableBuilder,
    private val noPrepMode: Boolean
) {
    object Crystalline : GauntletRewards(2, crystallineRewardsMain, crystallineRewardsTertiary, false)
    object Corrupted : GauntletRewards(3, corruptedRewardsMain, corruptedRewardsTertiary, false)
    object CrystallineNoPrep : GauntletRewards(2, crystallineRewardsMain, crystallineRewardsTertiary, true)
    object CorruptedNoPrep : GauntletRewards(3, corruptedRewardsMain, corruptedRewardsTertiary, true)

    private fun getQuantityModifier(): Double {
        return if (World.hasBoost(XamphurBoost.BONUS_GAUNTLET) && !noPrepMode)
            3.0
        else if (World.hasBoost(XamphurBoost.BONUS_GAUNTLET) && noPrepMode)
            2.0
        else if(noPrepMode)
            1.0
        else
            1.5
    }

    private fun getTertiaryQuantityMod(chance: RollItemChance): Int {
        return if (World.hasBoost(XamphurBoost.BONUS_GAUNTLET) && chance.id != YOUNGLLEF) 2 else 1
    }

    fun rollCompleted(player: Player, rateModifier: Double = 1.0): List<Item> {
        val bonusRolls: Int = if (player.boonManager.hasBoon(CrystalCatalyst::class.java)) 1 else 0
        val totalItems = mutableListOf<Item>()
        main.staticTable.run {
            totalItems += roll(player).mapNotNull { it.rollItem(player, getQuantityModifier()) }
            repeat(mainTableRolls + bonusRolls - 1) {
                totalItems += roll(player)
                    .filterNot { it is RollAlways }
                    .mapNotNull { it.rollItem(player, getQuantityModifier()) }
            }
        }
        totalItems += tertiary.staticTable.roll(
            player,
            DropTableType.Main,
            staticRollChanceRarityTransformer = { (it.rarity * rateModifier).toInt() })
            .mapNotNull { it.rollItem(player, getTertiaryQuantityMod(it)) }
        return totalItems
    }

    fun rollMinimalDamageRewardSim(player: Player) =
            minimalDamageTable.rollItems(player)


    fun rollMinimalDamageReward(player: Player): List<Item> {
        if (!noPrepMode)
            return minimalDamageTable.rollItems(player)
        return emptyList()
    }
    fun rollBossPartiallyDamagedRewardSim(player: Player) =
            bossPartiallyDamagedTable.rollItems(player)


    fun rollBossPartiallyDamagedReward(player: Player): List<Item> {
        if(!noPrepMode)
            return bossPartiallyDamagedTable.rollItems(player)
        return emptyList()
    }

    private fun StandaloneDropTableBuilder.rollItems(player: Player) =
        staticTable.roll(player, DropTableType.Main, staticRollChanceRarityTransformer = { 1 }).mapNotNull { it.rollItem(player) }

    private val minimalDamageTable = StandaloneDropTableBuilder {
        val common = 20
        limit = common * 3 // 3 items
        static {
            FLYER quantity 1 rarity common
            POTION quantity 1 rarity common
            ROTTEN_TOMATO quantity 1 rarity common
        }
    }

    private val bossPartiallyDamagedTable = StandaloneDropTableBuilder {
        val uncommon = 10
        limit = uncommon * 26 // 26 items
        static {
            ADAMANT_DAGGER quantity 1 rarity uncommon
            ADAMANT_FULL_HELM quantity 1 rarity uncommon
            ADAMANT_MACE quantity (2..3).noted rarity uncommon
            ADAMANT_PICKAXE quantity 1 rarity uncommon
            ADAMANT_PLATEBODY quantity 1 rarity uncommon
            ADAMANT_PLATESKIRT quantity 1 rarity uncommon
            ADAMANT_SCIMITAR quantity 1 rarity uncommon
            MAPLE_LONGBOW quantity (7..13).noted rarity uncommon
            MAPLE_SHORTBOW quantity (8..11).noted rarity uncommon
            MITHRIL_FULL_HELM quantity 1 rarity uncommon
            MITHRIL_MACE quantity (2..5).noted rarity uncommon
            MITHRIL_PLATEBODY quantity 1 rarity uncommon
            MITHRIL_PLATELEGS quantity 1 rarity uncommon
            MITHRIL_PLATESKIRT quantity 1 rarity uncommon
            AIR_RUNE quantity 200..300 rarity uncommon
            BODY_RUNE quantity 250..350 rarity uncommon
            EARTH_RUNE quantity 200..300 rarity uncommon
            FIRE_RUNE quantity 200..300 rarity uncommon
            MIND_RUNE quantity 300..400 rarity uncommon
            WATER_RUNE quantity 200..300 rarity uncommon
            CAKE quantity (10..20).noted rarity uncommon
            COD quantity (75..125).noted rarity uncommon
            TROUT quantity (50..100).noted rarity uncommon
            EYE_OF_NEWT quantity (300..500).noted rarity uncommon
            SILVER_BAR quantity (15..30).noted rarity uncommon
            UNCUT_SAPPHIRE quantity (1..3).noted rarity uncommon
        }
    }
}
