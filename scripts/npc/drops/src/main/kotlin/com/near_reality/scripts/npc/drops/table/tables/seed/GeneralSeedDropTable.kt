package com.near_reality.scripts.npc.drops.table.tables.seed

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ItemId

object GeneralSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        ItemId.POTATO_SEED quantity 4 rarity 368
        ItemId.ONION_SEED quantity 4 rarity 276
        ItemId.CABBAGE_SEED quantity 4 rarity 184
        ItemId.TOMATO_SEED quantity 3 rarity 92
        ItemId.SWEETCORN_SEED quantity 3 rarity 46
        ItemId.STRAWBERRY_SEED quantity 2 rarity 23
        ItemId.WATERMELON_SEED quantity 2 rarity 11
        ItemId.SNAPE_GRASS_SEED quantity 2 rarity 8
    }
})
