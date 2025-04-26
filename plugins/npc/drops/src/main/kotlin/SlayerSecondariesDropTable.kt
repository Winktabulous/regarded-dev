package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.content.skills.slayer.BossTask
import com.zenyte.game.content.skills.slayer.RegularTask
import com.zenyte.game.util.Colour
import mgi.types.config.npcs.NPCDefinitions

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*

class SlayerSecondariesDropTable : NPCDropTableScript() {
    init {
        val npcNames = (RegularTask.VALUES.flatMap { it.monsters.toSet() } + BossTask.entries.flatMap { it.monsters.toSet() })
            .map { it.lowercase().trim() }
            .toSet()
        
        //println(npcNames)
        
        npcs(*NPCDefinitions.getDefinitions()
            .filterNotNull()
            .filter { it.combatLevel >= 50 }
            .mapNotNull { npcDef -> npcDef.takeIf { npcNames.contains(it.name.lowercase()) }?.id }
            .toSet()
            .toIntArray()
        )
        
        onDeath {
            rollStaticTableAndDrop(killer, Main)
            if (killer.slayer.isCurrentAssignment(npc)) {
                if (rollStaticTableAndDrop(killer, Tertiary).isNotEmpty())
                    killer.sendMessage(Colour.RS_PURPLE.wrap("A mysterious casket has materialised on the ground."))
            }
        }
        
        buildTable {
            Main(1700) { // 10% chance
                RED_SPIDERS_EGGS quantity (10..15).noted rarity 10
                LIMPWURT_ROOT quantity (10..15).noted rarity 10
                SNAPE_GRASS quantity (10..15).noted rarity 10
                CRUSHED_NEST quantity (10..15).noted rarity 10
                MORT_MYRE_FUNGUS quantity (10..15).noted rarity 10
                UNICORN_HORN_DUST quantity (10..15).noted rarity 10
                DRAGON_SCALE_DUST quantity (10..15).noted rarity 10
                CRUSHED_SUPERIOR_DRAGON_BONES quantity (10..15).noted rarity 10
                WINE_OF_ZAMORAK quantity (10..15).noted rarity 10
                WHITE_BERRIES quantity (10..15).noted rarity 10
                POTATO_CACTUS quantity (10..15).noted rarity 10
                NAIL_BEAST_NAILS quantity (10..15).noted rarity 10
                GOAT_HORN_DUST quantity (10..15).noted rarity 10
                SNAKE_WEED quantity (10..15).noted rarity 10
                JANGERBERRIES quantity (10..15).noted rarity 10
                MAGIC_ROOTS quantity (10..15).noted rarity 10
                COCONUT_MILK quantity (10..15).noted rarity 10
            }
            Tertiary(100) { // 10% chance
                CASKET_7956 quantity 1 rarity 10 info { "Only dropped on slayer task" }
            }
        }
        
        
        
    }
}