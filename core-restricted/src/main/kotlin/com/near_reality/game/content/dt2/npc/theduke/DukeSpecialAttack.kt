package com.near_reality.game.content.dt2.npc.theduke

import com.zenyte.game.content.boons.impl.AllGassedUp
import com.zenyte.game.content.boons.impl.EyeDontSeeYou
import com.zenyte.game.content.skills.slayer.SlayerEquipment
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.ProjectileUtils
import com.zenyte.game.util.RSColour
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
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
sealed interface DukeSpecialAttack {

    operator fun invoke(duke: DukeSucellusEntity, target: Player) {
        WorldTasksManager.schedule(object : TickTask() {
            override fun run() {
                target.sendDeveloperMessage("Scheduling Special Attack")
                duke.specialAttackActive = true
                execute(duke, target)
                duke.combat.combatDelay = 11
                stop()
            }
        }, 0, 0)
    }

    fun execute(duke: DukeSucellusEntity, target: Player)

    data object DeathGaze : DukeSpecialAttack {
        override fun execute(duke: DukeSucellusEntity, target: Player) {
            WorldTasksManager.schedule(object: TickTask() {
                fun Location.safeFromSpecial() : Boolean {
                    return this.x == duke.arena.leftBound.x || this.x == duke.arena.leftBound.x - 1 || this.x == duke.arena.rightBound.x || this.x == duke.arena.rightBound.x + 1
                }

                override fun run() {
                    if(duke.slumbering || duke.hitpoints == 0) {
                        stop()
                        return
                    }
                    when(ticks) {
                        0 -> {
                            duke.override().models(listOf(49194 to 49193)).add(49194)
                            target.sendMessage(Colour.RS_PURPLE.wrap("Duke Sucellus turns his gaze upon you..."))
                        }
                        1 -> {
                            duke.setAnimation(Animation(10180))
                        }
                        5 -> {
                            val safe = target.location.safeFromSpecial()
                            if (!safe) {
                                target.graphics = Graphics(369)
                                target.freeze(3, 0)
                                val dmg = if(target.hasBoon(EyeDontSeeYou::class.java)) Utils.random(30, 34)
                                    else Utils.random(88, 104)
                                target.applyHit(Hit(duke, dmg, HitType.TYPELESS))
                            } else {
                                target.sendMessage(Colour.RS_GREEN.wrap("You manage to avoid Duke Sucellus' gaze."))
                            }
                        }
                        7 -> {
                            duke.override().reset()
                        }
                        8 -> {
                            duke.specialAttackActive = false
                            stop()
                        }
                    }
                    ticks++
                }
            }, 0,0)



        }
    }



    data object GasFlare : DukeSpecialAttack {

        private const val GAS_PROJ_TYPE: Int = 2436

        private val proj: Projectile = Projectile(GAS_PROJ_TYPE, 87, 10, 20, 60, 2, 32, 10)

        override fun execute(duke: DukeSucellusEntity, target: Player) {
            val ventsNearby = duke.arena.ventPositions.sortedBy { it.getDistance(target.position) }.iterator()

            duke.animation = Animation(10180)
            target.sendSound(5002)

            // At this point, the vent logic should be migrated into the `GasVent` object to allow
            // easily setting the state for whether its active or not as well as on-hit effects.
            val ventTask = object : TickTask() {
                var trigger = 99
                lateinit var coord : Location
                override fun run() {
                    when(ticks) {
                        0 -> {
                            coord = ventsNearby.next()
                            trigger = World.sendProjectile(duke.position, coord, proj)
                        }
                        trigger -> { World.sendGraphics(Graphics(2431), coord) }
                        trigger + 1 -> { World.sendGraphics(Graphics(2432), coord) }
                        trigger + 2 -> { World.sendGraphics(Graphics(2433), coord) }
                        trigger + 3 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2431), coord)
                        }
                        trigger + 4 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2432), coord)
                        }
                        trigger + 5 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2433), coord)
                        }
                        trigger + 6 -> {
                            duke.specialAttackActive = false
                            stop()
                        }
                    }
                    ticks++
                }

            }
            WorldTasksManager.schedule(ventTask, 0, 0)
        }

        private fun checkTargetForGas(target: Player, location: Location) {
            if(target.hasBoon(AllGassedUp::class.java) && SlayerEquipment.FACE_MASK.isWielding(target))
                return

            if(target.location.withinDistance(location,2)) {
                target.applyHit(Hit(Utils.random(8, 15), HitType.POISON))
            }
        }

    }

    data object GasFlareEcho : DukeSpecialAttack {

        private const val GAS_PROJ_TYPE: Int = 2436

        private val proj: Projectile = Projectile(GAS_PROJ_TYPE, 87, 10, 20, 60, 2, 32, 10)

        override fun execute(duke: DukeSucellusEntity, target: Player) {
            val ventsNearby = duke.arena.ventPositions.sortedBy { it.getDistance(target.position) }.iterator()

            duke.animation = Animation(10180)
            target.sendSound(5002)

            // At this point, the vent logic should be migrated into the `GasVent` object to allow
            // easily setting the state for whether its active or not as well as on-hit effects.
            val ventTask = object : TickTask() {
                var trigger = 99
                lateinit var coord : Location
                override fun run() {
                    when(ticks) {
                        4 -> {
                            coord = ventsNearby.next()
                            trigger = World.sendProjectile(duke.position, coord, proj)
                        }
                        trigger -> { World.sendGraphics(Graphics(2431), coord) }
                        trigger + 1 -> { World.sendGraphics(Graphics(2432), coord) }
                        trigger + 2 -> { World.sendGraphics(Graphics(2433), coord) }
                        trigger + 3 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2431), coord)
                        }
                        trigger + 4 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2432), coord)
                        }
                        trigger + 5 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2433), coord)
                        }
                        trigger + 6 -> {
                            duke.specialAttackActive = false
                            stop()
                        }
                    }
                    ticks++
                }

            }
            WorldTasksManager.schedule(ventTask, 0, 0)
        }

        private fun checkTargetForGas(target: Player, location: Location) {
            if(target.hasBoon(AllGassedUp::class.java) && SlayerEquipment.FACE_MASK.isWielding(target))
                return

            if(target.location.withinDistance(location,2)) {
                target.applyHit(Hit(Utils.random(8, 15), HitType.POISON))
            }
        }

    }
}
