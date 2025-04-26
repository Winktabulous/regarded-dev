package com.near_reality.game.content.dt2.drops

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.DropTableType.Standalone
import com.near_reality.scripts.npc.drops.table.DropTableType.Tertiary
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.ADAMANT_JAVELIN
import com.zenyte.game.item.ItemId.AWAKENERS_ORB
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.BRONZE_JAVELIN
import com.zenyte.game.item.ItemId.CHROMIUM_INGOT
import com.zenyte.game.item.ItemId.DRAGON_DART_TIP
import com.zenyte.game.item.ItemId.DRAGON_JAVELIN_HEADS
import com.zenyte.game.item.ItemId.EYE_OF_THE_DUKE
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.FROZEN_TABLET
import com.zenyte.game.item.ItemId.ICE_QUARTZ
import com.zenyte.game.item.ItemId.LAVA_BATTLESTAFF
import com.zenyte.game.item.ItemId.LAVA_RUNE
import com.zenyte.game.item.ItemId.MAGUS_ICON
import com.zenyte.game.item.ItemId.MAGUS_VESTIGE
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MITHRIL_JAVELIN
import com.zenyte.game.item.ItemId.ONYX_BOLTS_E
import com.zenyte.game.item.ItemId.PRAYER_POTION3
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.RUNE_FULL_HELM
import com.zenyte.game.item.ItemId.RUNE_HALBERD
import com.zenyte.game.item.ItemId.RUNE_JAVELIN_HEADS
import com.zenyte.game.item.ItemId.SCROLL_BOX_EASY
import com.zenyte.game.item.ItemId.SCROLL_BOX_ELITE
import com.zenyte.game.item.ItemId.SCROLL_BOX_HARD
import com.zenyte.game.item.ItemId.SCROLL_BOX_MEDIUM
import com.zenyte.game.item.ItemId.SOUL_RUNE
import com.zenyte.game.item.ItemId.SUPER_COMBAT_POTION2
import com.zenyte.game.item.ItemId.TUNA_POTATO
import com.zenyte.game.item.ItemId.UNCUT_DIAMOND
import com.zenyte.game.item.ItemId.UNCUT_RUBY
import com.zenyte.game.item.ItemId.VIRTUS_MASK
import com.zenyte.game.item.ItemId.VIRTUS_ROBE_LEGS
import com.zenyte.game.item.ItemId.VIRTUS_ROBE_TOP
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.npc.NpcId.DUKE_SUCELLUS_12195

