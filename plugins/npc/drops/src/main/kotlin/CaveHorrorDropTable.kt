package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Always
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.DropTableType.Tertiary
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.near_reality.scripts.npc.drops.table.tables.herb.HerbDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.AllotmentSeedDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.RareSeedDropTable
import com.zenyte.game.item.ItemId.ADAMANT_FULL_HELM
import com.zenyte.game.item.ItemId.BIG_BONES
import com.zenyte.game.item.ItemId.BLACK_MASK_10
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.CURVED_BONE
import com.zenyte.game.item.ItemId.ENSOULED_HORROR_HEAD
import com.zenyte.game.item.ItemId.LIMPWURT_ROOT
import com.zenyte.game.item.ItemId.LONG_BONE
import com.zenyte.game.item.ItemId.MAHOGANY_LOGS
import com.zenyte.game.item.ItemId.MITHRIL_AXE
import com.zenyte.game.item.ItemId.MITHRIL_KITESHIELD
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.RUNE_DAGGER
import com.zenyte.game.item.ItemId.TEAK_LOGS
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.CAVE_HORROR_1048
import com.zenyte.game.world.entity.npc.NpcId.CAVE_HORROR_1049
import com.zenyte.game.world.entity.npc.NpcId.CAVE_HORROR_1050
import com.zenyte.game.world.entity.npc.NpcId.CAVE_HORROR_1051

class CaveHorrorDropTable : NPCDropTableScript() {
    init {
        npcs(NpcId.CAVE_HORROR, CAVE_HORROR_1048, CAVE_HORROR_1049, CAVE_HORROR_1050, CAVE_HORROR_1051)

        buildTable {
            Always {
                BIG_BONES quantity 1 rarity always
            }
            Main(128) {
                // Weapons and armour
                MITHRIL_AXE quantity 1 rarity 3
                RUNE_DAGGER quantity 1 rarity 1
                ADAMANT_FULL_HELM quantity 1 rarity 1
                MITHRIL_KITESHIELD quantity 1 rarity 1
                BLACK_MASK_10 quantity 1 rarity 1
                // Runes
                NATURE_RUNE quantity 6 rarity 6
                NATURE_RUNE quantity 4 rarity 5
                NATURE_RUNE quantity 3 rarity 1
                // Herbs
                chance(13) roll HerbDropTable
                // Seeds
                chance(18) roll RareSeedDropTable
                chance(15) roll AllotmentSeedDropTable
                // Coins
                COINS_995 quantity 44 rarity 28
                COINS_995 quantity 132 rarity 12
                COINS_995 quantity 440 rarity 1
                // Other
                LIMPWURT_ROOT quantity 1 rarity 7
                TEAK_LOGS quantity 4.noted rarity 7
                MAHOGANY_LOGS quantity 2 rarity 3
                // Gem drop table
                chance(5) roll GemDropTable
            }
            Tertiary {
                ENSOULED_HORROR_HEAD quantity 1 oneIn 30
                LONG_BONE quantity 1 oneIn 200
                CURVED_BONE quantity 1 oneIn 3012
            }
        }
    }
}