package com.near_reality.scripts.npc.drops.table.tables.rare

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.zenyte.game.item.ItemId

object RareDropTable : StandaloneDropTableBuilder({
    limit = 128
    static {
        // Runes and ammunition
        ItemId.NATURE_RUNE quantity 67 rarity 3
        ItemId.ADAMANT_JAVELIN quantity 20 rarity 2
        ItemId.DEATH_RUNE quantity 45 rarity 2
        ItemId.LAW_RUNE quantity 45 rarity 2
        ItemId.RUNE_ARROW quantity 42 rarity 2
        ItemId.STEEL_ARROW quantity 150 rarity 2
        // Weapons and armour
        ItemId.RUNE_2H_SWORD quantity 1 rarity 3
        ItemId.RUNE_BATTLEAXE quantity 1 rarity 3
        ItemId.RUNE_SQ_SHIELD quantity 1 rarity 2
        ItemId.DRAGON_MED_HELM quantity 1 rarity 1
        ItemId.RUNE_KITESHIELD quantity 1 rarity 1
        // Other
        ItemId.COINS_995 quantity 3000 rarity 21
        ItemId.LOOP_HALF_OF_KEY quantity 1 rarity 20
        ItemId.TOOTH_HALF_OF_KEY quantity 1 rarity 20
        ItemId.RUNITE_BAR quantity 1 rarity 5
        ItemId.DRAGONSTONE quantity 1 rarity 2
        ItemId.SILVER_ORE quantity 100.noted rarity 2
        // Sub-tables
        chance(20) roll GemDropTable
        chance(15) roll MegaRareDropTable
    }
})
