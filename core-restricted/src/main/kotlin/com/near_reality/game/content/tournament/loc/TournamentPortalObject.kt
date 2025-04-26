package com.near_reality.game.content.tournament.loc

import com.near_reality.game.content.tournament.Tournament
import com.near_reality.game.content.tournament.TournamentManager
import com.near_reality.game.plugin.optionsMenu
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

@Suppress("unused")
class TournamentPortalObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val tournaments: List<Tournament> = TournamentManager.listActiveTournaments()
        if (tournaments.isEmpty()) {
            player.dialogue { plain("There are currently no active tournaments.") }
            return
        }
        when (option) {
            "View-Tournaments" -> {
                player.optionsMenu(tournaments) { tournament ->
                    if(player.equipment.isEmpty())
                        teleportTo(player, tournament)
                    else
                        player.sendMessage("Please unequip all of your items before entering")
                }
            }
        }
    }

    private fun teleportTo(
        player: Player,
        singleTournament: Tournament,
    ) = FadeScreen(player) { singleTournament.lobby.teleportPlayer(player) }.fade(3, true)

    override fun getObjects(): Array<Any> = arrayOf(ObjectId.CLAN_HALL_PORTAL)

    companion object {
        val LOCATION_IN_FRONT_OF_PORTAL = Location(3079, 3492, 0)
    }
}

private fun Equipment.isEmpty(): Boolean {
    return this.container.isEmpty
}
