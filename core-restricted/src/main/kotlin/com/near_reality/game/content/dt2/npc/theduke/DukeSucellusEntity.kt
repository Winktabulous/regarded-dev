package com.near_reality.game.content.dt2.npc.theduke

import com.near_reality.game.content.dt2.area.DukeSucellusInstance
import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
class DukeSucellusEntity(val arena: DukeSucellusInstance)
    : NPC(12166, arena.getLocation(3036, 6452), Direction.SOUTH, 0), CombatScript {

    val difficulty: DT2BossDifficulty = arena.difficulty
    var specialAttackActive: Boolean = false

    var attackCounter = 0

    val slumbering get() = id == 12166


    override fun postInit() {
        setShouldUpdateOptionsMask(true)
    }

    override fun performDefenceAnimation(attacker: Entity?) {}

    fun disturb(player: Player) {
        WorldTasksManager.schedule( object: TickTask() {
            override fun run() {
                when(ticks) {
                    0 -> {
                        setTransformation(difficulty.getTransformation())
                        player.inventory.container.findAllById(ItemId.ARDERMUSCA_POISON).values.forEach { player.inventory.container.remove(it) }
                        player.packetDispatcher.sendMusic(763)
                        setAnimation(Animation(10179))
                        setHitpoints(difficulty.getHitpoints())
                        player.hpHud.open(difficulty.getTransformation(), difficulty.getHitpoints())
                        combat = DukeCombatController(this@DukeSucellusEntity)
                        possibleTargets.add(arena.player)
                        combat.combatDelay = 5
                        aggressionDistance = 15
                        maxDistance = 200
                        isForceAggressive = true
                        attackCounter = 0
                        specialAttackActive = false
                        setAttackDistance(20)
                        arena.dukeAwakened()
                    }
                    1 ->  {
                        isForceAttackable = true
                    }
                    3 -> stop()
                }
                ticks++

            }
        }, 0, 0)
    }

    override fun postHitProcess(hit: Hit?) {
        super.postHitProcess(hit)
        arena.player.hpHud.updateValue(getHitpoints())
    }

    override fun isLocked(): Boolean {
        return id == 12166 || super.isLocked()
    }

    override fun isDead(): Boolean {
        if (slumbering) {
            return false
        }

        return super.isDead()
    }

    override fun onFinish(source: Entity?) {
        if (isFinished) {
            return
        }

        try {
            try {
                val p = source as Player
                (source as? Player)?.let { sendNotifications(it) }
                drop(p.location)
            } catch (_: Exception) { }
            isFinished = true
            routeEvent = null
            interactingWith = null
            World.updateEntityChunk(this, true)
            lastChunkId = -1
            if (!interactingEntities.isEmpty()) {
                interactingEntities.clear()
            }
        } catch (_: Throwable) { }
        reset()
        setTransformation(12166)
        combat.reset()
        arena.dukeGoodnight()
        arena.player.hpHud.close()
        World.removeNPC(this)
        spawn()
    }

    override fun isAttackable(e: Entity): Boolean {
        return !slumbering
    }

    private fun Location.safeFromMage() : Boolean {
        return this.y == arena.leftBound.y && (this.x == arena.leftBound.x || this.x == arena.leftBound.x - 1 || this.x == arena.rightBound.x || this.x == arena.rightBound.x + 1)
    }

    override fun attack(target: Entity): Int {
        if (target !is Player) {
            return 0
        }

        if (specialAttackActive) {
            target.sendDeveloperMessage("Special Attack in progress")
            return 0
        }

        if(target.location.safeFromMage()) {
            meleeAttack(target, reduced = true)
            return 5
        }

        if (!isWithinMeleeDistance(this, target)) {
            magicAttack(target)
            return 5
        }

        meleeAttack(target)
        return 5
    }

    override fun canMove(fromX: Int, fromY: Int, direction: Int): Boolean {
        return false
    }

    private fun meleeAttack(player: Player, reduced: Boolean = false) {
        if (attackCounter == 5) {
            if(!arena.passingGas) {
                if(Utils.randomBoolean())
                    DukeSpecialAttack.DeathGaze(this, player)
                else DukeSpecialAttack.GasFlare(this, player)

                if(difficulty == DT2BossDifficulty.AWAKENED)
                    DukeSpecialAttack.GasFlareEcho(this, player)
                attackCounter = (attackCounter + 1) % 6
                return
            } else {
                performStandardMelee(player, reduced)
            }
        } else {
            performStandardMelee(player, reduced)
            attackCounter = (attackCounter + 1) % 6
        }
    }

    private fun performStandardMelee(player: Player, reduced: Boolean) {
        setAnimation(Animation(10176))
        graphics = Graphics(2439)
        player.sendSound(7186)
        val denom = if(reduced) 3 else 1
        player.applyHit(difficulty.getMeleeHit(this, player, denom))
    }

    private fun magicAttack(player: Player) {
        if (attackCounter == 5) {
            if(!arena.passingGas) {
                if(Utils.randomBoolean())
                    DukeSpecialAttack.DeathGaze(this, player)
                else DukeSpecialAttack.GasFlare(this, player)
                attackCounter = (attackCounter + 1) % 6
                return
            } else {
                performStandardMagic(player)
            }
        } else {
            performStandardMagic(player)
            attackCounter = (attackCounter + 1) % 6
        }
    }

    private fun performStandardMagic(player: Player) {
        setAnimation(Animation(10176))
        val delay = World.sendProjectile(this, player, magicProjectile)
        player.scheduleHit(this, difficulty.getMagicHit(this, player), delay, false)
    }

    private val magicProjectile = Projectile(2434, 87, 25, 20, 60, 2, 32, 10)

    private fun DT2BossDifficulty.getHitpoints(): Int {
        return when(this) {
            DT2BossDifficulty.NORMAL -> 440
            DT2BossDifficulty.AWAKENED -> 1540
            DT2BossDifficulty.QUEST -> 350
        }
    }

    private fun DT2BossDifficulty.getTransformation(): Int {
        return when(this) {
            DT2BossDifficulty.NORMAL -> 12191
            DT2BossDifficulty.AWAKENED -> 12195
            DT2BossDifficulty.QUEST -> 12190
        }
    }

    private fun DT2BossDifficulty.getMeleeHit(duke: DukeSucellusEntity, player: Player, cutIn: Int = 1): Hit {
        val minHit = when(this) {
            DT2BossDifficulty.NORMAL -> 25
            DT2BossDifficulty.AWAKENED -> 30
            DT2BossDifficulty.QUEST -> 12
        }

        val maxHit = when(this) {
            DT2BossDifficulty.NORMAL -> 50
            DT2BossDifficulty.AWAKENED -> 75
            DT2BossDifficulty.QUEST -> 20
        }
        var dmgReduction = 1.0
        if(player.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) {
            dmgReduction = when (this) {
                DT2BossDifficulty.NORMAL -> 0.5
                DT2BossDifficulty.AWAKENED -> 0.3
                DT2BossDifficulty.QUEST -> 0.7
            }
        }

        val damage = (Utils.random(minHit, maxHit) * dmgReduction) / cutIn
        return Hit(duke, damage.toInt(), HitType.MELEE)
    }

    private fun DT2BossDifficulty.getMagicHit(duke: DukeSucellusEntity, player: Player, cutIn: Int = 1): Hit {
        val minHit = when(this) {
            DT2BossDifficulty.NORMAL -> 28
            DT2BossDifficulty.AWAKENED -> 35
            DT2BossDifficulty.QUEST -> 12
        }

        val maxHit = when(this) {
            DT2BossDifficulty.NORMAL -> 44
            DT2BossDifficulty.AWAKENED -> 61
            DT2BossDifficulty.QUEST -> 20
        }
        var dmgReduction = 1.0
        if(player.prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)) {
            dmgReduction = when (this) {
                DT2BossDifficulty.NORMAL -> 0.5
                DT2BossDifficulty.AWAKENED -> 0.3
                DT2BossDifficulty.QUEST -> 0.7
            }
        }

        val damage = (Utils.random(minHit, maxHit) * dmgReduction) / cutIn
        val hit = Hit(duke, damage.toInt(), HitType.MAGIC)
        hit.onLand { player.sendSound(7197) }
        return hit
    }
}


