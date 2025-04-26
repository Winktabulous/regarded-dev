package com.near_reality.game.content.tormented_demon

import com.near_reality.game.content.tormented_demon.attacks.Attack
import com.near_reality.game.content.tormented_demon.attacks.impl.FireBomb
import com.near_reality.game.content.tormented_demon.attacks.impl.MagicAttack
import com.near_reality.game.content.tormented_demon.attacks.impl.MeleeSlash
import com.near_reality.game.content.tormented_demon.attacks.impl.RangedAttack
import com.near_reality.game.util.Ticker
import com.near_reality.game.world.entity.player.tormentedDemonAccuracyBoost
import com.zenyte.game.content.boss.BossRespawnTimer
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.Item
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.util.DirectionUtil
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.EntityHitBar
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.world.entity.npc.NpcOverhead
import com.zenyte.game.world.entity.npc.Spawnable
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-12
 */
class TormentedDemon(
    id: Int,
    spawnLocation: Location?,
    direction: Direction,
    radius: Int
) : NPC(
    id,
    spawnLocation,
    direction,
    radius
), CombatScript,
   Spawnable {

    private val DAMAGE_THRESHOLD: Int = 150
    private var ticker: Int = 0
    private var damageCounter: Int = 0
    private var attackCounter: Int = 1
    private var activePrayer: Prayer? = null
    val accuracyBoostTimer: Ticker = Ticker(25)
    private var fireShieldActive: Boolean = true
    private var activeAttackStyle: Attack = MeleeSlash()

    fun getMagicSpellAnimation(): Int = 11388
    fun getMeleeSlashAnimation(): Int = 11392
    fun getRangeAttackAnimation(): Int = 11389
    fun getFireSkullThrowAnimation(): Int = 11387

    private fun getFireShield(): Int = 2849
    private fun getFireShieldStart(): Int = 2850
    private fun getFireShieldRemove(): Int = 2852

    override fun getRespawnDelay(): Int = BossRespawnTimer.TORMENTED_DEMON.timer.toInt()

    override fun isEntityClipped(): Boolean = true

    override fun isTolerable(): Boolean = false

    override fun validate(id: Int, name: String): Boolean =
        id == TORRMENTED_DEMON || id == TORRMENTED_DEMON_13600 || id == TORRMENTED_DEMON_13601 || id == TORRMENTED_DEMON_13602

    override fun spawn(): NPC {
        val npc = super.spawn()
        temporaryAttributes["npc_overhead_prayers"] = true
        setPrayer(Prayer.PROTECT_FROM_MELEE)
        return npc
    }

    init {
        aggressionDistance = 16
        attackDistance = 16

        hitBar = object : EntityHitBar(this) {
            override fun getType(): Int {
                return 20
            }
        }
    }

    private fun switchPrayer(lastHit: Hit) {
        val type = lastHit.hitType
        when(type) {
            HitType.MELEE -> setPrayer(Prayer.PROTECT_FROM_MELEE)
            HitType.MAGIC -> setPrayer(Prayer.PROTECT_FROM_MAGIC)
            HitType.RANGED -> setPrayer(Prayer.PROTECT_FROM_MISSILES)
            else -> {}
        }
    }

    private fun setPrayer(prayer: Prayer) {
        activePrayer = prayer
        when(prayer) {
            Prayer.PROTECT_FROM_MELEE -> { overhead = NpcOverhead.MELEE }
            Prayer.PROTECT_FROM_MAGIC -> { overhead = NpcOverhead.MAGE }
            Prayer.PROTECT_FROM_MISSILES -> { overhead = NpcOverhead.RANGE }
            else -> {}
        }
        resetAttackedByDelay()
    }

    private fun toggleFireShield() {
        fireShieldActive = !fireShieldActive
    }

    private fun switchAttack(target: Entity) {
        isForceFollowClose = false
        // Random chance to Melee
        if (Random.nextDouble() < 0.33)
            isForceFollowClose = true

        val newStyle =
            if (isForceFollowClose) MeleeSlash()
            else if (Random.nextBoolean()) MagicAttack()
            else RangedAttack()
        if (newStyle == activeAttackStyle)
            switchAttack(target)
        else
            activeAttackStyle = newStyle
    }

    override fun attack(target: Entity): Int {
        val demon = this
        if (attackCounter++ % 10 == 0) {
            FireBomb().invoke(demon, target)
            toggleFireShield()
            graphics = Graphics(getFireShieldRemove())
            switchAttack(target)
            return 6
        }
        if (!fireShieldActive) {
            graphics = Graphics(getFireShieldStart())
            schedule(2) { toggleFireShield() }
        }
        if (getAttack() is MeleeSlash) {
            if (!this.middleLocation.withinDistance(target.location, 2)) {
                switchAttack(target)
                return 6
            }
        }
        getAttack().invoke(demon, target)
        return 6
    }

    private fun getAttack(): Attack {
        return activeAttackStyle
    }

    override fun processNPC() {
        super.processNPC()
        accuracyBoostTimer.tick()

        if (accuracyBoostTimer.finished) {
            val target = attackedBy
            if (target is Player)
                target.tormentedDemonAccuracyBoost = true
        }
        if (fireShieldActive && ticker++ % 2 == 0)
            graphics = Graphics(getFireShield())
    }

    override fun handleIngoingHit(hit: Hit?) {
        if (hit == null) return
        val weapon = hit.weapon
        val type = hit.hitType
        if (weapon != null && "Dwarf Multicannon" == weapon.toString())
            hit.damage = 0
        when(type) {
            HitType.MELEE -> if (activePrayer == Prayer.PROTECT_FROM_MELEE) hit.damage = 0
            HitType.MAGIC -> if (activePrayer == Prayer.PROTECT_FROM_MAGIC) hit.damage = 0
            HitType.RANGED -> if (activePrayer == Prayer.PROTECT_FROM_MISSILES) hit.damage = 0
            else -> {}
        }
        val attacker = hit.source
        if (attacker is Player) {
            if (hit.damage > 0)
                damageCounter += hit.damage
            if (damageCounter >= DAMAGE_THRESHOLD) {
                switchPrayer(hit)
                damageCounter = 0
                switchAttack(attacker)
            }
            if (fireShieldActive)
                if (weapon is Item && (!weapon.isDemonbaneWeapon || !weapon.isAbyssalWeapon))
                    hit.damage = (hit.damage * 0.8).toInt()
            else {
                if (weapon is Item && isApplicableWeapon(weapon))
                    hit.damage = getBoostedDamage(hit.damage, attacker.attackingDelay)
            }
        }
        super.handleIngoingHit(hit)
    }

    private fun getBoostedDamage(baseDamage: Int, attackSpeed: Long): Int {
        val damageBoost = sqrt(attackSpeed.toDouble()) - 16
        return (baseDamage + damageBoost).toInt()
    }

    private fun isApplicableWeapon(weapon: Item?): Boolean {
        if (weapon == null) return false
        if (weapon.name.contains("crossbow", true)) return true
        if (weapon.name.contains("ballistae", true)) return true
        if (weapon.definitions.bonuses[2] > 0) return true
        return false
    }
}