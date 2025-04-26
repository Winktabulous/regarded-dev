package com.zenyte.game.content.theatreofblood.room.nylocas

import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.room.HealthBarType
import com.zenyte.game.content.theatreofblood.room.JailLocation
import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.content.theatreofblood.room.TheatreRoomType
import com.zenyte.game.content.theatreofblood.room.nylocas.model.*
import com.zenyte.game.content.theatreofblood.room.nylocas.npc.NylocasVasilias
import com.zenyte.game.content.theatreofblood.room.nylocas.npc.PillarSupport
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.plugins.HitProcessPlugin
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import java.util.function.Predicate

/**
 * @author Tommeh
 * @author Jire
 */
internal class NylocasRoom(raid: TheatreOfBloodRaid, area: AllocatedArea, room: TheatreRoomType) :
    TheatreRoom(raid, area, room), HitProcessPlugin {

    override var boss: TheatreBossNPC<out TheatreRoom>? = NylocasVasilias(this)

    val pillars: MutableMap<PillarLocation, PillarSupport> = HashMap(4)
    var phase: NylocasPhase? = NylocasPhase.BOSS
    var ticks = 0

    init {
        for (location in PillarLocation.values) pillars[location] = PillarSupport(this, location)
    }

    override fun onStart(player: Player) {
        for (pillar in pillars.values) {
            pillar.spawn()
            World.spawnObject(pillar.worldObject)
        }
    }

    override fun process() {
        if (!started) return
        if (ticks >= 8) {
            if (ticks % 5 == 0) {
                if (phase != null) {
                    val boss = boss!!
                    boss.spawn()
                    boss.lock()
                    refreshHealthBar()
                    WorldTasksManager.schedule({
                        boss.unlock()
                        boss.setTransformation(NylocasType.MELEE.id)
                        boss.animation = Animation.STOP
                        boss.setTarget(Utils.random(validTargets))
                        boss.combat.combatDelay = 4
                        boss.heal(boss.maxHitpoints)
                        if (boss is NylocasVasilias) {
                            boss.ticks = -1
                        }
                    }, 2)
                }
                phase = null
            }
        }
        ticks++
    }

    override fun hit(source: Player, target: Entity, hit: Hit, modifier: Float): Boolean {
        if (target is NylocasVasilias) {
            val type = target.type
            if (type != null && hit.hitType != type.acceptableHitType) {
                target.applyHit(Hit(hit.damage, HitType.HEALED))
                target.delayHit(-1, source, Hit(target, hit.damage, HitType.REGULAR))
                hit.predicate = Predicate { true }
                return false
            }
        }
        return true
    }


    override val currentHitpoints: Int
        get() {
            if (phase == null || phase == NylocasPhase.BOSS) return boss!!.hitpoints
            return 0
        }
    override val maximumHitpoints: Int
        get() {
            if (phase == null || phase == NylocasPhase.BOSS) return boss!!.maxHitpoints
            return 1
        }

    override val entranceLocation: Location = getLocation(3295, 4283, 0)
    override val vyreOrator = WorldObject(ObjectId.VYRE_ORATOR, 11, 0, getLocation(3296, 4262, 0))
    override val spectatingLocation: Location = getLocation(3290, 4257, 0)

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = player.y > barrier.y

    override val healthBarType = HealthBarType.REGULAR

    override var nextRoomType : TheatreRoomType? = TheatreRoomType.SOTETSEG

    override val jailLocations = Companion.jailLocations

    companion object {
        private val jailLocations = arrayOf(
            JailLocation(26, 33),
            JailLocation(23, 30),
            JailLocation(23, 19),
            JailLocation(40, 19)
        )

    }

}