package com.near_reality.plugins.area.ferox_enclave

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the entering and leaving of the Ferox Enclave area.
 *
 * @author Tamatea
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class FeroxEnclaveBarrier : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        val barrier = Barrier.forPosition(player.position) ?: return

        if (barrier.goingOutside(player.position)) {

            if (player.variables.isDontPromptFeroxEnclaveBarrier) {
                walkThroughBarrier(player, `object`)
            } else {
                player.dialogue {
                    plain(
                        "When returning to the Enclave, if you are teleblocked, you will not " +
                                "be allowed to enter the Enclave until the teleblock has worn off." +
                                Colour.RED.wrap(
                                    "You will also be attackable in the safe zone outside the Enclave if " +
                                            "you are teleblocked."
                                )
                    )
                    options("Continue through the Barrier?") {
                        "Yes." {
                            walkThroughBarrier(player, `object`)
                        }
                        "Yes, and don't ask again." {
                            player.variables.isDontPromptFeroxEnclaveBarrier = true
                            walkThroughBarrier(player, `object`)
                        }
                        "No." {}
                    }
                }
            }
        } else {

            if (player.variables.getTime(TickVariable.TELEBLOCK) > 0) {
                player.sendMessage(Colour.RED.wrap("You are currently teleblocked, it would be too dangerous to allow you into the Enclave."))
                return
            }

            walkThroughBarrier(player, `object`)
        }
    }



    private fun walkThroughBarrier(player: Player, `object`: WorldObject) {
        player.resetWalkSteps()
        player.addWalkSteps(`object`.x, `object`.y, 1, true)
        player.lock(if (player.hasWalkSteps()) 2 else 1)
        WorldTasksManager.scheduleOrExecute({
            val location: WorldObject = `object`.location
            val mod = if (location.y == 3639) 2 else if (location.x == 3123) 4 else 1
            val direction = Direction.cardinalDirections[`object`.rotation - mod and 3]
            val destination: Location = `object`.transform(direction, if (player.matches(`object`)) 1 else 0)
            player.addWalkSteps(destination.x, destination.y, 1, false)
        }, if (player.hasWalkSteps()) 1 else -1)
    }

    override fun getObjects() = arrayOf(ObjectId.BARRIER_39653, ObjectId.BARRIER_39652)
}
