package com.near_reality.game.content.gauntlet.npc.drops

import com.near_reality.game.content.gauntlet.gauntletReceivedWeaponFrame
import com.near_reality.game.content.gauntlet.gauntletStrongMonsterKills
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Always
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.always
import com.zenyte.game.item.ItemId.CORRUPTED_SHARDS
import com.zenyte.game.item.ItemId.CORRUPTED_TELEPORT_CRYSTAL
import com.zenyte.game.item.ItemId.RAW_PADDLEFISH
import com.zenyte.game.item.ItemId.WEAPON_FRAME
import com.zenyte.game.world.entity.npc.NpcId.CORRUPTED_SCORPION
import com.zenyte.game.world.entity.npc.NpcId.CORRUPTED_UNICORN
import com.zenyte.game.world.entity.npc.NpcId.CORRUPTED_WOLF

class CorruptedStrongMonsterDropTable : NPCDropTableScript() {

    val common = 100
    val uncommon = 50

    init {
        npcs(
            CORRUPTED_UNICORN,
            CORRUPTED_SCORPION,
            CORRUPTED_WOLF
        )

        buildTable(250) {
            Always {
                CORRUPTED_SHARDS quantity 50..105 rarity always
            }
            Main {
                WEAPON_FRAME quantity 1 dynamicRarity {
                    if (!gauntletReceivedWeaponFrame && gauntletStrongMonsterKills == 1) always else common
                } transformItem { item ->
                    gauntletReceivedWeaponFrame = true
                    item
                } info {
                    "Guaranteed weapon frame drop if you haven't received one before."
                }
                RAW_PADDLEFISH quantity 4 rarity common
                CORRUPTED_TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }
        }

    }

}

