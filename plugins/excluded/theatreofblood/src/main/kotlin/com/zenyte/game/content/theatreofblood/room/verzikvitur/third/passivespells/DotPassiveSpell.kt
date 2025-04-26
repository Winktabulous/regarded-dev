package com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
internal object DotPassiveSpell : PassiveSpell {

    override val nextSpell = MeteorPassiveSpell

    private val castAnimation = Animation(8126)
    private val dotGraphics = Graphics(1595)

    private val avoidDamageGraphics = Graphics(1597)
    private val didntAvoidDamageGraphics = Graphics(1600, 0, 96)

    private const val PROJECTILE_ID = 1596

    override fun VerzikVitur.cast() {
        setTransformation(8373)
        resetWalkSteps()
        cancelCombat()
        lock()

        animation = castAnimation

        fun Location.sendDot() = World.sendGraphics(dotGraphics, this)

        val availableLocations: MutableList<Location> = ArrayList()
        populateList(availableLocations)

        val validTargets = room.validTargets
        val dots: MutableSet<Location> = HashSet(validTargets.size)
        for (p in validTargets) {
            val dot = createDot(availableLocations) ?: continue

            dots.add(dot)
            dot.sendDot()

            WorldTasksManager.schedule({
                World.sendProjectile(
                    this, p,
                    Projectile(PROJECTILE_ID, 186, 0, 0, 59, 120, 0, 0)
                )
            }, 9)
        }

        val dotDuration = 5
        for (i in 1..2) {
            WorldTasksManager.schedule({
                for (dot in dots)
                    dot.sendDot()
            }, dotDuration * i)
        }

        WorldTasksManager.schedule({
            setTransformation(8374)
            unlock()
            attackRandomTarget()
        }, 10)

        WorldTasksManager.schedule({
            val players = room.entered
            val avoidedPlayers = HashSet<Player>()

            dotsLoop@ for (dot in dots) {
                for (p in players) {
                    if (p.location == dot && room.isValidTarget(p)) {
                        avoidedPlayers.add(p)
                        continue@dotsLoop
                    }
                }
            }

            for (p in players) {
                if (!room.isValidTarget(p)) continue

                if (avoidedPlayers.contains(p)) {
                    p.graphics = avoidDamageGraphics
                    p.sendMessage("The power resonating here protects you from the blast.")
                } else {
                    p.graphics = didntAvoidDamageGraphics
                    p.applyHit(Hit(80, HitType.REGULAR))
                }
            }
        }, 13)
    }

    private fun createDot(availableLocations: MutableList<Location>): Location? {
        val randomIndex = Utils.randomNoPlus(availableLocations.size)
        val randomLocation = availableLocations[randomIndex]
        availableLocations.removeAt(randomIndex)
        return randomLocation
    }

    private fun VerzikVitur.populateList(availableLocations: MutableList<Location>) {
        val startLocation = room.getLocation(3155, 4303)
        val endLocation = room.getLocation(3181, 4322)
        for (x in startLocation.x..endLocation.x) {
            for (y in startLocation.y..endLocation.y) {
                if (x < location.x || y < location.y || x >= location.x + size || y >= location.y + size) {
                    availableLocations.add(Location(x, y))
                }
            }
        }
    }

}