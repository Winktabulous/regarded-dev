
package com.near_reality.game.content.boss.nex.npc.drops

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*

class ZarosSpiritualWarriorDropTable : NPCDropTableScript() {
    init {
        npcs(SPIRITUAL_WARRIOR_11290)

        buildTable(640) {
            Main {
                // Ancient ceremonial robes
                ANCIENT_CEREMONIAL_MASK quantity 1 rarity  1
                ANCIENT_CEREMONIAL_TOP quantity 1 rarity  1
                ANCIENT_CEREMONIAL_LEGS quantity 1 rarity  1
                ANCIENT_CEREMONIAL_GLOVES quantity 1 rarity  1
                ANCIENT_CEREMONIAL_BOOTS quantity 1 rarity  1
                // Runes
                AIR_RUNE quantity 150 rarity 10
                CHAOS_RUNE quantity 1 rarity 15
                MIND_RUNE quantity 1 rarity 35
                MUD_RUNE quantity 15 rarity 35
                NATURE_RUNE quantity 1 rarity 20
                // Herbs
                GRIMY_AVANTOE quantity 1 rarity 15
                GRIMY_RANARR_WEED quantity 1 rarity 13
                GRIMY_SNAPDRAGON quantity 1 rarity 13
                GRIMY_TORSTOL quantity 1 rarity 6
                // Coins
                COINS_995 quantity 400..499 rarity 45
                COINS_995 quantity 1300..1337 rarity 30
                // Potions
                PRAYER_POTION2 quantity 1 rarity 45
                SUPER_ATTACK1 quantity  1 rarity 55
                SUPER_DEFENCE1 quantity 1 rarity 10
                SUPER_STRENGTH1 quantity 1 rarity 10
                // Other
                ADAMANT_CHAINBODY quantity 1 rarity 35
                ADAMANTITE_BAR quantity (1..4).noted rarity 40
                BLOOD_ESSENCE quantity 1 rarity 5
                ItemId.COAL quantity (1..10).noted rarity 40
                LOBSTER quantity 1 rarity 5
                NIHIL_SHARD quantity 2..5 rarity 5
                POTATO_CACTUS quantity 1 rarity 35
                PURE_ESSENCE quantity 23.noted rarity 40
                // Gem
                chance(5) roll GemDropTable
            }
            Tertiary {
                CLUE_SCROLL_HARD quantity 1 rarity 5
            }
        }
    }
}
