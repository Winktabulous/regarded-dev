package com.near_reality.scripts.npc.drops.table.tables.gem

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.tables.rare.MegaRareDropTable
import com.near_reality.scripts.npc.drops.table.nothing
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*

object GemDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        UNCUT_SAPPHIRE quantity 1 rarity 32
        UNCUT_EMERALD quantity 1 rarity 16
        UNCUT_RUBY quantity 1 rarity 8
        chance(3) roll StandaloneDropTableBuilder {
            static {
                CHAOS_TALISMAN quantity 1 rarity 1
                NATURE_TALISMAN quantity 1 rarity 1
            }
        }
        UNCUT_DIAMOND quantity 1 rarity 2
        RUNE_JAVELIN quantity 5 rarity 1
        LOOP_HALF_OF_KEY quantity 1 rarity 1
        TOOTH_HALF_OF_KEY quantity 1 rarity 1
        chance(1) roll MegaRareDropTable
    }
})
