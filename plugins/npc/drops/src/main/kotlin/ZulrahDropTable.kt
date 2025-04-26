package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemChance
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*

class ZulrahDropTable : NPCDropTableScript() {
    init {
        npcs(ZULRAH, ZULRAH_2043, ZULRAH_2044)
        
        onDeath {
            rollStaticTableAndDropBelowPlayer(killer, type = Always)
            repeat(2) {
                rollStaticTableAndDropBelowPlayer(killer, type = Main)
                rollStaticTableAndDropBelowPlayer(killer, type = Unique)
            }
            rollStaticTableAndDropBelowPlayer(killer, type = Tertiary)
        }
        
        provideInfo<StaticRollItemChance> { table ->
            if (table != Always && table != Tertiary)
                "Zulrah rolls twice on the main drop table.<br>The rate above is for a single roll."
            else
                null
        }
        
        buildTable {
            Always {
                ZULRAHS_SCALES quantity 100..299 rarity always
            }
            Unique(1500) {
                chance(20) roll ZulrahUniques
            }
            Main(248) {
                // Weapons and Armour
                BATTLESTAFF quantity 10.noted rarity 10     // 10
                DRAGON_MED_HELM quantity 1 rarity 2         // 12
                DRAGON_HALBERD quantity 1 rarity 2          // 14
                // Runes
                DEATH_RUNE quantity 300 rarity 12           // 26
                LAW_RUNE quantity 200 rarity 12             // 38
                CHAOS_RUNE quantity 500 rarity 12           // 50
                // Herbs
                SNAPDRAGON quantity 10.noted rarity 2       // 52
                DWARF_WEED quantity 30.noted rarity 2       // 54
                TOADFLAX quantity 25.noted rarity 2         // 56
                TORSTOL quantity 10.noted rarity 2          // 58
                // Seeds
                PALM_TREE_SEED quantity 1 rarity 6          // 64
                PAPAYA_TREE_SEED quantity 3 rarity 6        // 70
                CALQUAT_TREE_SEED quantity 2 rarity 6       // 76
                MAGIC_SEED quantity 1 rarity 4              // 80
                TOADFLAX_SEED quantity 2 rarity 2           // 82
                SNAPDRAGON_SEED quantity 1 rarity 2         // 84
                DWARF_WEED_SEED quantity 2 rarity 2         // 86
                TORSTOL_SEED quantity 1 rarity 2            // 88
                SPIRIT_SEED quantity 1 rarity 1             // 89
                // Resources
                SNAKESKIN quantity 35.noted rarity 11       // 100
                ItemId.RUNITE_ORE quantity 2.noted rarity 11// 111
                PURE_ESSENCE quantity 1500.noted rarity 10  // 121
                chance(10) roll FlaxTable             // 131
                YEW_LOGS quantity 35.noted rarity 10        // 141
                ADAMANTITE_BAR quantity 20.noted rarity 8   // 149
                ItemId.COAL quantity 200.noted rarity 8     // 157
                DRAGON_BONES quantity 12.noted rarity 8     // 165
                MAHOGANY_LOGS quantity 50.noted rarity 8    // 173
                // Other
                ZULANDRA_TELEPORT quantity 4 rarity 15      // 188
                MANTA_RAY quantity 35.noted rarity 12       // 200
                ANTIDOTE4_5952 quantity 10.noted rarity 9   // 209
                DRAGONSTONE_BOLT_TIPS quantity 12 rarity 8  // 217
                GRAPES quantity 250.noted rarity 6          // 223
                COCONUT quantity 20.noted rarity 6          // 229
                SWAMP_TAR quantity 1000 rarity 5            // 234
                ZULRAHS_SCALES quantity 500 rarity 5        // 239
                // Rare
                chance(9) roll RareDropTable          // 248
            }
            Tertiary {
                JAR_OF_SWAMP quantity 1 oneIn 1500 announce everywhere
            }
        }
    }

    object ZulrahUniques : StandaloneDropTableBuilder({
        limit = 400
        static {
            TANZANITE_FANG quantity 1 rarity 100 announce everywhere
            MAGIC_FANG quantity 1 rarity 100 announce everywhere
            SERPENTINE_VISAGE quantity 1 rarity 100 announce everywhere
            UNCUT_ONYX quantity 1 rarity 100 announce everywhere
        }
    })

    object FlaxTable : StandaloneDropTableBuilder({
        limit = 2632
        static {
            FLAX quantity 1000.noted rarity 2500
            chance(132) roll Mutagens
        }
    })

    object Mutagens : StandaloneDropTableBuilder({
        limit = 200
        static {
            TANZANITE_MUTAGEN quantity 1 rarity 100 announce everywhere
            MAGMA_MUTAGEN quantity 1 rarity 100 announce everywhere
        }
    })
}