package com.near_reality.game.content.gauntlet.npc.drops

import com.near_reality.game.content.gauntlet.gauntletReceivedWeaponFrame
import com.near_reality.game.content.gauntlet.gauntletStrongMonsterKills
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Always
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.always
import com.zenyte.game.item.ItemId.CRYSTAL_SHARDS
import com.zenyte.game.item.ItemId.RAW_PADDLEFISH
import com.zenyte.game.item.ItemId.TELEPORT_CRYSTAL
import com.zenyte.game.item.ItemId.WEAPON_FRAME_23871
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_SCORPION
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_UNICORN
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_WOLF

class CrystallineStrongMonsterDropTable : NPCDropTableScript() {

    val common = 75
    val uncommon = 50

    init {
        npcs(
            CRYSTALLINE_UNICORN,
            CRYSTALLINE_SCORPION,
            CRYSTALLINE_WOLF
        )

        buildTable(250) {
            Always {
                CRYSTAL_SHARDS quantity 50..105 rarity always
            }
            Main {
                WEAPON_FRAME_23871 quantity 1 dynamicRarity {
                    if (!gauntletReceivedWeaponFrame && gauntletStrongMonsterKills == 1) always else common
                } transformItem { item ->
                    gauntletReceivedWeaponFrame = true
                    item
                } info {
                    "Guaranteed weapon frame drop if you haven't received one before."
                }
                RAW_PADDLEFISH quantity 4 rarity common
                TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }
        }

    }
}
