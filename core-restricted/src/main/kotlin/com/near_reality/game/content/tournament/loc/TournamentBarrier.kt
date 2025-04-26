package com.near_reality.game.content.tournament.loc

import com.near_reality.game.content.tournament.TournamentState
import com.near_reality.game.content.tournament.tournamentOrNull
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the barrier to enter/leave the tournament fight waiting area.
 *
 * @author Tommeh | 31/05/2019 | 20:17
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentBarrier : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        player.dialogue(NpcId.TOURNAMENT_GUARD) {
            if (player.y < `object`.y) {
                if(player.tournamentOrNull?.participants?.hasMatchingIpWith(player)!!) {
                    npc("You cannot enter this tournament with another character.")
                    return@dialogue
                }
                when(player.tournamentOrNull?.state) {
                    is TournamentState.Ongoing ->
                        npc("The tournament has already started. You will not be able to enter the lobby.")
                    is TournamentState.Finished ->
                        npc("The tournament has already ended. Come back another time.")
                    is TournamentState.Scheduled -> {
                        player.lock(3)
                        player.setRunSilent(3)
                        player.addWalkSteps(player.x, `object`.y + 1, 2, false)
                        player.packetDispatcher.sendUpdateItemContainer(player.equipmentTemp.container)
                        player.packetDispatcher.sendUpdateItemContainer(player.inventoryTemp.container)
                    } null ->
                        npc("The tournament is currently not active.")
                }
            } else {
                options("Are you sure you\'d like to leave the tournament?") {
                    "Yes." {
                        player.lock(2)
                        player.setRunSilent(2)
                        player.addWalkSteps(player.x, `object`.y - 1, 2, false)
                        player.packetDispatcher.sendUpdateItemContainer(player.equipment.container)
                        player.packetDispatcher.sendUpdateItemContainer(player.inventory.container)
                    }
                    "No."()
                }
            }
        }
    }

    private fun List<Player>.hasMatchingIpWith(p: Player) : Boolean {
        forEach { if(it.ip == p.ip) return true }
        return false
    }

//
//    override fun handle(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String) {
//        player.routeEvent = ObjectEvent(
//            player,
//            ObjectStrategy(
//                `object`,
//                0,
//                RouteStrategy.BLOCK_FLAG_NORTH or RouteStrategy.BLOCK_FLAG_SOUTH
//            ),
//            getRunnable(player, `object`, name, optionId, option),
//            delay
//        )
//    }
//
//    override fun getRunnable(
//        player: Player,
//        `object`: WorldObject,
//        name: String,
//        optionId: Int,
//        option: String
//    ): Runnable {
//        return Runnable {
//            val existingObject = World.getObjectWithId(`object`, `object`.id)
//            if (existingObject == null || player.plane != `object`.plane) {
//                return@Runnable
//            }
//            player.stopAll()
//            player.faceLocation = player.location.transform(0, 2, 0)
//            if (!ObjectHandler.handleOptionClick(player, optionId, `object`)) {
//                return@Runnable
//            }
//            handleObjectAction(player, `object`, name, optionId, option)
//        }
//    }

    override fun getDelay(): Int = 1

    override fun getObjects(): Array<Any> = arrayOf(ObjectId.SHIMMERING_BARRIER_30397)
}
