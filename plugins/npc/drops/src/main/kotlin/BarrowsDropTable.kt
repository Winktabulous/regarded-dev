package com.zenyte.game.content.minigame.barrows

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.DropTableType.Tertiary
import com.near_reality.scripts.npc.drops.table.DropTableType.Unique
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.ADAMANTITE_BAR
import com.zenyte.game.item.ItemId.AHRIMS_HOOD
import com.zenyte.game.item.ItemId.AHRIMS_ROBESKIRT
import com.zenyte.game.item.ItemId.AHRIMS_ROBETOP
import com.zenyte.game.item.ItemId.AHRIMS_STAFF
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.ASTRAL_RUNE
import com.zenyte.game.item.ItemId.BLOOD_ESSENCE
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.BOLT_RACK
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.DHAROKS_GREATAXE
import com.zenyte.game.item.ItemId.DHAROKS_HELM
import com.zenyte.game.item.ItemId.DHAROKS_PLATEBODY
import com.zenyte.game.item.ItemId.DHAROKS_PLATELEGS
import com.zenyte.game.item.ItemId.GRIMY_AVANTOE
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.GRIMY_SNAPDRAGON
import com.zenyte.game.item.ItemId.GRIMY_TORSTOL
import com.zenyte.game.item.ItemId.GUTHANS_CHAINSKIRT
import com.zenyte.game.item.ItemId.GUTHANS_HELM
import com.zenyte.game.item.ItemId.GUTHANS_PLATEBODY
import com.zenyte.game.item.ItemId.GUTHANS_WARSPEAR
import com.zenyte.game.item.ItemId.KARILS_COIF
import com.zenyte.game.item.ItemId.KARILS_CROSSBOW
import com.zenyte.game.item.ItemId.KARILS_LEATHERSKIRT
import com.zenyte.game.item.ItemId.KARILS_LEATHERTOP
import com.zenyte.game.item.ItemId.LAVA_RUNE
import com.zenyte.game.item.ItemId.MUD_RUNE
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.SCROLL_BOX_HARD
import com.zenyte.game.item.ItemId.SMOKE_RUNE
import com.zenyte.game.item.ItemId.SOUL_RUNE
import com.zenyte.game.item.ItemId.SUPER_ATTACK2
import com.zenyte.game.item.ItemId.SUPER_DEFENCE2
import com.zenyte.game.item.ItemId.SUPER_DEFENCE3
import com.zenyte.game.item.ItemId.SUPER_RESTORE1
import com.zenyte.game.item.ItemId.TORAGS_HAMMERS
import com.zenyte.game.item.ItemId.TORAGS_HELM
import com.zenyte.game.item.ItemId.TORAGS_PLATEBODY
import com.zenyte.game.item.ItemId.TORAGS_PLATELEGS
import com.zenyte.game.item.ItemId.VERACS_BRASSARD
import com.zenyte.game.item.ItemId.VERACS_FLAIL
import com.zenyte.game.item.ItemId.VERACS_HELM
import com.zenyte.game.item.ItemId.VERACS_PLATESKIRT
import com.zenyte.game.world.entity.npc.NpcId.AHRIM_THE_BLIGHTED
import com.zenyte.game.world.entity.npc.NpcId.DHAROK_THE_WRETCHED
import com.zenyte.game.world.entity.npc.NpcId.GUTHAN_THE_INFESTED
import com.zenyte.game.world.entity.npc.NpcId.KARIL_THE_TAINTED
import com.zenyte.game.world.entity.npc.NpcId.TORAG_THE_CORRUPTED
import com.zenyte.game.world.entity.npc.NpcId.VERAC_THE_DEFILED

class BarrowsDropTable : NPCDropTableScript() {
    init {
        npcs(GUTHAN_THE_INFESTED, AHRIM_THE_BLIGHTED, KARIL_THE_TAINTED, VERAC_THE_DEFILED, DHAROK_THE_WRETCHED, TORAG_THE_CORRUPTED)

        buildTable(650) {
            Main {
                // Runes
                AIR_RUNE quantity 40..70 rarity 35
                ASTRAL_RUNE quantity 38..98 rarity 75
                BLOOD_RUNE quantity 25 rarity 10
                LAVA_RUNE quantity 30..60 rarity 45
                DEATH_RUNE quantity 25 rarity 20
                MUD_RUNE quantity 40..70 rarity 35
                SMOKE_RUNE quantity 100..150 rarity 10
                SOUL_RUNE quantity 25 rarity 15
                // Herbs
                GRIMY_AVANTOE quantity 1 rarity 15
                GRIMY_RANARR_WEED quantity 1 rarity 13
                GRIMY_SNAPDRAGON quantity 1 rarity 13
                GRIMY_TORSTOL quantity 1 rarity 45
                // Coins
                COINS_995 quantity 1300..1337 rarity 30
                COINS_995 quantity 6900..6942 rarity 10
                // Potions
                SUPER_DEFENCE2 quantity 1 rarity 10
                SUPER_RESTORE1 quantity 1 rarity 45
                SUPER_ATTACK2 quantity 1 rarity 55
                SUPER_DEFENCE3 quantity 1 rarity 20
                // Other
                ADAMANTITE_BAR quantity (1..4).noted rarity 40
                BLOOD_ESSENCE quantity 1 rarity 5
                ItemId.COAL quantity (1..10).noted rarity 40
                PURE_ESSENCE quantity 46.noted rarity 40
                BOLT_RACK quantity 30..50 rarity 75
                // Gem
                chance(5) roll GemDropTable
            }
            Unique {
                chance(20) roll BarrowsUniques
            }
            Tertiary {
                SCROLL_BOX_HARD quantity 1 rarity 30
            }
        }
    }

    object BarrowsUniques : StandaloneDropTableBuilder({
        limit = 80
        static {
            AHRIMS_HOOD quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED
            AHRIMS_ROBESKIRT quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED
            AHRIMS_STAFF quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED
            AHRIMS_ROBETOP quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED

            GUTHANS_WARSPEAR quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED
            GUTHANS_CHAINSKIRT quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED
            GUTHANS_HELM quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED
            GUTHANS_PLATEBODY quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED

            KARILS_COIF quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED
            KARILS_CROSSBOW quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED
            KARILS_LEATHERTOP quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED
            KARILS_LEATHERSKIRT quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED

            VERACS_BRASSARD quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED
            VERACS_FLAIL quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED
            VERACS_HELM quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED
            VERACS_PLATESKIRT quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED

            DHAROKS_GREATAXE quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED
            DHAROKS_HELM quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED
            DHAROKS_PLATEBODY quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED
            DHAROKS_PLATELEGS quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED

            TORAGS_HAMMERS quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED
            TORAGS_HELM quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED
            TORAGS_PLATELEGS quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED
            TORAGS_PLATEBODY quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED
        }
    })
}
