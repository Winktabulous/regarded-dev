package com.near_reality.game.content.araxxor

import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.araxxor.araxytes.impl.AcidicAraxyte
import com.near_reality.game.content.araxxor.araxytes.impl.MirrorbackAraxyte
import com.near_reality.game.content.araxxor.araxytes.impl.RupturaAraxyte
import com.near_reality.game.content.araxxor.attacks.impl.CleaveAttack
import com.near_reality.game.content.araxxor.attacks.impl.MagicAttack
import com.near_reality.game.content.araxxor.attacks.impl.MeleeAttack
import com.near_reality.game.content.araxxor.attacks.impl.RangedAttack
import com.near_reality.game.content.araxxor.attacks.spec.AcidBall
import com.near_reality.game.content.araxxor.attacks.spec.AcidDrip
import com.near_reality.game.content.araxxor.attacks.spec.AcidSplatter
import com.near_reality.game.content.seq
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.NotificationSettings
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
data class Araxxor(
    val instance: AraxxorInstance,
    var spawnLocation: Location?,
) : NPC(
    ARAXXOR,
    spawnLocation,
    true
), CombatScript {

    var primaryAraxyte: Araxyte? = null
    var enraged: Boolean = false
    private var minionIndex: Int = 0

    private fun getAttackSpeed(): Int = if (enraged) 4 else 6

    var attackCounter: Int = 0
    private var isHatchTick: Boolean = true

    init {
        aggressionDistance = 64
        isForceAggressive = true
    }

    override fun isEntityClipped(): Boolean = false

    private fun specialAttack(target: Entity) {
        isHatchTick = true
        when (primaryAraxyte) {
            is AcidicAraxyte -> AcidBall(instance).invoke(this, target)
            is MirrorbackAraxyte -> AcidSplatter(instance).invoke(this, target)
            is RupturaAraxyte -> AcidDrip(instance).invoke(this, target)
        }
    }

    private fun hatchSpiderEgg(target: Entity) {
        isHatchTick = false
        if (instance.araxytes.size > 0 && minionIndex < 9) {
            val hatching = instance.araxytes[minionIndex]
            if (!hatching.isDead || !hatching.isFinished) {
                hatching.hatchEgg(target)
            }
            minionIndex++
        }
    }

    override fun attack(target: Entity?): Int {
        if (target == null) return getAttackSpeed()
        if (attackCounter++ % 3 == 0 && attackCounter > 2) {
            if (isHatchTick) hatchSpiderEgg(target)
            else if (!enraged) {
                specialAttack(target)
                return getAttackSpeed()
            }
        }

        if (this.middleLocation.withinDistance(target, 4))
            if (enraged)
                CleaveAttack().invoke(this, target)
            else
                MeleeAttack().invoke(this, target)
        else if (attackingWithRange(target))
            RangedAttack().invoke(this, target)
        else
            MagicAttack().invoke(this, target)
        return getAttackSpeed()
    }

    override fun handleIngoingHit(hit: Hit?) {
        super.handleIngoingHit(hit)
        if (hit == null) return
        val minions = instance.araxytes
        minions.forEachIndexed { index, araxyte ->
            if (araxyte.id == MIRRORBACK_ARAXYTE) {
                val mirrorBack = minions[index]
                if (hit.damage > 1) {
                    val hitBack = hit.damage / 2
                    mirrorBack.applyHit(Hit(hit.source, hitBack, HitType.DEFAULT))
                }
            }
        }
    }

    override fun processNPC() {
        super.processNPC()
        if (attacking != null && attacking is Player) {
            if ((attacking as Player).hpHud != null && (attacking as Player).hpHud.isOpen)
                (attacking as Player).hpHud.updateValue(getHitpoints())
        }
        val targetHp = (0.25 * maxHitpoints).toInt()
        if (hitpoints < targetHp && !enraged) {
            lock()
            enraged = true
            this seq 11488
            schedule(1) {
                this seq 11489
                // TODO: assume npc switches a 'varbit' to change legs to envenomed legs
                unlock()
            }
        }
    }

    override fun sendDeath() {
        // Remove acid pools
        if (instance.poolObjects.isNotEmpty()) {
            instance.poolObjects.forEach(World::removeObject)
            instance.poolObjects.clear()
        }
        // Remove remaining Araxytes
        if (instance.araxytes.isNotEmpty()) {
            instance.araxytes.forEach(NPC::remove)
            instance.araxytes.clear()
        }
        setAnimation(getCombatDefinitions().deathAnim)
        schedule(5) {
            if (attacking != null && attacking is Player) {
                val player = (attacking as Player)
                player.hpHud.close()
                if (NotificationSettings.isKillcountTracked(name)) {
                    player.notificationSettings.increaseKill(name)
                    if (NotificationSettings.BOSS_NPC_NAMES.contains(name.lowercase(Locale.getDefault())))
                        player.notificationSettings.sendBossKillCountNotification(name)
                }
                player.slayer.checkAssignment(this)
                player.bossTimer.finishTracking("Araxxor")
            }
            setTransformationPreservingStats(ARAXXOR_CORPSE)
        }
    }

    override fun setRespawnTask() {}

    fun resetInstance() {
        enraged = false
        minionIndex = 0
        attackCounter = 0
        isHatchTick = true
        instance.poolObjects.forEach(World::removeObject)
        instance.poolObjects.clear()
        instance.npcs.forEach(NPC::remove)
        instance.npcs.clear()
        instance.araxytes.forEach(NPC::remove)
        instance.araxytes.clear()
    }

    private fun attackingWithRange(target: Entity): Boolean {
        if (target is Player) {
            val mageDef = target.bonuses.getBonus(Bonuses.Bonus.DEF_MAGIC)
            val rangeDef = target.bonuses.getBonus(Bonuses.Bonus.DEF_RANGE)
            return rangeDef < mageDef
        }
        return false;
    }

}