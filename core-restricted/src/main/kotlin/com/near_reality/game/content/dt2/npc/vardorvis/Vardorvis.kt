package com.near_reality.game.content.dt2.npc.vardorvis

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.near_reality.game.content.dt2.npc.*
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.Attack
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.AutoAttack
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.DartingSpikes
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.Strangle
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.headgaze.HeadGaze
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.headgaze.HeadGazeProjectile
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.headgaze.VardorvisHead
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe.SwingingAxe
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe.SwingingAxeTask
import com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe.SwingingAxes
import com.near_reality.game.content.seq
import com.near_reality.game.content.spotanim
import com.zenyte.game.content.boss.BossRespawnTimer
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.Tinting
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.VARDORVIS
import com.zenyte.game.world.entity.npc.NpcId.VARDORVIS_12224
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions
import com.zenyte.game.world.entity.npc.combatdefs.StatType
import com.zenyte.game.world.entity.player.Player
import java.util.*
import kotlin.math.abs

/**
 * @author John J. Woloszyk / Kryeus
 * @date 5.9.2024
 */
internal class Vardorvis(
    tile: Location,
    private val instance: VardorvisInstance,
    val difficulty: DT2BossDifficulty = instance.difficulty
) : NPC(
    if (difficulty == DT2BossDifficulty.AWAKENED) VARDORVIS_12224
    else VARDORVIS,
    tile,
    true
), CombatScript {

    private var enraged: Boolean = false
    private var cooldowns = mutableMapOf<Attack, Int>()

    override fun isForceAggressive(): Boolean = true
    override fun getRespawnDelay(): Int = BossRespawnTimer.VARDORVIS.timer.toInteger()

    init {
        setMaxDistance(200)
        isForceAggressive = false
        setDamageCap(200)
        possibleTargets.add(instance.player)
        instance.player.hpHud.open(id, maxHitpoints)
        cooldowns.clear()
    }

    override fun attack(target: Entity): Int {
        val atk = determineAttack()
        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val decreaseInSpeed = if (hitpointsAsPercentage < 33) 1 else 0

        if (atk.requiredCooldownAttacks(enraged) != 0)
            cooldowns[atk] = atk.requiredCooldownAttacks(enraged)
        decreaseOtherCooldowns(atk)

        if (target is Player && target.entangled)
            return defs.attackSpeed - decreaseInSpeed

        when (atk) {
            is AutoAttack -> performAutoAttack(target)
            is DartingSpikes -> performDartingSpikes(target as Player)
            is HeadGaze -> performHeadGaze()
            is SwingingAxes -> {
                performSwingingAxes()
                performAutoAttack(target)
            }
            is Strangle -> {
                performStrangle()
                return 14
            }
        }
        return defs.attackSpeed - decreaseInSpeed
    }

    private fun performStrangle() {
        val player = instance.player
            player.sendMessage(Colour.RS_RED.wrap("Vardorvis manages to entangle you!"))
            player.sendMessage(Colour.RS_RED.wrap("You must try to run and break free!"))
            player.cancelCombat()
            player.entangled = true
            player.entangledFreedomCounter = 0
            player.tinting = Tinting(0, 6, 28, 112, 0, 600)

        val failMessage: String = Colour.RS_RED.wrap("Vardorvis drains a significant portion of your health!")
        World.sendGraphics(Graphics(2523), player.location)
        World.sendGraphics(Graphics(2527, 0, 100), player.location)
        val vardorvis = this
            vardorvis seq 10342
        schedule(object : TickTask() {
            override fun run() {
                if (!player.entangled) {
                    stop()
                    return
                }
                when (ticks++) {
                    1 -> vardorvis seq 10343
                    9 -> {
                        if (player.entangled && !isDead) {
                            instance.player.sendMessage(failMessage)
                            val dmg = if (difficulty == DT2BossDifficulty.AWAKENED) 80 else 50
                            instance.player.applyHit(Hit(dmg, HitType.TYPELESS))
                            applyHit(Hit(dmg, HitType.HEALED))
                        }
                        instance.resetEntanglement(false)
                        stop()
                        return
                    }
                }
                if (ticks % 2 == 0 && player.entangled) {
                    player spotanim 2525
                    World.sendGraphics(Graphics(2527, 0, 100), player.location)
                }
            }

            override fun stop() {
                super.stop()
                vardorvis seq 10344
                vardorvis.animation =  Animation(10345, 3)
            }

        }, 0, 0)
    }

    override fun postHitProcess(hit: Hit) {
        if (hit.source is Player)
            instance.player.hpHud.updateValue(getHitpoints())
    }

    private fun performDartingSpikes(target: Player) {
        val locs = target.getPotentialSpawnsInArena(instance, 3, 5)
        for (loc in locs)
            World.sendGraphicsObject(Graphics(2510), loc)
    }

    private fun performAutoAttack(target: Entity): Int {
        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val attDefs = defs.attackDefinitions
        if (attDefs != null) {
            val drawback = attDefs.drawbackGraphics
            if (drawback != null)
                this.graphics = drawback
        }
        val maxHit: Int = if (difficulty == DT2BossDifficulty.AWAKENED) 50 else 35
        val damage: Int = getRandomMaxHit(this, maxHit, CombatScript.MELEE, target)
        if (attDefs != null && target is Player) {
            val sound = attDefs.startSound
            if (sound != null)
                target.sendSound(sound)
        }
        val hit = Hit(this, damage, HitType.MELEE)
        delayHit(this, 0, target, hit)
        if (hit.damage > 0 && hit.isAccurate && (target is Player) && !target.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
            hit.source.applyHit(Hit(hit.damage / 2, HitType.HEALED))

        this.setAnimation(defs.attackAnim)
        return defs.attackSpeed
    }

    private fun performSwingingAxes(): Int {
        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val axes = mutableListOf<SwingingAxe>()
        val player: Player = instance.player

        instance.translatedAxes.entries
            .shuffled()
            .take(getAxeCount())
            .forEach { axes.add(SwingingAxe(instance, it.key)) }

        axes.forEach {
            it.spawn()
            it.faceLocation = instance.getLocation(Location(1129, 3418))
            it seq 10364
        }
        schedule(SwingingAxeTask(instance, player, axes), 0, 0)
        return defs.attackSpeed
    }

    private fun performHeadGaze(): Int {
        val location = instance.player.getOffsetLocationInArena(instance)
        if (location.isEmpty) return 1
        val spawn = VardorvisHead(location.get())
        spawn.spawn()
        spawn.setFaceEntity(instance.player)
        spawn seq 10348
        spawn spotanim 2520

        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val head: Optional<VardorvisHead> = Optional.of(spawn)

        HeadGazeProjectile(instance, head.get()).headGazeProjectile()

        instance.player.sendMessage(Colour.STRANGLE_WOOD_PINK.wrap("Vardorvis's head gazes upon you..."))

        schedule(2) { head.get().sendDeath() }

        return defs.attackSpeed
    }

    private fun getAxeCount(): Int =
        if (hitpointsAsPercentage < 50) 3 else 2

    private fun rateForCurrentHP(atk: Attack): Int {
        if (this.hitpointsAsPercentage <= 33) {
            enraged = true
            return atk.getEnrageRate()
        }
        if (this.hitpointsAsPercentage <= 66) {
            return atk.getSecondRate()
        }
        return atk.getFirstRate()
    }

    private fun decreaseOtherCooldowns(lastAttack: Attack) {
        val removals = mutableListOf<Attack>()
        for (cooldown in cooldowns) {
            if (cooldown.key == lastAttack)
                continue
            if (cooldown.value == 0) {
                removals.add(cooldown.key)
                continue
            }
            cooldown.setValue(cooldown.value - 1)
        }

        for (remove in removals)
            cooldowns.remove(remove)
    }

    private fun determineAttack(): Attack {
        val availableAttacks = determineAvailableAttacks()

        val attackOddsPool = mutableListOf<Attack>()
        for (atk in availableAttacks) {
            attackOddsPool.addAll(Collections.nCopies(rateForCurrentHP(atk), atk))
        }
        val currentSize = attackOddsPool.size
        attackOddsPool.addAll(Collections.nCopies(abs(100 - currentSize), AutoAttack))
        instance.debugMechanic(
            "Auto: ${attackOddsPool.filter { it == AutoAttack }.size}, " +
                    "Axes: ${attackOddsPool.filter { it == SwingingAxes }.size}, " +
                    "Gaze: ${attackOddsPool.filter { it == HeadGaze }.size}, " +
                    "Strangle: ${attackOddsPool.filter { it == Strangle }.size}, " +
                    "Spikes: ${attackOddsPool.filter { it == DartingSpikes }.size}"
        )
        if (!instance.player.location.withinDistance(this.position, 1))
            attackOddsPool.removeAll { it is AutoAttack }
        return if (attackOddsPool.isEmpty()) AutoAttack else attackOddsPool.random()
    }

    private fun determineAvailableAttacks(): List<Attack> {
        val potentialAttacks = mutableListOf(SwingingAxes, DartingSpikes, HeadGaze, Strangle)
        potentialAttacks.removeIf {
            !it.getEnabled() || it.getRequiredHp() <= this.hitpointsAsPercentage
        }

        val removals = mutableListOf<Attack>()
        for (atk in potentialAttacks) {
            if (cooldowns[atk] != null && cooldowns[atk]!! > 0)
                removals.add(atk)
        }

        for (remove in removals)
            potentialAttacks.remove(remove)
        return potentialAttacks
    }

    override fun getCombatDefinitions(): NPCCombatDefinitions {
        val defs = super.getCombatDefinitions()

        val defenseReduction = 70 - ((hitpointsAsPercentage / 100) * 70)
        val baseDefense = when (difficulty) {
            DT2BossDifficulty.NORMAL -> 215
            DT2BossDifficulty.AWAKENED -> 268
            DT2BossDifficulty.QUEST -> 180
        }

        val strengthIncrease = ((hitpointsAsPercentage / 100) * 70)
        val baseStrength = when (difficulty) {
            DT2BossDifficulty.NORMAL -> 270
            DT2BossDifficulty.AWAKENED -> 391
            DT2BossDifficulty.QUEST -> 210
        }

        defs.statDefinitions.set(StatType.DEFENCE, baseDefense - defenseReduction)
        defs.statDefinitions.set(StatType.STRENGTH, baseStrength + strengthIncrease)
        return defs
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)
        instance.killAxes = true
        instance.killBleed = true
        instance.player.entangled = false
        instance.player.tinting = Tinting(-1, -1, -1, 0, 0, 0)
        cooldowns.clear()
        setRespawnTask()
    }
}