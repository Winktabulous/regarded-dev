package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.noted

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*

class RevenantsDropTable : NPCDropTableScript() {
    init {
        /**
         * This only handles the common/uncommon drops, all uniques are handled through DropProcessor implementations due to
         * significantly varied drop rates.
         */
        
        npcs(REVENANT_ORK, REVENANT_IMP, REVENANT_DEMON, REVENANT_DRAGON, REVENANT_DARK_BEAST, REVENANT_KNIGHT, REVENANT_HOBGOBLIN, REVENANT_HELLHOUND, REVENANT_GOBLIN, REVENANT_PYREFIEND)
        
        buildTable(165) {
            Main {
                DRAGON_PLATELEGS quantity 1 rarity 1
                DRAGON_PLATESKIRT quantity 1 rarity 1
                RUNE_FULL_HELM quantity 2 rarity 2
                RUNE_PLATEBODY quantity 2 rarity 2
                RUNE_PLATELEGS quantity 2 rarity 2
                RUNE_KITESHIELD quantity 2 rarity 2
                RUNE_WARHAMMER quantity 2 rarity 2
                DRAGON_LONGSWORD quantity 2 rarity 1
                DRAGON_DAGGER quantity 2 rarity 1
                SUPER_RESTORE4 quantity 4..10 rarity 4
                ONYX_BOLT_TIPS quantity 9..26 rarity 4
                DRAGONSTONE_BOLT_TIPS quantity 60..120 rarity 4
                DRAGONSTONE quantity 8..14 rarity 1
                DEATH_RUNE quantity 90..360 rarity 3
                BLOOD_RUNE quantity 90..360 rarity 3
                LAW_RUNE quantity 100..420 rarity 3
                ItemId.RUNITE_ORE quantity (5..12).noted rarity 6
                REVENANT_CAVE_TELEPORT quantity (1..3) rarity 6
                ADAMANTITE_BAR quantity (15..24).noted rarity 6
                ItemId.COAL quantity (100..250).noted rarity 6
                BATTLESTAFF quantity 8.noted rarity 5
                BLACK_DRAGONHIDE quantity (13..24).noted rarity 6
                MAHOGANY_PLANK quantity (19..37).noted rarity 5
                MAGIC_LOGS quantity (20..55).noted rarity 2
                YEW_LOGS quantity (70..180).noted rarity 3
                MANTA_RAY quantity (45..90).noted rarity 3
                RUNITE_BAR quantity (5..12).noted rarity 6
                BLIGHTED_ANCIENT_ICE_SACK quantity (8..55) rarity 9
                BLIGHTED_ENTANGLE_SACK quantity 8..55 rarity 9
                BLIGHTED_TELEPORT_SPELL_SACK quantity 8..55 rarity 9
                BLIGHTED_VENGEANCE_SACK quantity 8..55 rarity 9
                BLIGHTED_BIND_SACK quantity 8..40 rarity 9
                BLIGHTED_SURGE_SACK quantity 8..55 rarity 9
            }
        }
    }
}