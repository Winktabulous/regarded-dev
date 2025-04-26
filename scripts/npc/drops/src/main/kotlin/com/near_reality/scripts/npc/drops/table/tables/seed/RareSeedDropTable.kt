package com.near_reality.scripts.npc.drops.table.tables.seed

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ItemId

object RareSeedDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        ItemId.TOADFLAX_SEED quantity 1 rarity 47
        ItemId.IRIT_SEED quantity 1 rarity 32
        ItemId.BELLADONNA_SEED quantity 1 rarity 31
        ItemId.AVANTOE_SEED quantity 1 rarity 22
        ItemId.POISON_IVY_SEED quantity 1 rarity 22
        ItemId.CACTUS_SEED quantity 1 rarity 21
        ItemId.KWUARM_SEED quantity 1 rarity 15
        ItemId.POTATO_CACTUS_SEED quantity 1 rarity 15
        ItemId.SNAPDRAGON_SEED quantity 1 rarity 10
        ItemId.CADANTINE_SEED quantity 1 rarity 7
        ItemId.LANTADYME_SEED quantity 1 rarity 5
        ItemId.SNAPE_GRASS_SEED quantity 3 rarity 4
        ItemId.DWARF_WEED_SEED quantity 1 rarity 3
        ItemId.TORSTOL_SEED quantity 1 rarity 2
    }
})
