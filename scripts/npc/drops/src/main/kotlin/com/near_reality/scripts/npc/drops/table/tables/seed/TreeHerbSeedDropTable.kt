package com.near_reality.scripts.npc.drops.table.tables.seed

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ItemId

object TreeHerbSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        ItemId.RANARR_SEED quantity 1 rarity 30
        ItemId.SNAPDRAGON_SEED quantity 1 rarity 28
        ItemId.TORSTOL_SEED quantity 1 rarity 22
        ItemId.WATERMELON_SEED quantity 15 rarity 21
        ItemId.WILLOW_SEED quantity 1 rarity 20
        ItemId.MAHOGANY_SEED quantity 1 rarity 18
        ItemId.MAPLE_SEED quantity 1 rarity 18
        ItemId.TEAK_SEED quantity 1 rarity 18
        ItemId.YEW_SEED quantity 1 rarity 18
        ItemId.PAPAYA_TREE_SEED quantity 1 rarity 14
        ItemId.MAGIC_SEED quantity 1 rarity 11
        ItemId.PALM_TREE_SEED quantity 1 rarity 10
        ItemId.SPIRIT_SEED quantity 1 rarity 4
        ItemId.DRAGONFRUIT_TREE_SEED quantity 1 rarity 6
        ItemId.CELASTRUS_SEED quantity 1 rarity 4
        ItemId.REDWOOD_TREE_SEED quantity 1 rarity 4
    }
})
