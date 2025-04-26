package com.near_reality.game.content.pvm_arena.npc

import com.near_reality.game.content.pvm_arena.PvmArenaManager
import com.near_reality.game.content.pvm_arena.PvmArenaState
import com.near_reality.game.util.formattedString
import com.zenyte.game.util.Direction
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.Spawnable
import kotlin.time.Duration.Companion.hours

/**
 * Handles the PvM Arena Ghost NPC (Sir Eldric), announces the time left until the arena opens.
 *
 * @author Stan van der Bend
 */
@Suppress("unused", "SpellCheckingInspection")
class PvmArenaSirEldric(id: Int, tile: Location?, facing: Direction?, radius: Int) : NPC(id, tile, facing, radius), Spawnable {

    override fun processNPC() {
        super.processNPC()
        if (everyNthWorldCycle(15)) {
            when(val state = PvmArenaManager.state) {
                is PvmArenaState.Idle -> {
                    setForceTalk("The arena will open in ${state.timeLeft.formattedString}")
                }
                else -> Unit
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun everyNthWorldCycle(n: Int) = WorldThread.getCurrentCycle() % n == 0L

    override fun validate(id: Int, name: String?): Boolean {
        return id == NpcId.GHOST_3516
    }
}
