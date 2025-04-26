package com.near_reality.scripts.npc.drops.table.tables.misc

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.game.item.ItemId

object TalismanDropTable : StandaloneDropTableBuilder({
    limit = 70
    static {
        ItemId.AIR_TALISMAN quantity 1 rarity 10
        ItemId.BODY_TALISMAN quantity 1 rarity 10
        ItemId.EARTH_TALISMAN quantity 1 rarity 10
        ItemId.FIRE_TALISMAN quantity 1 rarity 10
        ItemId.MIND_TALISMAN quantity 1 rarity 10
        ItemId.WATER_TALISMAN quantity 1 rarity 10
        ItemId.COSMIC_TALISMAN quantity 1 rarity 4
        ItemId.CHAOS_TALISMAN quantity 1 rarity 3
        ItemId.NATURE_TALISMAN quantity 1 rarity 3
    }
})
