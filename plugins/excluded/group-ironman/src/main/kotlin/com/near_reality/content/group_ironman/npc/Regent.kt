package com.near_reality.content.group_ironman.npc

import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId

class Regent : NPC(NpcId.REGENT, waitForOresLocation, Direction.WEST, 0) {

    private var sequenceIndex = 0

    override fun processNPC() {
        if (hasWalkSteps()) {
            return
        }

        if (forceWalk != null) {
            calcFollow(forceWalk, -1, true, true, false)
        }

        if (location.matches(forceWalk)) {
            handleSequence()
        }
    }

    private fun handleSequence() {
        when (sequenceIndex) {
            GET_ORE -> {
                delay(1) {
                    setForceTalk("Thanks, I'll get right to arrow making.")
                    delay(1) {
                        forceWalk = furnaceLocation
                        sequenceIndex = SMELT
                    }
                }
            }
            SMELT -> {
                faceDirection(Direction.SOUTH_EAST)
                delay(1) {
                    setAnimation(Animation(899))
                    delay(2) {
                        setAnimation(null)
                        delay(2) {
                            forceWalk = anvilLocation
                            sequenceIndex = SMITH
                        }
                    }
                }
            }
            SMITH -> {
                faceDirection(Direction.EAST)
                delay(1) {
                    setAnimation(Animation(898))
                    delay(2) {
                        setAnimation(null)
                        delay(1) {
                            forceWalk = waitForOresLocation
                            faceDirection(Direction.WEST)
                        }
                    }
                }
            }
        }

        sequenceIndex = 0
    }

    fun startSequence() {
        sequenceIndex = GET_ORE
        handleSequence()
    }

    override fun isEntityClipped(): Boolean {
        return false
    }

    private companion object {
        val furnaceLocation = Location(3107, 3027, 0)
        val waitForOresLocation = Location(3105, 3024, 0)
        val anvilLocation = Location(3106, 3023, 0)
        const val GET_ORE = 1
        const val SMELT = 2
        const val SMITH = 3
    }
}
