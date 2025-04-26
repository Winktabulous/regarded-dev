package com.near_reality.game.content.gauntlet.rewards

import com.near_reality.game.content.crystal.CRYSTAL_SHARD
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.item.ItemId.*

private const val common = 24

val crystallineRewardsMain = StandaloneDropTableBuilder {
    static {
        CRYSTAL_SHARD quantity 3..7 rarity always

        RUNE_FULL_HELM quantity (2..4).noted rarity common
        RUNE_CHAINBODY quantity (1..2).noted rarity common
        RUNE_PLATEBODY quantity (1..2).noted rarity common
        RUNE_PLATELEGS quantity (1..2).noted rarity common
        RUNE_PLATESKIRT quantity (1..2).noted rarity common
        RUNE_HALBERD quantity (1..2).noted rarity common
        RUNE_PICKAXE quantity (1..2).noted rarity common
        DRAGON_HALBERD quantity 1.noted rarity common

        COSMIC_RUNE quantity 160..240 rarity common
        NATURE_RUNE quantity 100..140 rarity common
        LAW_RUNE quantity 80..140 rarity common
        CHAOS_RUNE quantity 180..300 rarity common
        DEATH_RUNE quantity 100..160 rarity common
        BLOOD_RUNE quantity 80..140 rarity common
        MITHRIL_ARROW quantity 800..1200 rarity common
        ADAMANT_ARROW quantity 400..600 rarity common
        RUNE_ARROW quantity 200..300 rarity common
        DRAGON_ARROW quantity 30..85 rarity common

        UNCUT_SAPPHIRE quantity (25..60).noted rarity common
        UNCUT_EMERALD quantity (10..50).noted rarity common
        UNCUT_RUBY quantity (5..30).noted rarity common
        UNCUT_DIAMOND quantity (3..7).noted rarity common

        COINS_995 quantity 20_000..80_000 rarity common
        BATTLESTAFF quantity (4..8).noted rarity common
    }
}

private const val near_reality_mod = 0.7

val crystallineRewardsTertiary = StandaloneDropTableBuilder {
    static {
        SCROLL_BOX_ELITE quantity 1 oneIn 25
        CRYSTAL_SEED quantity 1 oneIn (120 * near_reality_mod).toInt()
        CRYSTAL_ARMOUR_SEED quantity 1 oneIn (120 * near_reality_mod).toInt()
        ENHANCED_CRYSTAL_WEAPON_SEED quantity 1 oneIn (2_000 * near_reality_mod).toInt() announce everywhere
        YOUNGLLEF quantity 1 oneIn (2_000 * near_reality_mod).toInt() announce everywhere
    }
}
