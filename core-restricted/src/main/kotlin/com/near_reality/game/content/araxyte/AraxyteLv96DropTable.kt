package com.near_reality.game.content.araxyte

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.DropTableType.Tertiary
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.ADAMANT_BATTLEAXE
import com.zenyte.game.item.ItemId.ADAMANT_LONGSWORD
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.ARANEA_BOOTS
import com.zenyte.game.item.ItemId.ARAXYTE_HEAD
import com.zenyte.game.item.ItemId.ARAXYTE_VENOM_SACK
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.CELASTRUS_SEED
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.COSMIC_RUNE
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.DRAGONFRUIT_TREE_SEED
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.GRIMY_AVANTOE
import com.zenyte.game.item.ItemId.GRIMY_CADANTINE
import com.zenyte.game.item.ItemId.GRIMY_DWARF_WEED
import com.zenyte.game.item.ItemId.GRIMY_KWUARM
import com.zenyte.game.item.ItemId.GRIMY_LANTADYME
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.GRIMY_SNAPDRAGON
import com.zenyte.game.item.ItemId.GRIMY_TORSTOL
import com.zenyte.game.item.ItemId.MAGIC_SEED
import com.zenyte.game.item.ItemId.MAHOGANY_SEED
import com.zenyte.game.item.ItemId.MAPLE_SEED
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.PALM_TREE_SEED
import com.zenyte.game.item.ItemId.PAPAYA_TREE_SEED
import com.zenyte.game.item.ItemId.RANARR_SEED
import com.zenyte.game.item.ItemId.REDWOOD_TREE_SEED
import com.zenyte.game.item.ItemId.RUNE_DAGGER
import com.zenyte.game.item.ItemId.RUNE_MED_HELM
import com.zenyte.game.item.ItemId.RUNE_PLATELEGS
import com.zenyte.game.item.ItemId.SCROLL_BOX_ELITE
import com.zenyte.game.item.ItemId.SNAPDRAGON_SEED
import com.zenyte.game.item.ItemId.SOUL_RUNE
import com.zenyte.game.item.ItemId.SPIRIT_SEED
import com.zenyte.game.item.ItemId.TEAK_SEED
import com.zenyte.game.item.ItemId.TORSTOL_SEED
import com.zenyte.game.item.ItemId.WATERMELON_SEED
import com.zenyte.game.item.ItemId.WATER_RUNE
import com.zenyte.game.item.ItemId.WILLOW_SEED
import com.zenyte.game.item.ItemId.YEW_SEED
import com.zenyte.game.world.entity.npc.NpcId.ARAXYTE_LV_96

class AraxyteLv96DropTable : NPCDropTableScript() {
    init {
        npcs(ARAXYTE_LV_96)
        
        onDeath {
            npc.dropItem(killer, Item(ARAXYTE_VENOM_SACK))
            rollStaticTableAndDrop(killer, Tertiary)
            rollStaticTableAndDrop(killer, Main)
        }
        
        buildTable(100) {
            Main {
                // Weapons and Armor
                ADAMANT_LONGSWORD quantity 1 oneIn 25
                ADAMANT_BATTLEAXE quantity 1 oneIn 25
                RUNE_DAGGER quantity 1 oneIn 42
                RUNE_MED_HELM quantity 1 oneIn 63
                RUNE_PLATELEGS quantity 1 oneIn 63
                ARANEA_BOOTS quantity 1 oneIn 4000
                // Runes and Ammo
                AIR_RUNE quantity IntRange(120, 140) oneIn 13
                WATER_RUNE quantity IntRange(120, 140) oneIn 13
                EARTH_RUNE quantity IntRange(120, 140) oneIn 13
                FIRE_RUNE quantity IntRange(120, 140) oneIn 13
                COSMIC_RUNE quantity IntRange(7, 12) oneIn 25
                CHAOS_RUNE quantity IntRange(10, 15) oneIn 25
                NATURE_RUNE quantity IntRange(15, 20) oneIn 25
                DEATH_RUNE quantity IntRange(20, 25) oneIn 25
                BLOOD_RUNE quantity IntRange(15, 18) oneIn 25
                SOUL_RUNE quantity IntRange(9, 12) oneIn 25
                // Herbs
                GRIMY_KWUARM quantity 1 oneIn 40
                GRIMY_DWARF_WEED quantity 1 oneIn 51
                GRIMY_CADANTINE quantity 1 oneIn 51
                GRIMY_LANTADYME quantity 1 oneIn 68
                GRIMY_AVANTOE quantity 1 oneIn 82
                GRIMY_RANARR_WEED quantity 1 oneIn 102
                GRIMY_SNAPDRAGON quantity 1 oneIn 102
                GRIMY_TORSTOL quantity 1 oneIn 136
                // Seeds
                RANARR_SEED quantity 1 oneIn 425
                SNAPDRAGON_SEED quantity 1 oneIn 455
                TORSTOL_SEED quantity 1 oneIn 580
                WATERMELON_SEED quantity 15 oneIn 607
                WILLOW_SEED quantity 1 oneIn 637
                MAHOGANY_SEED quantity 1 oneIn 708
                MAPLE_SEED quantity 1 oneIn 708
                TEAK_SEED quantity 1 oneIn 708
                YEW_SEED quantity 1 oneIn 708
                PAPAYA_TREE_SEED quantity 1 oneIn 911
                MAGIC_SEED quantity 1 oneIn 1159
                PALM_TREE_SEED quantity 1 oneIn 1275
                SPIRIT_SEED quantity 1 oneIn 1594
                DRAGONFRUIT_TREE_SEED quantity 1 oneIn 2125
                CELASTRUS_SEED quantity 1 oneIn 3188
                REDWOOD_TREE_SEED quantity 1 oneIn 3188
                // Other
                COINS_995 quantity IntRange(800, 1200) oneIn 13
                ARAXYTE_VENOM_SACK quantity 2 oneIn 25
                ARAXYTE_HEAD quantity 1 oneIn 2000
            }
        
            Tertiary {
                SCROLL_BOX_ELITE quantity 1 oneIn 128
            }
        }
        
    }
}