package com.near_reality.game.content.gauntlet.npc.drops

import com.near_reality.game.content.gauntlet.gauntletReceivedBowString
import com.near_reality.game.content.gauntlet.gauntletReceivedOrb
import com.near_reality.game.content.gauntlet.gauntletReceivedSpike
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Always
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.near_reality.scripts.npc.drops.table.DropTableType.Unique
import com.near_reality.scripts.npc.drops.table.always
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.CRYSTALLINE_BOWSTRING
import com.zenyte.game.item.ItemId.CRYSTAL_ORB
import com.zenyte.game.item.ItemId.CRYSTAL_SHARDS
import com.zenyte.game.item.ItemId.CRYSTAL_SPIKE
import com.zenyte.game.item.ItemId.RAW_PADDLEFISH
import com.zenyte.game.item.ItemId.TELEPORT_CRYSTAL
import com.zenyte.game.item.ItemId.WEAPON_FRAME_23871
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_BEAR
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_DARK_BEAST
import com.zenyte.game.world.entity.npc.NpcId.CRYSTALLINE_DRAGON
import com.zenyte.game.world.entity.player.Player
import kotlin.reflect.KMutableProperty1

class CrystallineDemiBossDropTable : NPCDropTableScript() {
    val common = 48
    val uncommon = 80

    init {
        npcs(
            CRYSTALLINE_BEAR,
            CRYSTALLINE_DRAGON,
            CRYSTALLINE_DARK_BEAST
        )

        buildTable(128) {
            Always {
                CRYSTAL_SHARDS quantity 50..105 rarity always
                WEAPON_FRAME_23871 quantity 1 rarity always
            }
            Main {
                RAW_PADDLEFISH quantity 5 rarity common
                TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }
            Unique {
                CRYSTAL_ORB quantity 1 rarity always onlyDroppedBy CRYSTALLINE_DRAGON transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedOrb)(this, item)
                }
                CRYSTAL_SPIKE quantity 1 rarity always onlyDroppedBy CRYSTALLINE_BEAR transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedSpike)(this, item)
                }
                CRYSTALLINE_BOWSTRING quantity 1 rarity always onlyDroppedBy CRYSTALLINE_DARK_BEAST transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedBowString)(this, item)
                }
            }
        }
    }

    fun tryUpgrade(receivedUpgrade: KMutableProperty1<Player, Boolean>): Player.(Item) -> Item? = { item ->
        if (!receivedUpgrade(this)) {
            receivedUpgrade.set(this, true)
            item
        } else
            buildMap {
                if (!gauntletReceivedOrb) put(CRYSTAL_ORB, Player::gauntletReceivedOrb)
                if (!gauntletReceivedSpike) put(CRYSTAL_SPIKE, Player::gauntletReceivedSpike)
                if (!gauntletReceivedBowString) put(CRYSTALLINE_BOWSTRING, Player::gauntletReceivedBowString)
            }.let {
                if (it.isEmpty()) {
                    null
                } else {
                    val itemId = it.keys.random()
                    val property = it[itemId]!!
                    property.set(this, true)
                    Item(itemId, 1)
                }
            }
    }
}