class DukeAwakenedDropTable : NPCDropTableScript() {
    init {
        npcs(DUKE_SUCELLUS_12195)
        
        onDeath {
            val baseRate = 3
            killer.hasKilledDukeAwakened = true
        
            if (Utils.random(350) == 0) {
                npc.dropItemAtKiller(killer, Item(MAGUS_VESTIGE, 1))
            }
        
            if (Utils.random(99) < baseRate) {
                rollTable(killer, Standalone, DukeUniques.staticTable).forEach {
                    npc.dropItemAtKiller(killer, Item(it.id, 1))
                }
                rollStaticTableAndDrop(killer, Tertiary)
            }
            if (Utils.random(25) == 0) {
                npc.dropItemAtKiller(killer, Item(AWAKENERS_ORB))
            }
            if (Utils.random(7) == 0) {
                npc.dropItemAtKiller(killer, Item(FROZEN_TABLET))
                killer.hasReceivedFrozenTablet = true
            }
            if (Utils.random(50) == 0) {
                npc.dropItemAtKiller(killer, Item(ICE_QUARTZ))
                killer.hasReceivedIceQuartz = true
            }
            if (Utils.random(5) == 0) {
                npc.dropItemAtKiller(killer, Item(TUNA_POTATO, (3..4).random()))
                npc.dropItemAtKiller(killer, Item(PRAYER_POTION3, 1))
                npc.dropItemAtKiller(killer, Item(SUPER_COMBAT_POTION2, 1))
            } else {
                rollStaticTableAndDropBelowPlayer(killer, Main)
            }
        
        }
        
        appendDrop(DisplayedDrop(CHROMIUM_INGOT, 1, 1, 72.46))
        appendDrop(DisplayedDrop(MAGUS_VESTIGE, 1, 1, 350.00))
        appendDrop(DisplayedDrop(EYE_OF_THE_DUKE, 1, 1, 277.78))
        appendDrop(DisplayedDrop(MAGUS_ICON, 1, 1, 138.89))
        appendDrop(DisplayedDrop(VIRTUS_MASK, 1, 1, 555.56))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_TOP, 1, 1, 555.56))
        appendDrop(DisplayedDrop(VIRTUS_ROBE_LEGS, 1, 1, 555.56))
        
        appendDrop(DisplayedDrop(AWAKENERS_ORB, 1, 1, 25.00))
        appendDrop(DisplayedDrop(FROZEN_TABLET, 1, 1, 7.00))
        appendDrop(DisplayedDrop(ICE_QUARTZ, 1, 1, 50.00))
        appendDrop(DisplayedDrop(TUNA_POTATO, 3, 4, 5.00))
        put(TUNA_POTATO, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(PRAYER_POTION3, 1, 1, 5.00))
        put(PRAYER_POTION3, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        appendDrop(DisplayedDrop(SUPER_COMBAT_POTION2, 1, 1, 5.00))
        put(SUPER_COMBAT_POTION2, PredicatedDrop("This will drop alongside the other supplies listed at 20% DR"))
        
        
        buildTable(100) {
            Main {
                //50
                ItemId.COAL quantity 260.noted rarity 8
                ItemId.ADAMANTITE_ORE quantity 90.noted rarity 8
                RUNE_JAVELIN_HEADS quantity 36 rarity 8
                DRAGON_JAVELIN_HEADS quantity 36 rarity 8
                UNCUT_RUBY quantity 40.noted rarity 5
                UNCUT_DIAMOND quantity 40.noted rarity 5
                ItemId.RUNITE_ORE quantity 26.noted rarity 2
                DRAGON_DART_TIP quantity 175 rarity 2
                PURE_ESSENCE quantity 270.noted rarity 2
                ItemId.IRON_ORE quantity 75.noted rarity 2
        
                //20
                ItemId.SILVER_ORE quantity 75.noted rarity 1
                ItemId.MITHRIL_ORE quantity 68.noted rarity 1
                ItemId.SAPPHIRE quantity 26.noted rarity 2
                ItemId.EMERALD quantity 26.noted rarity 2
                ItemId.RUBY quantity 26.noted rarity 1
                ItemId.RAW_SHARK quantity 170.noted rarity 1
                RUNE_FULL_HELM quantity 2.noted rarity 4
                LAVA_BATTLESTAFF quantity 2.noted rarity 4
                RUNE_HALBERD quantity 2.noted rarity 4
        
                //30
                LAVA_RUNE quantity 350 rarity 8
                BLOOD_RUNE quantity 350 rarity 8
                SOUL_RUNE quantity 450 rarity 2
                BRONZE_JAVELIN quantity 62 rarity 2
                MITHRIL_JAVELIN quantity 62 rarity 2
                ADAMANT_JAVELIN quantity 62 rarity 2
                ONYX_BOLTS_E quantity 65 rarity 2
                MIND_RUNE quantity 220 rarity 2
                FIRE_RUNE quantity 220 rarity 2
            }
            Tertiary {
                SCROLL_BOX_EASY quantity 1 oneIn 60
                SCROLL_BOX_MEDIUM quantity 1 oneIn 60
                SCROLL_BOX_HARD quantity 1 oneIn 40
                SCROLL_BOX_ELITE quantity 1 oneIn 25
                ItemId.BARON quantity 1 oneIn 1000
            }
        }
        
    }

    /* 3/100 awakened */
    object DukeUniques : StandaloneDropTableBuilder({
        limit = 100
        static {
            /* 1/8 -> 1/200, 3/200 */
            CHROMIUM_INGOT quantity 1 rarity 46
            EYE_OF_THE_DUKE quantity 1 rarity 12
            MAGUS_ICON quantity 1 rarity 24
            VIRTUS_MASK quantity 1 rarity 6
            VIRTUS_ROBE_LEGS quantity 1 rarity 6
            VIRTUS_ROBE_TOP quantity 1 rarity 6
        }
    })
}