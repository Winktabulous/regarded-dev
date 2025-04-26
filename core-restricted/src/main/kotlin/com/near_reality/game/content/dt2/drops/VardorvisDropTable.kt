package com.near_reality.game.content.dt2.drops

import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.vardorvis.Vardorvis
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemChance
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*

class VardorvisDropTable : NPCDropTableScript() {
    init {
        npcs(VARDORVIS)
        
        onDeath {
            val vardorvis = this.npc as Vardorvis
            val baseRate = when (vardorvis.difficulty) {
                DT2BossDifficulty.AWAKENED -> {
                    killer.hasKilledVardorvisAwakened = true
                    3
                }
        
                DT2BossDifficulty.NORMAL -> 1
                DT2BossDifficulty.QUEST -> 0
            }
            if (Utils.random(159) == 0) {
                rollTable(killer, Standalone, VardorvisClues.staticTable).forEach {
                    npc.dropItem(killer, Item(it.id, it.quantity.rollQuantity()))
                }
            }
            if (Utils.random(1999) == 0) {
                rollTable(killer, Standalone, VardorvisPet.staticTable).forEach {
                    npc.dropItem(killer, Item(it.id, it.quantity.rollQuantity()))
                }
            }
            if (baseRate > 0) {
                if (Utils.random(99) < baseRate) {
                    rollTable(killer, Standalone, VardorvisUniques.staticTable).forEach {
                        npc.dropItem(killer, Item(it.id, it.quantity.rollQuantity()))
                    }
                    rollStaticTableAndDrop(killer, Tertiary)
                } else if (Utils.random(74) == 0)
                    npc.dropItem(killer, Item(AWAKENERS_ORB))
                else if (Utils.random(24) == 0) {
                    npc.dropItem(killer, Item(STRANGLED_TABLET))
                    killer.hasReceivedStrangledTablet = true
                } else if (Utils.random(149) == 0) {
                    npc.dropItem(killer, Item(BLOOD_QUARTZ))
                    killer.hasReceivedBloodQuartz = true
                } else if (Utils.random(5) == 0) {
                    npc.dropItem(killer, Item(TUNA_POTATO, (3..4).random()))
                    npc.dropItem(killer, Item(PRAYER_POTION3, 1))
                    npc.dropItem(killer, Item(SUPER_COMBAT_POTION2, 1))
                }
                else
                    rollStaticTableAndDrop(killer, Main)
        
        
                if (Utils.random(50) == 0) {
                    npc.dropItem(killer, Item(RING_OF_SHADOWS))
                }
            }
        
        }
        
        appendDrop(DisplayedDrop(CHROMIUM_INGOT, 1, 1, 200.00))
        appendDrop(DisplayedDrop(EXECUTIONERS_AXE_HEAD, 1, 1, 533.33))
        appendDrop(DisplayedDrop(ULTOR_VESTIGE, 1, 1, 800.00))
        appendDrop(DisplayedDrop(VIRTUS_MASK, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_TOP, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_LEGS, 1, 1, 1600.00))
        
        appendDrop(DisplayedDrop(AWAKENERS_ORB, 1, 1, 75.00))
        appendDrop(DisplayedDrop(STRANGLED_TABLET, 1, 1, 25.00))
        appendDrop(DisplayedDrop(RING_OF_SHADOWS, 1, 1, 50.00))
        appendDrop(DisplayedDrop(BLOOD_QUARTZ, 1, 1, 150.00))
        appendDrop(DisplayedDrop(TUNA_POTATO, 3, 4, 5.00))
        put(TUNA_POTATO, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(PRAYER_POTION3, 1, 1, 5.00))
        put(PRAYER_POTION3, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(SUPER_COMBAT_POTION2, 1, 1, 5.00))
        put(SUPER_COMBAT_POTION2, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        
        appendDrop(DisplayedDrop(SCROLL_BOX_EASY, 1, 1, 160.00))
        appendDrop(DisplayedDrop(SCROLL_BOX_MEDIUM, 1, 1, 160.00))
        appendDrop(DisplayedDrop(SCROLL_BOX_HARD, 1, 1, 160.00))
        appendDrop(DisplayedDrop(SCROLL_BOX_ELITE, 1, 1, 160.00))
        appendDrop(DisplayedDrop(ItemId.BUTCH, 1, 1, 2000.00))
        
        
        provideInfo<StaticRollItemChance> { table ->
            if (table == Unique)
                "This rate is scaled down based on player contributions & DR boost"
            else if (rarity == 300)
                "The MVP has a 10% increased chance of getting this drop."
            else
                null
        }
        
        buildTable(100) {
            Main {
                ItemId.COAL quantity 130.noted rarity 8
                ItemId.ADAMANTITE_ORE quantity 45.noted rarity 8
                RUNE_JAVELIN_HEADS quantity 36 rarity 8
                DRAGON_JAVELIN_HEADS quantity 36 rarity 8
                UNCUT_RUBY quantity 25.noted rarity 5
                UNCUT_DIAMOND quantity 25.noted rarity 5
                ItemId.RUNITE_ORE quantity 18.noted rarity 2
                DRAGON_DART_TIP quantity 100 rarity 2
                PURE_ESSENCE quantity 120.noted rarity 1
                ItemId.IRON_ORE quantity 38.noted rarity 1
                ItemId.SILVER_ORE quantity 38.noted rarity 1
                ItemId.MITHRIL_ORE quantity 38.noted rarity 1
                SAPPHIRE quantity 17.noted rarity 1
                EMERALD quantity 17.noted rarity 1
                RUBY quantity 17.noted rarity 1
                RAW_SHARK quantity 200.noted rarity 1
        
                LAVA_RUNE quantity 200 rarity 8
                BLOOD_RUNE quantity 200 rarity 8
                SOUL_RUNE quantity 400 rarity 2
                BRONZE_JAVELIN quantity 42 rarity 1
                MITHRIL_JAVELIN quantity 42 rarity 1
                ADAMANT_JAVELIN quantity 42 rarity 1
                ONYX_BOLTS_E quantity 60 rarity 1
                MIND_RUNE quantity 120 rarity 1
                FIRE_RUNE quantity 120 rarity 1
            }
        }
        
    }

    /* 1/100 base || 3/100 awakened */
    object VardorvisUniques : StandaloneDropTableBuilder({
        limit = 16
        static {
            /* 1/2 -> 1/200, 3/200 */
            CHROMIUM_INGOT quantity 1 rarity 8
            EXECUTIONERS_AXE_HEAD quantity 1 rarity 3
            ULTOR_VESTIGE quantity 1 rarity 2
            VIRTUS_MASK quantity 1 rarity 1
            VIRTUS_ROBE_LEGS quantity 1 rarity 1
            VIRTUS_ROBE_TOP quantity 1 rarity 1
        }
    })

    object VardorvisClues : StandaloneDropTableBuilder({
        limit = 4
        static {
            SCROLL_BOX_EASY quantity 1 rarity 1
            SCROLL_BOX_MEDIUM quantity 1 rarity 1
            SCROLL_BOX_HARD quantity 1 rarity 1
            SCROLL_BOX_ELITE quantity 1 rarity 1
        }
    })

    object VardorvisPet : StandaloneDropTableBuilder({
        limit = 1
        static {
            ItemId.BUTCH quantity 1 rarity 1
        }
    })
}