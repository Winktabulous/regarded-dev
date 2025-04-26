package com.near_reality.scripts.npc.drops.table.tables.seed

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ItemId

object AllotmentSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        ItemId.POTATO_SEED quantity (1..4) rarity 64
        ItemId.ONION_SEED quantity (1..3) rarity 32
        ItemId.CABBAGE_SEED quantity (1..3) rarity 16
        ItemId.TOMATO_SEED quantity (1..2) rarity 8
        ItemId.SWEETCORN_SEED quantity (1..2) rarity 4
        ItemId.STRAWBERRY_SEED quantity 1 rarity 2
        ItemId.WATERMELON_SEED quantity 1 rarity 1
        ItemId.SNAPE_GRASS_SEED quantity 1 rarity 1
    }
})

