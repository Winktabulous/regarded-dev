package com.near_reality.game.content.dt2.drops

import com.near_reality.game.content.dt2.npc.theduke.DukeSucellusEntity
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

class DukeNormalDropTable : NPCDropTableScript() {
    init {
        npcs(DUKE_SUCELLUS_12191)
        
        onDeath {
            val duke = this.npc as DukeSucellusEntity
            val baseRate = 1
        
            if(baseRate > 0) {
                if(Utils.random(99) < baseRate) {
                    rollTable(killer, Standalone, DukeUniques.staticTable).forEach {
                        npc.dropItemAtKiller(killer, Item(it.id, 1))
                    }
                    rollStaticTableAndDrop(killer, Tertiary)
                } else if(Utils.random(85) == 0) {
                    npc.dropItemAtKiller(killer, Item(AWAKENERS_ORB))
                } else if(Utils.random(24) == 0) {
                    npc.dropItemAtKiller(killer, Item(FROZEN_TABLET))
                    killer.hasReceivedFrozenTablet = true
                } else if(Utils.random(149) == 0) {
                    npc.dropItemAtKiller(killer, Item(ICE_QUARTZ))
                    killer.hasReceivedIceQuartz = true
                } else if(Utils.random(5) == 0) {
                    npc.dropItemAtKiller(killer, Item(TUNA_POTATO, (3..4).random()))
                    npc.dropItemAtKiller(killer, Item(PRAYER_POTION3, 1))
                    npc.dropItemAtKiller(killer, Item(SUPER_COMBAT_POTION2, 1))
                } else {
                    rollStaticTableAndDropBelowPlayer(killer, Main)
                }
            }
        }
        
        appendDrop(DisplayedDrop(CHROMIUM_INGOT, 1, 1, 200.00))
        appendDrop(DisplayedDrop(EYE_OF_THE_DUKE, 1, 1, 533.33))
        appendDrop(DisplayedDrop(MAGUS_ICON, 1, 1, 800.00))
        appendDrop(DisplayedDrop(VIRTUS_MASK, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_TOP, 1, 1, 1600.00))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_LEGS, 1, 1, 1600.00))
        
        appendDrop(DisplayedDrop(AWAKENERS_ORB, 1, 1, 75.00))
        appendDrop(DisplayedDrop(FROZEN_TABLET, 1, 1, 25.00))
        appendDrop(DisplayedDrop(ICE_QUARTZ, 1, 1, 150.00))
        appendDrop(DisplayedDrop(TUNA_POTATO, 3, 4, 5.00))
        put(TUNA_POTATO, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(PRAYER_POTION3, 1, 1, 5.00))
        put(PRAYER_POTION3, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(SUPER_COMBAT_POTION2, 1, 1, 5.00))
        put(SUPER_COMBAT_POTION2, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        
        
        buildTable(100) {
            Main {
                //50
                ItemId.COAL quantity 130.noted rarity 8
                ItemId.ADAMANTITE_ORE quantity 45.noted rarity 8
                RUNE_JAVELIN_HEADS quantity 24 rarity 8
                DRAGON_JAVELIN_HEADS quantity 24 rarity 8
                UNCUT_RUBY quantity 25.noted rarity 5
                UNCUT_DIAMOND quantity 25.noted rarity 5
                ItemId.RUNITE_ORE quantity 18.noted rarity 2
                DRAGON_DART_TIP quantity 100 rarity 2
                PURE_ESSENCE quantity 120.noted rarity 2
                ItemId.IRON_ORE quantity 38.noted rarity 2
        
                //20
                ItemId.SILVER_ORE quantity 38.noted rarity 1
                ItemId.MITHRIL_ORE quantity 38.noted rarity 1
                ItemId.SAPPHIRE quantity 17.noted rarity 2
                ItemId.EMERALD quantity 17.noted rarity 2
                ItemId.RUBY quantity 17.noted rarity 1
                ItemId.RAW_SHARK quantity 120.noted rarity 1
                RUNE_FULL_HELM quantity 1 rarity 4
                LAVA_BATTLESTAFF quantity 1 rarity 4
                RUNE_HALBERD quantity 1 rarity 4
        
                //30
                LAVA_RUNE quantity 200 rarity 8
                BLOOD_RUNE quantity 200 rarity 8
                SOUL_RUNE quantity 400 rarity 2
                BRONZE_JAVELIN quantity 42 rarity 2
                MITHRIL_JAVELIN quantity 42 rarity 2
                ADAMANT_JAVELIN quantity 42 rarity 2
                ONYX_BOLTS_E quantity 35 rarity 2
                MIND_RUNE quantity 120 rarity 2
                FIRE_RUNE quantity 120 rarity 2
            }
            Tertiary {
                SCROLL_BOX_EASY quantity 1 oneIn 160
                SCROLL_BOX_MEDIUM quantity 1 oneIn 160
                SCROLL_BOX_HARD quantity 1 oneIn 160
                SCROLL_BOX_ELITE quantity 1 oneIn 160
                ItemId.BARON quantity 1 oneIn 2000
            }
        }
        
    }

    /* 1/100 base || 3/100 awakened */
    object DukeUniques : StandaloneDropTableBuilder({
        limit = 16
        static {
            /* 1/2 -> 1/200, 3/200 */
            CHROMIUM_INGOT quantity 1 rarity 8
            EYE_OF_THE_DUKE quantity 1 rarity 3
            MAGUS_ICON quantity 1 rarity 2
            VIRTUS_MASK quantity 1 rarity 1
            VIRTUS_ROBE_LEGS quantity 1 rarity 1
            VIRTUS_ROBE_TOP quantity 1 rarity 1
        }
    })
}