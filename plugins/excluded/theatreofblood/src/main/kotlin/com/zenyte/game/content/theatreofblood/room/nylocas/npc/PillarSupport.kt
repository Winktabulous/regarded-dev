package com.zenyte.game.content.theatreofblood.room.nylocas.npc

import com.near_reality.game.world.entity.TargetSwitchCause
import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.nylocas.NylocasRoom
import com.zenyte.game.content.theatreofblood.room.nylocas.model.PillarLocation
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.EntityHitBar
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.npc.NPCCombat
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Tommeh
 * @author Jire
 */
internal class PillarSupport(
    room: NylocasRoom,
    var type: PillarLocation
) : TheatreNPC<NylocasRoom>(room, 8358, room.getLocation(type.location), Direction.SOUTH) {

    val worldObject = WorldObject(32863, 10, type.rotation, room.getLocation(type.location))

    init {
        hitBar = PillarSupportHitBar(this)
        combat = object : NPCCombat(this) {
            override fun setTarget(
                target: Entity,
                cause: TargetSwitchCause
            ) {}
            override fun forceTarget(target: Entity) {}
        }
        setTargetType(Entity.EntityType.NPC)
    }

    override fun setStats() {
        maxHpScaled = 500
        setHitpoints(maxHpScaled)
    }

    override fun removeHitpoints(hit: Hit) {
        super.removeHitpoints(hit)
        room.refreshHealthBar()
    }

    override fun heal(amount: Int) {
        super.heal(amount)
        room.refreshHealthBar()
    }

    override fun processHit(hit: Hit) {
        //if (isDead()) return
        if (isImmune(hit.hitType)) hit.damage = 0
        if (hit.damage > Short.MAX_VALUE) hit.damage = Short.MAX_VALUE.toInt()
        if (hit.damage > getHitpoints()) hit.damage = getHitpoints()
        getUpdateFlags().flag(UpdateFlag.HIT)
        addHitbar()
        if (hit.hitType == HitType.HEALED) heal(hit.damage)
        else removeHitpoints(hit)
        postHitProcess(hit)
    }

    override fun reset() {
        receivedHits.clear()
        walkSteps.clear()
        toxins.reset()
        receivedDamage.clear()
        hitBars.clear()
        nextHits.clear()
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        for (p in room.validTargets)
            p.applyHit(Hit(this, Utils.random(20, 35), HitType.REGULAR))
        World.sendObjectAnimation(worldObject, crumbleAnimation)
        room.pillars.remove(type)
        if (room.pillars.isNotEmpty()) return
        room.raid.activityFailed()
    }

    override fun isMovableEntity() = false

    private class PillarSupportHitBar(entity: Entity) : EntityHitBar(entity) {
        public override fun getSize() = 5
    }

    companion object {

        val crumbleAnimation = Animation(8074)

    }

}
