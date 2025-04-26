package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Always
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.DropTableType.Tertiary
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.near_reality.scripts.npc.drops.table.tables.misc.TalismanDropTable
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.RareSeedDropTable
import com.zenyte.game.content.achievementdiary.DiaryComplexity
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.AIR_BATTLESTAFF
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.AIR_TALISMAN
import com.zenyte.game.item.ItemId.BATTLESTAFF
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.DAGANNOTH_BONES
import com.zenyte.game.item.ItemId.DAGANNOTH_HIDE
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.DRAGON_AXE
import com.zenyte.game.item.ItemId.EARTH_BATTLESTAFF
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.EARTH_TALISMAN
import com.zenyte.game.item.ItemId.ENSOULED_DAGANNOTH_HEAD
import com.zenyte.game.item.ItemId.FARSEER_HELM
import com.zenyte.game.item.ItemId.FREMENNIK_BLADE
import com.zenyte.game.item.ItemId.FREMENNIK_HELM
import com.zenyte.game.item.ItemId.FREMENNIK_SHIELD
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.LAW_RUNE
import com.zenyte.game.item.ItemId.MUD_BATTLESTAFF
import com.zenyte.game.item.ItemId.MUD_RUNE
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.OYSTER_PEARLS
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.SEERS_RING
import com.zenyte.game.item.ItemId.SKELETAL_BOTTOMS
import com.zenyte.game.item.ItemId.SKELETAL_TOP
import com.zenyte.game.item.ItemId.WATER_BATTLESTAFF
import com.zenyte.game.item.ItemId.WATER_TALISMAN
import com.zenyte.game.world.entity.npc.NpcId.DAGANNOTH_PRIME
import com.zenyte.game.world.entity.npc.NpcId.DAGANNOTH_PRIME_6497

class DagannothPrimeDropTable : NPCDropTableScript() {
    init {
        npcs(DAGANNOTH_PRIME, DAGANNOTH_PRIME_6497)
        
        buildTable {
            Always {
                DAGANNOTH_BONES quantity 1 rarity always transformItem {item ->
                    if(this.achievementDiaries.isAllSetCompleted(DiaryComplexity.ELITE, FremennikDiary.VALUES))
                        item.toNote()
                    item
                }
                DAGANNOTH_HIDE quantity 1 rarity always
            }
            Main(85) {
                // Weapons and armour
                EARTH_BATTLESTAFF quantity 1 rarity 3
                WATER_BATTLESTAFF quantity 1 rarity 2
                AIR_BATTLESTAFF quantity 1 rarity 2
                BATTLESTAFF quantity (1..10).noted rarity 1
                FREMENNIK_BLADE quantity 1 rarity 1
                FREMENNIK_SHIELD quantity 1 rarity 1
                FREMENNIK_HELM quantity 1 rarity 1
                MUD_BATTLESTAFF quantity 1 rarity 1
                DRAGON_AXE quantity 1 rarity 1
                FARSEER_HELM quantity 1 rarity 1
                SKELETAL_TOP quantity 1 rarity 1
                SKELETAL_BOTTOMS quantity 1 rarity 1
                SEERS_RING quantity 1 rarity 1
                // Runes
                AIR_RUNE quantity 100..200 rarity 3
                EARTH_RUNE quantity 50..100 rarity 2
                BLOOD_RUNE quantity 25..75 rarity 2
                LAW_RUNE quantity 10..75 rarity 2
                NATURE_RUNE quantity 25..75 rarity 2
                MUD_RUNE quantity 25..75 rarity 2
                DEATH_RUNE quantity 25..85 rarity 2
                // Seeds
                chance(7) roll RareSeedDropTable
                // Talismans
                chance(1) roll TalismanDropTable
                // Talismans (noted)
                EARTH_TALISMAN quantity (25..75).noted rarity 5
                AIR_TALISMAN quantity (25..75).noted rarity 3
                WATER_TALISMAN quantity (1..76).noted rarity 3
                // Other
                ItemId.SHARK quantity 5 rarity 4
                COINS_995 quantity (500..1109) rarity 3
                OYSTER_PEARLS quantity 1 rarity 3
                PURE_ESSENCE quantity 150.noted rarity 3
                GRIMY_RANARR_WEED quantity 1 rarity 3
                // Rare and Gem drop table
                chance(8) roll RareDropTable
                chance(10) roll GemDropTable
            }
            Tertiary {
                ENSOULED_DAGANNOTH_HEAD quantity 1 oneIn 20
            }
        }
    }
}