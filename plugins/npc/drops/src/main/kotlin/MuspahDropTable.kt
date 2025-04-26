package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemChance
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.near_reality.scripts.npc.drops.table.tables.herb.HerbDropTable
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

class MuspahDropTable : NPCDropTableScript() {
    init {
        npcs(PHANTOM_MUSPAH, PHANTOM_MUSPAH_12078, PHANTOM_MUSPAH_12079, PHANTOM_MUSPAH_12080)
        
        onDeath {
            rollStaticTableAndDrop(killer, type = Always)
            val random = Utils.random(9)
            if(random < 2) {
                when(random) {
                    0 -> {
                        npc.dropItem(killer, Item(ItemId.SHARK, (4..6).random()).toNote())
                        npc.dropItem(killer, Item(ANCIENT_BREW_3, (1..2).random()).toNote())
                        npc.dropItem(killer, Item(SUPER_RESTORE3, (2..3).random()).toNote())
                    }
                    1 -> {
                        npc.dropItem(killer, Item(SUMMER_PIE, (4..6).random()).toNote())
                        npc.dropItem(killer, Item(RANGING_POTION3, (1..2).random()).toNote())
                        npc.dropItem(killer, Item(PRAYER_POTION3, (2..3).random()).toNote())
                    }
                }
            }
            val mainRolls = getStaticTable(Main).staticRolls
                .filterIsInstance<StaticRollItemChance>()
                .shuffled()
                .take(2)
                .map { it.rollItem() }
            mainRolls.forEach { npc.dropItem(killer, it) }
            rollStaticTableAndDrop(killer, type = Tertiary)
        }
        
        buildTable(220) {
            Always {
                ANCIENT_ESSENCE quantity 540..2060 rarity always
            }
            Main {
                RUNE_KITESHIELD quantity 3.noted rarity 10
                DRAGON_PLATESKIRT quantity 1 rarity 5
                RUNE_PLATELEGS quantity 3.noted rarity 10
                BLACK_DHIDE_BODY quantity 1 rarity 5
                DRAGON_PLATELEGS quantity 2 rarity 4
                RUNE_SWORD quantity 1 rarity 1
        
                LAW_RUNE quantity 146 rarity 10
                SOUL_RUNE quantity 466 rarity 10
                DEATH_RUNE quantity 428 rarity 10
                SMOKE_RUNE quantity 314 rarity 10
                CHAOS_RUNE quantity 480 rarity 5
                FIRE_RUNE quantity 1964 rarity 5
                CANNONBALL quantity 666 rarity 5
        
                GRIMY_TOADFLAX quantity 55.noted rarity 3
                GRIMY_KWUARM quantity 6.noted rarity 2
                GRIMY_CADANTINE quantity 6.noted rarity 2
                GRIMY_DWARF_WEED quantity 6.noted rarity 2
                GRIMY_LANTADYME quantity 6.noted rarity 1
        
                chance(5) roll HerbDropTable
                chance(5) roll GemDropTable
        
                ItemId.ADAMANTITE_ORE quantity 22.noted rarity 10
                ItemId.GOLD_ORE quantity 180.noted rarity 10
                TEAK_PLANK quantity 22.noted rarity 10
                MOLTEN_GLASS quantity 89.noted rarity 10
                PURE_ESSENCE quantity 2314.noted rarity 5
                ItemId.COAL quantity 163.noted rarity 5
                ItemId.RUNITE_ORE quantity 18.noted rarity 3
                ItemId.SILVER_ORE quantity 101.noted rarity 2
        
                MANTA_RAY quantity 28.noted rarity 10
                WATER_ORB quantity 21.noted rarity 10
                DRAGON_BOLTS_UNF quantity 89 rarity 10
                LIMPWURT_ROOT quantity 21.noted rarity 3
            }
            Tertiary {
                FROZEN_CACHE quantity 1 oneIn 25
                ANCIENT_ICON quantity 1 oneIn 50
                VENATOR_SHARD quantity 1 oneIn 80
                SCROLL_BOX_HARD quantity 1 oneIn 40
                SCROLL_BOX_ELITE quantity 1 oneIn 50
            }
        }
    }
}