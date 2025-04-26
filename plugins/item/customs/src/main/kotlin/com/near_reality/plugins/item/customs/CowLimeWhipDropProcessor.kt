package com.near_reality.plugins.item.customs

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.player.Player

/**
 * Handles the one in a million chance of a Cow dropping a Lime whip.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class CowLimeWhipDropProcessor : DropProcessor() {

    override fun attach() {
        appendDrop(DisplayedDrop(CustomItemId.LIME_WHIP, 1, 1, DROP_RATE_LIME_WHIP.toDouble()))
    }

    override fun onDeath(npc: NPC, killer: Player) {
        if (Utils.random(DROP_RATE_LIME_WHIP) == 0) {
            val limeWhip = Item(CustomItemId.LIME_WHIP)
            npc.dropItem(killer, limeWhip)
        }
    }

    override fun ids() = intArrayOf(
        NpcId.COW,
        NpcId.COW_2791,
        NpcId.COW_2793,
        NpcId.COW_2795,
        NpcId.COW_5842,
        NpcId.COW_6401,
        NpcId.COW_10598,
        NpcId.PLAGUE_COW_11588,
        NpcId.PLAGUE_COW_11589,
        NpcId.UNDEAD_COW_4421
    )

    private companion object {
        const val DROP_RATE_LIME_WHIP = 1_000_000
    }
}
