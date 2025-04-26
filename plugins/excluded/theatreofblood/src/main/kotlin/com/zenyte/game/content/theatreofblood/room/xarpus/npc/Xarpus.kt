package com.zenyte.game.content.theatreofblood.room.xarpus.npc

import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.xarpus.XarpusRoom
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.calog.CAType
import com.zenyte.game.world.`object`.WorldObject
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList

/**
 * @author Jire
 * @author Tommeh
 */
internal class Xarpus(room: XarpusRoom) :
    TheatreBossNPC<XarpusRoom>(room, NpcId.XARPUS, room.getLocation(3169, 4386, XarpusRoom.PLANE)),
    CombatScript {

    init {
        maxDistance = 64
        radius = 0
    }

    var ticks = 0

    val poisonSplats: ObjectList<WorldObject> = ObjectArrayList()

    var phase: XarpusPhase = XarpusExhumedPhaseFactory.getPhase(this, room.raid.party.size)
    var healedAmount = 0
    var meleeOnly = true

    override fun spawn(): NPC {
        super.spawn()
        phase.onPhaseStart()
        return this
    }

    override fun processNPC() {
        phase = phase.process()
        if (phase is XarpusPoisonPhase || phase is XarpusCounterPhase)
            processMechanics()
        super.processNPC()
        ticks++

    }

    private fun processMechanics() {
        val players = room.validTargets
        for (player in players) {
            for (poisonSplat in poisonSplats) {
                if (player.location == poisonSplat)
                    XarpusPoisonPhase.poison(this, player)
            }

            if (player.location.withinDistance(middleLocation, 2)) {
                World.sendProjectile(middleLocation, player, rockProjectile)
                World.sendProjectile(middleLocation, player, rockProjectile2)
                player.scheduleHit(this, Hit(this, Utils.random(2, 8), HitType.REGULAR), 1)
                player.scheduleHit(this, Hit(this, Utils.random(2, 8), HitType.REGULAR), 1)
            }
        }
    }

    override fun faceEntity(entity: Entity?) {
        if (phase is XarpusCounterPhase) return

        super.faceEntity(entity)
    }

    override fun setFaceEntity(entity: Entity?) {
        if (phase is XarpusCounterPhase) {
            super.setFaceEntity(null)
            return
        }
        super.setFaceEntity(entity)
    }

    fun hitsplatHeal(amount: Int) {
        if (hitpoints >= maxHitpoints) return

        applyHit(Hit(amount, HitType.HEALED))
    }

    override fun setTransformation(id: Int) {
        val currentHitpoints = hitpoints
        val currentMaxHp = maxHpScaled

        super.setTransformation(id)

        maxHpScaled = currentMaxHp
        if (hitpoints != currentHitpoints) {
            hitpoints = currentHitpoints
        }
    }

    override fun removeHitpoints(hit: Hit) {
        if (phase is XarpusCounterPhase)
            (phase as XarpusCounterPhase).counter(hit)

        if (hit.hitType == HitType.RANGED || hit.hitType == HitType.MAGIC) {
            meleeOnly = false
        }

        super.removeHitpoints(hit)
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        if (meleeOnly) {
            val players = room.validTargets
            for (player in players) {
                player.combatAchievements.complete(CAType.CAN_YOU_DANCE)
            }
        }
    }

    override fun attack(target: Entity) = 0

    override fun addWalkStep(nextX: Int, nextY: Int, lastX: Int, lastY: Int, check: Boolean) = false

    override fun checkProjectileClip(player: Player, melee: Boolean) = false

    companion object {
        val rockProjectile = Projectile(1557, 40, 20, 0, 0, 30, 0, 0)
        val rockProjectile2 = Projectile(1557, 40, 20, 15, 0, 30, 0, 0)
    }

}