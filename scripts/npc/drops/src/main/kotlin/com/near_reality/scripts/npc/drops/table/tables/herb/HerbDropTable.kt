package com.near_reality.scripts.npc.drops.table.tables.herb

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ItemId.*

object HerbDropTable : StandaloneDropTableBuilder({
    static {
        limit = 128
        GRIMY_GUAM_LEAF quantity 1 rarity 32
        GRIMY_MARRENTILL quantity 1 rarity 24
        GRIMY_TARROMIN quantity 1 rarity 18
        GRIMY_HARRALANDER quantity 1 rarity 14
        GRIMY_RANARR_WEED quantity 1 rarity 11
        GRIMY_IRIT_LEAF quantity 1 rarity 8
        GRIMY_AVANTOE quantity 1 rarity 6
        GRIMY_KWUARM quantity 1 rarity 5
        GRIMY_CADANTINE quantity 1 rarity 4
        GRIMY_LANTADYME quantity 1 rarity 3
        GRIMY_DWARF_WEED quantity 1 rarity 3
    }
})
