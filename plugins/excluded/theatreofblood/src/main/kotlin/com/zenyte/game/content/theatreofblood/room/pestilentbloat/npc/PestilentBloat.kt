package com.zenyte.game.content.theatreofblood.room.pestilentbloat.npc

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.zenyte.game.content.theatreofblood.party.RaidingParty.Companion.getPlayer
import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.pestilentbloat.PestilentBloatRoom
import com.zenyte.game.model.CameraShakeType
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.ProjectileUtils
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.calog.CAType
import com.zenyte.utils.TimeUnit

/**
 * @author Tommeh
 * @author Jire
 */
internal class PestilentBloat(room: PestilentBloatRoom) :
    TheatreBossNPC<PestilentBloatRoom>(room, NpcId.PESTILENT_BLOAT, room.getLocation(3299, 4445, 0)),
    CombatScript {

    private val northWest: Location = room.getLocation(3288, 4451, 0)
    private val southWest: Location = room.getLocation(3288, 4440, 0)
    private val northEast: Location = room.getLocation(3299, 4451, 0)
    private val southEast: Location = room.getLocation(3299, 4440, 0)
    private var pathStrategy: BiMap<Location, Location> = HashBiMap.create(
        mapOf(
            northWest to southWest,
            southWest to southEast,
            southEast to northEast,
            northEast to northWest
        )
    )
    private var previousCorner: Location? = null
    private var performingStompAttack = false
    private var previousMeatLocations: MutableList<Location> = ArrayList(16)
    private var stompCount = 0
    private var shutdownCount = 0

    override fun spawn(): NPC = super.spawn().apply {
        addWalkSteps(northEast.x, northEast.y, -1, false)
    }

    override fun performDefenceAnimation(attacker: Entity) {
        val blockDefinitions = combatDefinitions.blockDefinitions
        val sound = blockDefinitions.sound
        if (sound != null && attacker is Player) {
            WorldTasksManager.schedule {
                attacker.sendSound(sound)
            }
        }
    }

    override fun autoRetaliate(source: Entity) {}
    override fun checkAggressivity() = false

    override fun isPathfindingEventAffected() = false

    public override fun isFreezeable() = performingStompAttack

    override fun getRangedPrayerMultiplier() = 0.50

    override fun addWalkStep(nextX: Int, nextY: Int, lastX: Int, lastY: Int, check: Boolean) =
        !isFrozen && super.addWalkStep(nextX, nextY, lastX, lastY, check)

    override fun handleIngoingHit(hit: Hit) {
        if (hasWalkSteps())
            hit.damage /= 2

        super.handleIngoingHit(hit)
    }

    override fun processNPC() {
        super.processNPC()

        val destination = pathStrategy[location]
        if (destination != null) {
            addWalkSteps(destination.x, destination.y, -1, false)
            previousCorner = destination
        }
        if (room.started && !performingStompAttack) {
            for (p in room.validTargets) {
                if (!ProjectileUtils.isProjectileClipped(this, p, location, p, false)) {
                    val ticks = World.sendProjectile(this, p, fliesProjectile)
                    WorldTasksManager.schedule({ fliesAttack(p) }, ticks)
                }
            }
        }
    }

    private fun fliesAttack(player: Player) {
        delayHit(0, player, Hit(this, Utils.random(10, 20), HitType.RANGED))
        player.graphics = fliesImpactGfx
        player.sendSound(fliesSounds[Utils.random(fliesSounds.size - 1)])

        for (p in room.validTargets) {
            if (p == player || !player.position.withinDistance(p.position, 1)) continue

            delayHit(
                World.sendProjectile(player, p, fliesProjectile),
                p,
                Hit(this, Utils.random(10, 20), HitType.RANGED).onLand {
                    p.graphics = fliesImpactGfx
                    p.sendSound(fliesSounds[Utils.random(fliesSounds.size - 1)])
                }
            )
        }
    }

    fun inversePath(delay: Int) {
        if (performingStompAttack) return

        WorldTasksManager.schedule({
            pathStrategy = pathStrategy.inverse()
            val previousCorner = previousCorner ?: return@schedule
            val next = pathStrategy[previousCorner]!!
            resetWalkSteps()
            addWalkSteps(next.x, next.y, -1, false)
            this.previousCorner = next
        }, delay)
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        if (shutdownCount < 3) {
            val players = room.validTargets
            for (player in players) {
                player.combatAchievements.complete(CAType.TWO_DOWN)
            }
        }
    }

    fun stompAttack(delay: Int) {
        WorldTasksManager.schedule(object : TickTask() {
            override fun run() {
                if (room.players.isEmpty() || isDead || isFinished) {
                    stop()
                    return
                }
                if (ticks == 0) {
                    shutdownCount++
                    performingStompAttack = true
                    resetWalkSteps()
                    freeze(32)
                    setAnimation(stompAnimation)
                } else if (ticks == 30) {
                    for (p in room.validTargets)
                        p.packetDispatcher.sendCameraShake(CameraShakeType.UP_AND_DOWN, 20, 5, 0)
                    combatDefinitions.resetStats()
                } else if (ticks == 31) {
                    for (p in room.validTargets) {
                        if (!isProjectileClipped(p, true)) {
                            delayHit(-1, p, Hit(this@PestilentBloat, Utils.random(40, 80), HitType.REGULAR))
                        }
                    }
                } else if (ticks == 33 || (room.raid.hardMode && Utils.randomBoolean(12))) {
                    for (m in room.raid.party.members) {
                        val member = getPlayer(m) ?: continue
                        member.packetDispatcher.resetCamera()
                    }
                    if (previousCorner != null)
                        addWalkSteps(previousCorner!!.x, previousCorner!!.y, -1, false)
                    performingStompAttack = false
                    stompCount++
                    if (stompCount == 2) isRun = true
                    fallingMeatAttack()
                    inversePath(TimeUnit.SECONDS.toTicks(15).toInt())
                    stop()
                    return
                } else if (room.raid.hardMode && ticks % 3 == 0 && Utils.randomBoolean()) {
                    fallingMeatAttack()
                }
                ticks++
            }
        }, delay, 0)
    }

    fun fallingMeatAttack() {
        WorldTasksManager.schedule(object : TickTask() {
            var cycles = Utils.random(7, 12)
            var meatLocations: MutableList<Location> = ArrayList(16)
            override fun run() {
                if (room.players.isEmpty() || isDead || isFinished) {
                    stop()
                    return
                }

                if (ticks == 5) {
                    ticks = 0
                    cycles--
                }

                if (ticks == 0) {
                    if (cycles <= 0) {
                        stompAttack(0)
                        stop()
                        return
                    } else {
                        meatLocations.clear()
                        while (meatLocations.size < 16) {
                            val point = room.randomMeatPoint
                            if (!previousMeatLocations.contains(point) && !meatLocations.contains(point)) {
                                meatLocations.add(point)
                            }
                        }
                        for (point in meatLocations)
                            World.sendGraphics(Graphics(1570 + Utils.randomNoPlus(4)), point)
                        previousMeatLocations = ArrayList(meatLocations)
                    }
                } else if (ticks == 3) {
                    for (p in room.validTargets) {
                        p.sendSound(fallingMeatSound)
                        for (point in meatLocations) {
                            if (p.location.matches(point)) {
                                delayHit(-1, p, Hit(this@PestilentBloat, Utils.random(30, 50), HitType.REGULAR))
                                p.stun(3)
                                p.graphics = stunGfx
                            }
                        }
                    }
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun attack(target: Entity) = 0

    companion object {

        private val fliesProjectile = Projectile(1568, 60, 10, 0, 0, 30, 0, 6)
        private val fliesImpactGfx = Graphics(1569)
        private val stompAnimation = Animation(8082)
        private val stunGfx = Graphics(254, 0, 92)
        private val fallingMeatSound = SoundEffect(3308, 15)
        private val fliesSounds = arrayOf(
            SoundEffect(3945), SoundEffect(3954), SoundEffect(4016)
        )

    }

}