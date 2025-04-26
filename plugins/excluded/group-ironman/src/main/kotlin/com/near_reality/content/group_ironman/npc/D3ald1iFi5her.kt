package com.near_reality.content.group_ironman.npc

import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.Spawnable

@Suppress("unused")
class D3ald1iFi5her(id: Int, tile: Location?, facing: Direction?, radius: Int)
    : NPC(id, tile, facing, radius), Spawnable {

    private var chatDelay: Long = 0

    override fun processNPC() {
        super.processNPC()
        if (chatDelay < Utils.currentTimeMillis() && Utils.random(20) == 0) {
            chatDelay = Utils.currentTimeMillis() + 5000
            forceTalk = MESSAGES.random()
        }
    }

    override fun validate(id: Int, name: String?): Boolean =
        id == NpcId.D3AD1I_F15HER

    private companion object {
        val MESSAGES = arrayOf(
            ForceTalk("Cor blimey, This is a big one!"),
            ForceTalk("Fishing levels?"),
            ForceTalk("Oh no... It got away!"),
            ForceTalk("Is that a shark?"),
            ForceTalk("Here fishy fishies!"),
        )
    }
}
