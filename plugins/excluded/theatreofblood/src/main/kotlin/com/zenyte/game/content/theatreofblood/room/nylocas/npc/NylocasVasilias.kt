package com.zenyte.game.content.theatreofblood.room.nylocas.npc

import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.nylocas.NylocasRoom
import com.zenyte.game.content.theatreofblood.room.nylocas.model.NylocasType
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combat.CombatScript

/**
 * @author Tommeh
 * @author Jire
 */
internal class NylocasVasilias(room: NylocasRoom) :
    TheatreBossNPC<NylocasRoom>(room, NpcId.NYLOCAS_VASILIAS, room.getLocation(3294, 4247)),
    CombatScript {

    var type: NylocasType?
        private set
    var ticks = 0

    init {
        type = NylocasType.MELEE
        setDeathDelay(3)
        isIntelligent = true
        setAnimation(spawnAnimation)
    }

    override fun sendDeath() {
        val source = mostDamagePlayerCheckIronman
        onDeath(source)
        WorldTasksManager.schedule(object : TickTask() {
            override fun run() {
                if (ticks == 0) {
                    setAnimation(type!!.explosionAnimation)
                }
                if (ticks == 2) {
                    onFinish(source)
                    stop()
                    return
                }
                ticks++
            }
        }, 0, 1)
    }

    override fun getXpModifier(hit: Hit) = if (type != null && hit.hitType != type!!.acceptableHitType) 0F else 1F

    override fun setId(id: Int) {
        super.setId(id)
        type = NylocasType[id]
    }

    override fun setTransformation(id: Int) {
        val currentHitpoints = hitpoints

        super.setTransformation(id)

        if (hitpoints != currentHitpoints) {
            hitpoints = currentHitpoints
        }
    }

    override fun getRangedPrayerMultiplier() = 0.25

    override fun getMagicPrayerMultiplier() = 0.25

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)
        if (hit.source != null)
            setTarget(hit.source)
    }

    private fun transform() {
        val types = NylocasType.values.toMutableList()
        types.remove(type)
        val random = types[Utils.random(types.size - 1)]
        type = random
        setAnimation(Animation.STOP)
        setTransformation(type!!.id)
        combat.combatDelay = 4
        for (p in room.validTargets)
            p.cancelCombat()
        ticks = 0
    }

    override fun processNPC() {
        super.processNPC()

        if (combat.target == null) return

        ticks++
        if (!isDead && !isFinished && ticks > 0 && ticks % 10 == 0) {
            transform()
        }
    }

    override fun attack(target: Entity): Int {
        when (type) {
            NylocasType.MELEE -> {
                setAnimation(meleeAnimation)
                delayHit(
                    0,
                    target,
                    Hit(
                        this,
                        getRandomMaxHit(
                            this,
                            if (room.raid.hardMode) 99 else combatDefinitions.maxHit,
                            CombatScript.STAB,
                            target
                        ), HitType.MELEE
                    )
                )
            }
            NylocasType.MAGIC -> {
                setAnimation(magicAnimation)
                delayHit(
                    World.sendProjectile(this, target, magicProjectile),
                    target,
                    Hit(
                        this,
                        getRandomMaxHit(
                            this,
                            if (room.raid.hardMode) 99 else combatDefinitions.maxHit,
                            CombatScript.MAGIC,
                            target
                        ),
                        HitType.MAGIC
                    )
                )
            }
            NylocasType.RANGED -> {
                setAnimation(rangedAnimation)
                delayHit(
                    World.sendProjectile(this, target, rangedProjectile),
                    target,
                    Hit(
                        this,
                        getRandomMaxHit(
                            this,
                            if (room.raid.hardMode) 99 else combatDefinitions.maxHit,
                            CombatScript.RANGED,
                            target
                        ),
                        HitType.RANGED
                    )
                )
            }
            else -> throw IllegalStateException()
        }
        return combatDefinitions.attackSpeed
    }

    companion object {

        private val spawnAnimation = Animation(8075)
        private val magicAnimation = Animation(7990)
        private val meleeAnimation = Animation(8004)
        private val rangedAnimation = Animation(8001)
        private val magicProjectile = Projectile(1610, 45, 25, 30, 10, 30, 0, 1)
        private val rangedProjectile = Projectile(1561, 30, 20, 30, 10, 30, 0, 5)

    }

}