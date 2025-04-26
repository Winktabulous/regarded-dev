package com.near_reality.game.content.gauntlet.rewards

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.game.content.gauntlet.gauntletHasCape
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.never
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.item.ItemId.*

private const val common = 24

val corruptedRewardsMain = StandaloneDropTableBuilder {
    static {
        CRYSTAL_SHARD quantity 5..9 rarity always
        GAUNTLET_CAPE quantity 1 dynamicRarity {
            if (gauntletHasCape) {
                gauntletHasCape =true
                always
            } else
                never
        } info {
            "Awarded only if you do not already have a Gauntlet cape"
        }

        RUNE_FULL_HELM quantity (3..5).noted rarity common
        RUNE_CHAINBODY quantity (2..3).noted rarity common
        RUNE_PLATEBODY quantity 2.noted rarity common
        RUNE_PLATELEGS quantity (2..3).noted rarity common
        RUNE_PLATESKIRT quantity (2..3).noted rarity common
        RUNE_HALBERD quantity (2..3).noted rarity common
        RUNE_PICKAXE quantity (2..3).noted rarity common
        DRAGON_HALBERD quantity (1..2).noted rarity (2 * common)

        COSMIC_RUNE quantity 175..250 rarity common
        NATURE_RUNE quantity 125..150 rarity common
        LAW_RUNE quantity 100..150 rarity common
        CHAOS_RUNE quantity 200..350 rarity common
        DEATH_RUNE quantity 125..175 rarity common
        BLOOD_RUNE quantity 100..150 rarity common
        MITHRIL_ARROW quantity 1000..1500 rarity common
        ADAMANT_ARROW quantity 500..725 rarity common
        RUNE_ARROW quantity 250..450 rarity common
        DRAGON_ARROW quantity 50..100 rarity common

        UNCUT_SAPPHIRE quantity (25..65).noted rarity common
        UNCUT_EMERALD quantity (15..60).noted rarity common
        UNCUT_RUBY quantity (10..40).noted rarity common
        UNCUT_DIAMOND quantity (5..15).noted rarity common

        COINS_995 quantity 75_000..150_000 rarity common
        BATTLESTAFF quantity (8..12).noted rarity common
    }
}

val corruptedRewardsTertiary = StandaloneDropTableBuilder {
    static {
        SCROLL_BOX_ELITE quantity 1 oneIn 20
        CRYSTAL_SEED quantity 1 oneIn 25
        CRYSTAL_ARMOUR_SEED quantity 1 oneIn 25
        ENHANCED_CRYSTAL_WEAPON_SEED quantity 1 oneIn 100 announce everywhere
        YOUNGLLEF quantity 1 oneIn 500 announce everywhere
    }
}
