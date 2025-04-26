package com.near_reality.game.content.tournament.spectating

import com.near_reality.game.world.PlayerEvent
import com.near_reality.game.world.WorldEventListener

object TournamentSpectatorUpdateHook : WorldEventListener<PlayerEvent.Update> {

    override fun on(event: PlayerEvent.Update) {
        val player = event.player
        if (player.temporaryAttributes.containsKey("tournament_spectating"))
            TournamentViewerInterface.refreshSpectator(player)
    }
}
