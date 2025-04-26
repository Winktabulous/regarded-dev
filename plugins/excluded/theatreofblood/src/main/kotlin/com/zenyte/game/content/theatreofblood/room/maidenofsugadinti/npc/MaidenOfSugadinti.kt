package com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.npc

import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiPhase
import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiPhase.Companion.appropriateNewPhase
import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiRoom
import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.`object`.BloodTrail
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.DirectionUtil
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.*
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Bonuses.Bonus
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.`object`.WorldObject
import it.unimi.dsi.fastutil.ints.IntArraySet
import it.unimi.dsi.fastutil.ints.IntSet
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import kotlin.math.ceil
import kotlin.math.max

/**
 * @author Jire
 * @author Tommeh
 */
internal class MaidenOfSugadinti(room: MaidenOfSugadintiRoom) :
    TheatreBossNPC<MaidenOfSugadintiRoom>(
        room,
        MaidenOfSugadintiPhase.FIRST.npcId,
        room.getLocation(spawnLocation),
        Direction.EAST
    ), CombatScript {

    private val stormDirections: ObjectList<Location> = ObjectArrayList(stormDirectionPoints.size)
    private val bloodSplatTargets: IntSet = IntArraySet() // where projectiles are about to hit

    private val bloodSplatTiles: IntSet = IntArraySet() // where persistent splats are

    val bloodTrails: MutableList<BloodTrail> = ObjectArrayList()

    private val bloodSpawns: MutableList<BloodSpawn> = ObjectArrayList()
    private var ticks = 0
    private var currentPhase = MaidenOfSugadintiPhase.FIRST
    private var maxHit = MAX_HIT.toDouble()
    private var maxBloodSplatHit = BLOOD_SPLAT_MAX_HIT.toDouble()
    private var hasUsedStormAttack = false
    private var canRollBloodAttack = true // can only roll blood attack after 2 storm attacks

    private var attackInterval = TICKS_PER_ATTACK

    init {
        maxDistance = 64
        radius = 0

        for (point in stormDirectionPoints)
            stormDirections.add(room.getLocation(point))
    }

    override fun setStats() {
        if (currentPhase == MaidenOfSugadintiPhase.FIRST)
            super.setStats()
    }

    override fun setTransformation(id: Int) {
        nextTransformation = id
        setId(id)
        size = definitions.size
        updateFlags.flag(UpdateFlag.TRANSFORMATION)
    }

    private fun hitsplatHeal(amount: Int) {
        if (getHitpoints() < maxHitpoints)
            applyHit(Hit(amount, HitType.HEALED))
    }

    override fun processNPC() {
        if (!canProcess()) return

        ticks++

        handleBloodSplats()

        if (ticks % attackInterval == 0) {
            if (canRollBloodAttack && Utils.random(Utils.getRandom(), 1, BLOOD_ATTACK_CHANCE) == 1) {
                bloodAttack()
                canRollBloodAttack = false
                hasUsedStormAttack = false
            } else stormAttack()
        }

        val newPhase = appropriateNewPhase

        if (currentPhase != newPhase) {
            currentPhase = newPhase
            if (currentPhase == MaidenOfSugadintiPhase.DYING) sendDeath()
            else changePhase()
        }

        bloodSpawns.removeIf { it.isDead }
    }

    fun absorbNylocas(nylocas: NylocasMatomenos) {
        if (!canProcess()) return
        maxHit += 3.5
        maxBloodSplatHit += 1.8
        hitsplatHeal((nylocas.hitpoints * 2).coerceAtMost(200))

        if (room.raid.hardMode) {
            attackInterval = max(4, attackInterval - 1)
        }
    }

    private fun changePhase() {
        setTransformation(currentPhase.npcId)
    }

    private fun stormAttack() {
        // used to ensure blood attacks can only be rolled after 2 successful storm attacks
        if (hasUsedStormAttack && !canRollBloodAttack) {
            canRollBloodAttack = true
            hasUsedStormAttack = false
        } else hasUsedStormAttack = true

        val toAttack = getNearestPlayer() ?: return
        faceEntity(toAttack)
        setAnimation(stormAttackAnimation)

        val delay = World.sendProjectile(this, toAttack, stormProjectile)
        val hit = Hit(this, getRandomMaxHit(this, maxHit.toInt(), AttackType.MAGIC, toAttack), HitType.MAGIC)
        delayHit(delay - 1, toAttack, hit)

        /* This is not an official mechanic of the encounter, verified in OSRS and on Wiki, removed */
        // 1/3 chance to drain highest stat if damage > 0
        // https://twitter.com/JagexAsh/status/1265697130959831042
        //if (hit.damage > 0 && Utils.random(Utils.getRandom(), 2) == 0)
        // drainStats(toAttack, hit.damage)

        toAttack.sendSound(SoundEffect(stormAttackHitSound.id, 0, 0.coerceAtLeast((delay - 1) * 30)))
        WorldTasksManager.schedule({ faceEntity(toAttack) }, 3)
    }

    private fun drainStats(player: Player, damage: Int) {
        val drainAmount = ceil(damage / 4.0).coerceAtMost(10.0).toInt()
        var highestBonus: Bonus? = bonusesToCheck[0]
        var highestBonusAmount = 0
        for (bonus in bonusesToCheck) {
            val bonusAmount = player.bonuses.getBonus(bonus)
            if (bonusAmount > highestBonusAmount) {
                highestBonus = bonus
                highestBonusAmount = bonusAmount
            }
        }
        when (highestBonus) {
            Bonus.ATT_SLASH, Bonus.ATT_CRUSH, Bonus.ATT_STAB -> {
                player.drainSkill(SkillConstants.ATTACK, drainAmount)
                player.drainSkill(SkillConstants.STRENGTH, drainAmount)
            }
            Bonus.ATT_RANGED -> player.drainSkill(SkillConstants.RANGED, drainAmount)
            Bonus.ATT_MAGIC -> player.drainSkill(SkillConstants.MAGIC, drainAmount)
            else -> {}
        }
        player.sendFilteredMessage("Your stats are drained.")
    }

    private fun getNearestPlayer(): Player? {
        // find closest player and attack them
        // E N S W = priority order (if multiple people are stood within the same distance)
        // priority by party order if multiple people stood in same direction
        var shortestDistance = Int.MAX_VALUE
        var nearestPlayer = room.raid.party.getMember(0)
        for (direction in stormDirections) {
            for (p in room.validTargets) {
                val distance = direction.getTileDistance(p.location)
                if (distance < shortestDistance) {
                    shortestDistance = distance
                    nearestPlayer = p
                }
            }
        }
        return nearestPlayer
    }

    private fun bloodAttack() {
        bloodSplatTargets.clear()
        setAnimation(bloodAttackAnimation)
        val validTargets = room.validTargets
        val locations = validTargets.map { it.location.copy() }
        val randomPlayer = Utils.getRandomCollectionElement(validTargets) ?: return
        faceEntity(randomPlayer)
        for (loc in locations) fireBloodProjectile(loc)
        WorldTasksManager.schedule {
            if (!canProcess()) return@schedule
            val amount = Utils.random(1, 2)
            for (i in 0 until amount)
                fireBloodProjectile(randomPlayer.location.random(2))
        }
    }

    private fun fireBloodProjectile(tile: Location) {
        if (!bloodSplatTargets.contains(tile.positionHash)
            && !splatExists(tile)
            && World.canPlaceObjectWithoutCollisions(tile, WorldObject.DEFAULT_TYPE)
            && World.isFloorFree(tile, 1)
        ) bloodSplatTargets.add(tile.positionHash)
        else return

        val delay = World.sendProjectile(this, tile, bloodProjectile)
        WorldTasksManager.schedule({
            if (!canProcess()) return@schedule
            if (addSplat(tile)) {
                World.sendSoundEffect(tile, bloodSplatSound)
                World.sendGraphics(bloodSplatGraphic, tile)
                var attackHitTemp = false
                for (p in room.validTargets) {
                    if (p.location.positionHash == tile.positionHash) {
                        attackHitTemp = true
                        break
                    }
                }
                val attackHit = attackHitTemp
                WorldTasksManager.schedule({ rollBloodSpawn(tile, attackHit) }, 9)
                WorldTasksManager.schedule({ removeSplat(tile) }, 10)
            }
        }, delay)
    }

    private fun rollBloodSpawn(tile: Location, hit: Boolean) {
        if (bloodSpawns.size >= BLOOD_SPAWN_MAX_COUNT) return
        if (!canProcess()) return

        val chance = if (hit) 5 else 10
        if (Utils.random(Utils.getRandom(), 1, chance) == 1)
            bloodSpawns.add((BloodSpawn(this, tile).spawn() as BloodSpawn))
    }

    private fun handleBloodSplats() {
        if (bloodSplatTiles.isEmpty()) return
        for (p in room.validTargets) {
            if (bloodSplatTiles.contains(p.location.positionHash)) {
                val damage = Utils.random(Utils.getRandom(), BLOOD_SPLAT_MIN_HIT, maxBloodSplatHit.toInt())
                p.applyHit(Hit(damage, HitType.REGULAR))
                p.prayerManager.drainPrayerPoints(damage / 2)
                hitsplatHeal(damage)
            }
        }
        if (bloodTrails.isNotEmpty())
            bloodTrails.removeIf { !it.process() }
    }

    fun splatExists(tile: Location) = bloodSplatTiles.contains(tile.positionHash)

    fun addSplat(tile: Location) = bloodSplatTiles.add(tile.positionHash)

    fun removeSplat(tile: Location) = bloodSplatTiles.remove(tile.positionHash)

    private fun canProcess() = !dead() && room.raid.party.size != 0

    override fun sendDeath() {
        setPhase(MaidenOfSugadintiPhase.DYING)

        bloodTrails.forEach(BloodTrail::remove)
        bloodTrails.clear()
        bloodSplatTiles.clear()
        bloodSplatTargets.clear()

        bloodSpawns.forEach { it.hitpoints = 0 }
        bloodSpawns.clear()

        setAnimation(deathAnimationPartOne)

        WorldTasksManager.schedule({
            setPhase(MaidenOfSugadintiPhase.DEAD)
            setAnimation(deathAnimationPartTwo)
            WorldTasksManager.schedule({onFinish(null)}, 2)
        }, 2)
    }

    override fun attack(target: Entity?) = 0

    override fun getMagicPrayerMultiplier() = 0.5

    override fun addWalkStep(nextX: Int, nextY: Int, lastX: Int, lastY: Int, check: Boolean) = false

    override fun checkProjectileClip(player: Player?, melee: Boolean) = false

    private fun setPhase(phase: MaidenOfSugadintiPhase) {
        currentPhase = phase
        setTransformation(phase.npcId)
    }

    fun dead() = currentPhase == MaidenOfSugadintiPhase.DYING || currentPhase == MaidenOfSugadintiPhase.DEAD

    companion object {

        /**
         * Spawn point of the Maiden.
         */
        val spawnLocation = Location(3162, 4444)

        private val stormAttackAnimation = Animation(8092)
        private val bloodAttackAnimation = Animation(8091)
        private val stormProjectile = Projectile(1577, 0, 10, 120, 0, 50, 0, 0)
        private val bloodProjectile = Projectile(1578, 135, 0, 20, 11, 100, 0, 0)
        private val bloodSplatGraphic = Graphics(1579)
        private val bonusesToCheck =
            arrayOf(Bonus.ATT_STAB, Bonus.ATT_SLASH, Bonus.ATT_CRUSH, Bonus.ATT_MAGIC, Bonus.ATT_RANGED)
        private val bloodSplatSound = SoundEffect(3547, 2)
        private val stormAttackHitSound = SoundEffect(176)

        private val deathAnimationPartOne = Animation(8093)
        private val deathAnimationPartTwo = Animation(8094)

        /**
         * Location of each 'direction' the Maiden checks in order to find the nearest player.
         * <br></br>
         * Order is important.
         * <br></br>
         * Based on static map positions, not to be used for calculations.
         */
        private val stormDirectionPoints: List<Location> = ObjectArrayList(
            arrayOf(
                Location(3167, 4446),  // East
                Location(3165, 4449),  // North
                Location(3165, 4444),  // South
                Location(3162, 4447) // West
            )
        )

        private const val MAX_HIT = 35
        private const val BLOOD_SPAWN_MAX_COUNT = 8

        private const val BLOOD_ATTACK_CHANCE = 4
        private const val TICKS_PER_ATTACK = 10
        private const val BLOOD_SPLAT_MAX_HIT = 10
        private const val BLOOD_SPLAT_MIN_HIT = 5

    }

}