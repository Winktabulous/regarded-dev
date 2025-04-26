package com.near_reality.game.content.tournament.loc

import com.near_reality.game.content.tournament.preset.TournamentPreset
import com.near_reality.game.content.tournament.tournamentOrNull
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the Tournament Supplies object action, which opens the Tournament Presets interface.
 *
 * @author Tommeh | 01/06/2019 | 14:50
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentSupplies : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        when(player.tournamentOrNull?.preset) {
                TournamentPreset.F2P_PURE,
                TournamentPreset.MYSTERY_BOX,
                TournamentPreset.BOXING,
                TournamentPreset.DDS -> {
                    player.sendMessage("You are not able to access these supplies during this variant.")
                    return
                }
            else -> {}
        }
        if (option == "View")
            GameInterface.TOURNAMENT_PRESETS.open(player)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(ObjectId.TOURNAMENT_SUPPLIES, ObjectId.TOURNAMENT_SUPPLIES_35007)
}
