package com.zenyte.game.content.theatreofblood.room

import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.Spawnable
import java.util.concurrent.TimeUnit

/**
 * @author Tommeh
 * @author Jire
 */
class VyreOrator(id: Int, tile: Location?, facing: Direction, radius: Int) : NPC(id, tile, facing, radius), Spawnable {

    private var chatDelay = 0L

    override fun processNPC() {
        super.processNPC()
        val now = Utils.currentTimeMillis()
        if (chatDelay < now) {
            chatDelay = now + TimeUnit.SECONDS.toMillis(4)
            forceTalk = messages[Utils.random(messages.size - 1)]
        }
    }

    override fun validate(id: Int, name: String) = name.equals("vyre orator", ignoreCase = true)

    internal companion object {

        private val messages = arrayOf(
            "Welcome, one and all, to the Theatre of Blood.",
            "Verzik Vitur cordially invites you to the Theatre.",
            "Glory awaits those who enter the Theatre!",
            "Prove your worth in the Theatre!",
            "Only the Theatre can free you!"
        ).map { ForceTalk(it) }

    }

}