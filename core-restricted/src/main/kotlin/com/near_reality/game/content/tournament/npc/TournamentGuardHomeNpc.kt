package com.near_reality.game.content.tournament.npc

import com.near_reality.game.content.tournament.TournamentManager
import com.near_reality.game.content.tournament.TournamentState
import com.near_reality.game.util.formattedString
import com.zenyte.game.util.Direction
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId

class TournamentGuardHomeNpc : NPC(NpcId.TOURNAMENT_GUARD_16012, Location(3079, 3492, 0), Direction.EAST, 2) {

    private var lastForceChat: String? = null

    override fun processNPC() {
        if (!TournamentManager.enabled)
            return

        val activeTournaments = TournamentManager.listActiveTournaments()
        if (activeTournaments.isNotEmpty()) {
            sendRandomForceChat(
                interval = 20,
                possibleChats = buildSet {
                    if (activeTournaments.size > 1)
                        add("There are currently ${activeTournaments.size} tournaments active.")
                    activeTournaments.forEach {
                        when (val state = it.state) {
                            is TournamentState.Scheduled -> {
                                add("The ${it.preset} tournament is starting in ${state.startTimer.durationRemaining().formattedString}!")
                            }
                            is TournamentState.Ongoing -> {
                                add("Come watch round ${state.round} of the ${it.preset} tournament!")
                                add("There are ${it.participants.size} people left in the ${it.preset} tournament!.")
                            }
                            is TournamentState.Finished -> {
                                add("The ${it.preset} tournament has ended.")
                            }
                        }
                    }
                }
            )
        }
        super.processNPC()
    }

    private fun sendRandomForceChat(interval: Int, possibleChats: Set<String?>) {
        if (everyNthWorldCycle(interval)) {
            val chatPool = if (possibleChats.size > 1)
                possibleChats - lastForceChat
            else
                possibleChats
            if (chatPool.isEmpty())
                return
            val chat = chatPool.random()
            lastForceChat = chat
            setForceTalk(chat)
        }
    }

    private fun everyNthWorldCycle(n: Int) = WorldThread.getCurrentCycle() % n == 0L
}
