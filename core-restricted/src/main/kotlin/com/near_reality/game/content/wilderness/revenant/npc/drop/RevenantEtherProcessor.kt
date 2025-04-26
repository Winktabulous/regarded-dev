package com.near_reality.game.content.wilderness.revenant.npc.drop

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.player.Player
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import mgi.types.config.npcs.NPCDefinitions
import net.runelite.api.ItemID
import kotlin.math.max
import kotlin.math.sqrt

/**
 * @author Kris | 18/04/2019 19:37
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("unused")
class RevenantEtherProcessor : DropProcessor() {

    override fun attach() {
        val ids = allIds
        appendDrop(DisplayedDrop(ItemID.BLOOD_MONEY, 1, 3, 5.0))
        for (id in ids) {
            val level = NPCDefinitions.getOrThrow(id).combatLevel
            val etherAmount = max(1.0, sqrt((level * 3).toDouble()).toInt().toDouble())
                .toInt()
            appendDrop(
                DisplayedDrop(
                    ItemId.REVENANT_ETHER,
                    1,
                    etherAmount,
                    1.0,
                    { p: Player?, npcId: Int -> npcId == id },
                    id
                )
            )
        }
    }

    override fun onDeath(npc: NPC, killer: Player) {
        if (Utils.random(4) == 0) npc.dropItem(killer, Item(ItemID.BLOOD_MONEY, Utils.random(1, 3)))
    }

    override fun ids(): IntArray {
        val set = IntOpenHashSet()
        set.add(7881)
        for (i in 7931..7940) {
            set.add(i)
        }
        return set.toIntArray()
    }
}
