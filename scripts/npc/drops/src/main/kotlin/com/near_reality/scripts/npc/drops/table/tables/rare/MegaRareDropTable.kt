package com.near_reality.scripts.npc.drops.table.tables.rare

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.nothing
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*

object MegaRareDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        RUNE_SPEAR quantity 1 rarity 8
        SHIELD_LEFT_HALF quantity 1 rarity 4
        DRAGON_SPEAR quantity 1 rarity 3
    }
})
