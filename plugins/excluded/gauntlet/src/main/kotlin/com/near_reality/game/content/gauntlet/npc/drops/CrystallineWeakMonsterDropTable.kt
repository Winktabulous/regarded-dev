package com.near_reality.game.content.gauntlet.npc.drops

import com.near_reality.game.content.gauntlet.gauntletReceivedWeaponFrame
import com.near_reality.game.content.gauntlet.gauntletWeakMonsterKills
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Always
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.always
import com.zenyte.game.item.ItemId.CRYSTAL_SHARDS
import com.zenyte.game.item.ItemId.GRYM_LEAF_23875
import com.zenyte.game.item.ItemId.RAW_PADDLEFISH
import com.zenyte.game.item.ItemId.TELEPORT_CRYSTAL
import com.zenyte.game.item.ItemId.WEAPON_FRAME_23871
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_BAT
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_RAT
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_SPIDER

class CrystallineWeakMonsterDropTable : NPCDropTableScript() {

    val common = 50
    val uncommon = 15

    init {
        npcs(
            CRYSTALLINE_RAT,
            CRYSTALLINE_SPIDER,
            CRYSTALLINE_BAT
        )

        buildTable(165) {
            Always {
                CRYSTAL_SHARDS quantity 10..30 rarity always
            }
            Main {
                WEAPON_FRAME_23871 quantity 1 dynamicRarity {
                    if (!gauntletReceivedWeaponFrame && gauntletWeakMonsterKills == 3) always else common
                } transformItem { item ->
                    gauntletReceivedWeaponFrame = true
                    item
                } info {
                    "The third killed weak monster has a guaranteed weapon frame drop if you haven't received one before."
                }
                RAW_PADDLEFISH quantity 1..3 rarity common
                GRYM_LEAF_23875 quantity 1..3 rarity common
                TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }

        }
    }
}

