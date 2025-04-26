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
import com.zenyte.game.item.ItemId.CORRUPTED_BOWSTRING
import com.zenyte.game.item.ItemId.CORRUPTED_ORB
import com.zenyte.game.item.ItemId.CORRUPTED_SHARDS
import com.zenyte.game.item.ItemId.CORRUPTED_SPIKE
import com.zenyte.game.item.ItemId.CORRUPTED_TELEPORT_CRYSTAL
import com.zenyte.game.item.ItemId.RAW_PADDLEFISH
import com.zenyte.game.item.ItemId.WEAPON_FRAME
import com.zenyte.game.world.entity.npc.NpcId.CORRUPTED_BEAR
import com.zenyte.game.world.entity.npc.NpcId.CORRUPTED_DARK_BEAST
import com.zenyte.game.world.entity.npc.NpcId.CORRUPTED_DRAGON
import com.zenyte.game.world.entity.player.Player
import kotlin.reflect.KMutableProperty1

class CorruptedDemiBossDropTable : NPCDropTableScript() {
    val common = 48
    val uncommon = 80

    init {
        npcs(
            CORRUPTED_BEAR,
            CORRUPTED_DRAGON,
            CORRUPTED_DARK_BEAST
        )

        buildTable(128) {
            Always {
                WEAPON_FRAME quantity 1 rarity always
                CORRUPTED_SHARDS quantity 50..105 rarity always
            }
            Main {
                RAW_PADDLEFISH quantity 5 rarity common
                CORRUPTED_TELEPORT_CRYSTAL quantity 1 rarity uncommon
            }
            Unique {
                CORRUPTED_ORB quantity 1 rarity always onlyDroppedBy CORRUPTED_DRAGON transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedOrb)(this, item)
                }
                CORRUPTED_SPIKE quantity 1 rarity always onlyDroppedBy CORRUPTED_BEAR transformItem { item ->
                    tryUpgrade(Player::gauntletReceivedSpike)(this, item)
                }
                CORRUPTED_BOWSTRING quantity 1 rarity always onlyDroppedBy CORRUPTED_DARK_BEAST transformItem { item ->
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
                if (!gauntletReceivedOrb) put(CORRUPTED_ORB, Player::gauntletReceivedOrb)
                if (!gauntletReceivedSpike) put(CORRUPTED_SPIKE, Player::gauntletReceivedSpike)
                if (!gauntletReceivedBowString) put(CORRUPTED_BOWSTRING, Player::gauntletReceivedBowString)
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
