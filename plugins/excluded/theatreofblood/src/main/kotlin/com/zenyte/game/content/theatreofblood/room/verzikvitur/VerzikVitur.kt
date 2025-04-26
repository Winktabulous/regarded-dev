package com.zenyte.game.content.theatreofblood.room.verzikvitur

import com.zenyte.game.content.theatreofblood.awardMostDamageContributionPointsToMVPForPhase
import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.verzikvitur.first.firstPhase
import com.zenyte.game.content.theatreofblood.room.verzikvitur.second.secondPhase
import com.zenyte.game.content.theatreofblood.room.verzikvitur.second.switchToSecondPhase
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasMatomenos
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells.DefaultPassiveSpell
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.processThirdPhase
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.switchToThirdPhase
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.thirdPhase
import com.zenyte.game.content.theatreofblood.subtractContributionPointsAfterVerzik
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.CombatScriptsHandler
import com.zenyte.game.world.entity.npc.NPCCombat
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
internal class VerzikVitur(room: VerzikViturRoom) :
    TheatreBossNPC<VerzikViturRoom>(room, VerzikViturPhase.NONE.npcID, room.getBaseLocation(30, 35)),
    CombatScript {

    var phase = VerzikViturPhase.NONE

    var lastShield = -1L

    fun shielded() = lastShield >= 0 && WorldThread.WORLD_CYCLE - lastShield < 9

    var passiveSpell: PassiveSpell = DefaultPassiveSpell

    var spawnedTornados = false
    var attackSpeed = 7

    var ticks = 0
    var attackCounter = -1
    var electricAttackCounter = 0
    var healPhase = false
    var crabs = ArrayList< NylocasMatomenos>(2)
    var firstHit = true
    var previousTarget: Entity? = null

    val webLocs = HashSet<Location>()

    init {
        hitBar = VerzikViturHitBar(this)
        radius = 64
        maxDistance = 64
        combat = object : NPCCombat(this) {
            override fun combatAttack(): Int {
                if (target == null) {
                    return 0
                }
                val melee = isMelee
                if (npc.isProjectileClipped(target, melee)) {
                    return 0
                }
                addAttackedByDelay(target)
                return CombatScriptsHandler.specialAttack(npc, target)
            }
        }
    }

    fun switchPhase(nextPhase: VerzikViturPhase) {
        phase = nextPhase
        setTransformation(nextPhase.npcID)
        heal(maxHitpoints)
    }

    override fun attack(target: Entity) = when (phase) {
        VerzikViturPhase.THIRD -> {
            thirdPhase(target)
            attackSpeed
        }
        else -> 0
    }

    override fun processNPC() {
        super.processNPC()

        when (phase) {
            VerzikViturPhase.FIRST -> firstPhase()
            VerzikViturPhase.SECOND -> secondPhase()
            VerzikViturPhase.THIRD -> processThirdPhase()
            else -> {}
        }
    }

    override fun sendDeath() {
        when (phase) {
            VerzikViturPhase.SECOND -> switchToThirdPhase()
            VerzikViturPhase.THIRD -> {
                room.awardMostDamageContributionPointsToMVPForPhase()
                room.subtractContributionPointsAfterVerzik()
                phase = VerzikViturPhase.NONE
                for(m in room.raid.party.members) {
                    val p = World.getPlayer(m)
                    if(p.isPresent) {
                        p.get().blockIncomingHits(15)
                    }
                }
                val spawnDefinitions = combatDefinitions.spawnDefinitions
                setAnimation(spawnDefinitions.deathAnimation)
                WorldTasksManager.schedule({
                    setAnimation(Animation.STOP)
                    setTransformation(8375)
                    WorldTasksManager.schedule({
                        onFinish(null)
                    }, 5)
                }, 3)
            }
            else -> switchToSecondPhase()
        }
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        room.complete()
    }

    override fun canDrainSkill(skill: Int): Boolean {
        if (phase == VerzikViturPhase.SECOND) {
            return false
        }

        return super.canDrainSkill(skill)
    }

    override fun isMovableEntity() = phase == VerzikViturPhase.THIRD || id == TRANSFORM_INTO_SECOND_PHASE_ID

    override fun faceEntity(target: Entity?) {
        if (target == null || phase == VerzikViturPhase.THIRD)
            super.faceEntity(target)
    }

    fun attackRandomTarget() {
        val target = Utils.random(room.validTargets.filter { it != previousTarget }) ?: return
        setTarget(target)
    }

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)

        hit.setExecuteIfLocked()
        if (phase == VerzikViturPhase.FIRST) {
            if (hit.hitType == HitType.MELEE && hit.damage > 10) {
                hit.damage = 10
            }
            if ((hit.hitType == HitType.RANGED || hit.hitType == HitType.MAGIC) && hit.damage > 3) {
                hit.damage = 3
            }

            if (hit.hitType != HitType.SHIELD) {
                hit.hitType = HitType.SHIELD
            }
        } else {
            if (hit.hitType == HitType.SHIELD) {
                hit.damage = 0
            }

            if (phase == VerzikViturPhase.SECOND) {
                if (shielded()) {
                    hit.hitType = HitType.HEALED;
                }
            }
        }
    }

    override fun getMagicPrayerMultiplier(): Double {
        if (phase == VerzikViturPhase.FIRST) {
            return 0.5
        } else if (phase == VerzikViturPhase.SECOND) {
            return 0.0
        }

        return super.getMagicPrayerMultiplier()
    }

    override fun getRangedPrayerMultiplier(): Double {
        if (phase == VerzikViturPhase.SECOND) {
            return 0.5
        }

        return super.getRangedPrayerMultiplier()
    }

    override fun checkProjectileClip(player: Player?, melee: Boolean): Boolean {
        return !melee
    }

    override fun isForceAggressive(): Boolean {
        if (phase == VerzikViturPhase.THIRD) {
            return true
        }

        return super.isForceAggressive()
    }

    override fun setTransformation(id: Int) {
        val currentHitpoints = hitpoints

        super.setTransformation(id)

        if (hitpoints != currentHitpoints) {
            hitpoints = currentHitpoints
        }
    }

    override fun autoRetaliate(source: Entity?) {
        if (phase == VerzikViturPhase.THIRD) {
            return
        }
        
        super.autoRetaliate(source)
    }

    override fun isFreezeImmune(): Boolean {
        return true
    }

    override fun isValidAnimation(animID: Int) = true

    companion object {
        const val TRANSFORM_INTO_SECOND_PHASE_ID = 8371
    }

}
