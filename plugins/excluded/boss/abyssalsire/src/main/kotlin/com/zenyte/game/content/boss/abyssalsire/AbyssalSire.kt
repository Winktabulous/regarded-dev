package com.zenyte.game.content.boss.abyssalsire

import com.zenyte.game.content.boss.abyssalsire.WeakReferenceHelper.exists
import com.zenyte.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import com.zenyte.game.content.boss.abyssalsire.poisonfumes.AbyssalSirePoisonFumes
import com.zenyte.game.content.boss.abyssalsire.respiratorysystems.AbyssalSireRespiratorySystems
import com.zenyte.game.content.boss.abyssalsire.spawns.AbyssalSireSpawns
import com.zenyte.game.content.boss.abyssalsire.tentacles.AbyssalSireTentacleWalls
import com.zenyte.game.content.boss.abyssalsire.tentacles.AbyssalSireTentacles
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.Item
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.CollisionUtil
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.ImmutableLocation
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.entity.player.calog.CAType
import java.lang.ref.WeakReference

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSire(
    val corner: AbyssalNexusCorner,
    val lair: AbyssalNexusArea
) : NPC(SIRE_ASLEEP_ID, corner.translate(sireThroneLocation), Direction.SOUTH, 0) {

    var phase = AbyssalSirePhase.ASLEEP
    private var attackTimer = -4
    private var idleTimer = 0

    var target: WeakReference<Player>? = null

    private val disorientation = AbyssalSireDisorientation(this)

    var stunCount = 0
    var hitByPool = false
    var scionMatured = false
    var hitByTentacles = false
    var perfectSire = true
    val tentacles = AbyssalSireTentacles(this)
    private val tentacleWalls = AbyssalSireTentacleWalls(this)
    private val respiratorySystems = AbyssalSireRespiratorySystems(this)
    private val spawns = AbyssalSireSpawns(this)
    private val poisonFumes = AbyssalSirePoisonFumes(this)

    private fun resetSire(timeout: Boolean) {
        perfectSire = true
        stunCount = 0
        hitByPool = false
        scionMatured = false
        hitByTentacles = false
        idleTimer = 0
        respiratorySystems.respawnAll()
        tentacleWalls.buildAll()
        disorientation.reset()
        attackDistance = 1
        phase = AbyssalSirePhase.ASLEEP
        target = null
        setTransformation(SIRE_ASLEEP_ID)
        setDirection(Direction.SOUTH.direction)
        if (timeout) {
            tentacles.reset()
            spawns.reset()
            setLocation(respawnTile)
        }
    }

    override fun handleOutgoingHit(target: Entity, hit: Hit) {
        super.handleOutgoingHit(target, hit)

        val source = hit.source
        if (source is Player) {
            val type = hit.hitType
            if (type == HitType.MELEE && !source.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE) || type == HitType.RANGED && !source.prayerManager.isActive(
                    Prayer.PROTECT_FROM_MISSILES
                ) || type == HitType.MAGIC && !source.prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)
            ) {
                perfectSire = false
            }
        }
    }

    override fun spawn(): NPC {
        resetSire(false)
        return super.spawn()
    }

    override fun getXpModifier(hit: Hit): Float {
        disorientation.totalDamage += hit.damage
        onScheduledAttack(hit.source, hit.weapon)
        return super.getXpModifier(hit)
    }

    override fun applyHit(hit: Hit) {
        super.applyHit(hit)

        if (!target.exists() && hit.source !is Player)
            hit.damage = 0
    }

    override fun processNPC() {
        poisonFumes.attemptClear()

        if (isLocked || isDead || id == SIRE_ASLEEP_ID) return
        if (!target.exists()) {
            idleTimer = 0
            return
        }

        val player = target!!.get()!!
        if (!lair.players.contains(player)) {
            idleTimer++
            if (idleTimer >= 50) {
                resetSire(true)
            }
            return
        } else {
            idleTimer = 0
        }

        when (phase) {
            AbyssalSirePhase.ASLEEP -> {}
            AbyssalSirePhase.AWAKE -> {
                if (respiratorySystems.allDead()) {
                    secondPhase(player)
                    return
                }
                if (hitpoints < maxHitpoints && WorldThread.WORLD_CYCLE % 5 == 0L) {
                    setHitpoints(maxHitpoints)
                }
                if (!disorientation.processNPC() && id == SIRE_THRONE_CONTROLLING_ID && ++attackTimer % 5 == 0) {
                    if (!spawns.sendSpawn(player))
                        poisonFumes.sendPoisonFume()
                    return
                }
            }
            AbyssalSirePhase.MELEE -> {
                if (hitpointsAsPercentage <= 50) {
                    thirdPhase()
                    return
                }
                if (CollisionUtil.collides(x, y, getSize(), player.x, player.y, player.size)) {
                    val direction = Utils.random(Direction.cardinalDirections)
                    addWalkSteps(x + direction.offsetX, y + direction.offsetY, 1, true)
                    return
                } else if (combat.outOfRange(player, 0, player.size, true)) {
                    resetWalkSteps()
                    val result = calcFollow(player, 1, true, isIntelligent, isEntityClipped)
                    if (!result) {
                        if (!spawns.sendSpawn(player))
                            poisonFumes.sendPoisonFume()
                    }
                    return
                }
                if (++attackTimer % 7 == 0) {
                    var maxHit = 15
                    var style = AttackType.CRUSH
                    if (Utils.randomBoolean(10)) {
                        style = AttackType.SLASH
                        maxHit = 66
                        setAnimation(MELEE_ATTACK_TWO_TENTACLES)
                    } else if (Utils.randomBoolean(5)) {
                        style = AttackType.SLASH
                        setAnimation(MELEE_ATTACK_TENTACLE)
                    } else if (Utils.randomBoolean(5)) {
                        if (!spawns.sendSpawn(player))
                            poisonFumes.sendPoisonFume()
                    } else {
                        setAnimation(MELEE_ATTACK_JAB)
                    }

                    CombatUtilities.delayHit(
                        this,
                        0,
                        player,
                        Hit(
                            this,
                            CombatUtilities.getRandomMaxHit(this, maxHit, style, style, player),
                            HitType.MELEE
                        )
                    )
                }
            }
            AbyssalSirePhase.PREPARING_EXPLOSION -> {
                if (hitpoints <= 139) {
                    explodeTeleport(player)
                    return
                }
                if (poisonFumes.size() <= 0)
                    poisonFumes.sendPoisonFume()
                else
                    spawns.sendSpawn(player)
            }
            AbyssalSirePhase.POST_EXPLOSION -> {
                if (attackTimer % 2 == 0 && !spawns.forceSpawn(player) && poisonFumes.size() <= 0) {
                    poisonFumes.sendPoisonFume()
                }
            }
        }

    }

    private fun explodeTeleport(player: Player) {
        lock()
        setAnimation(EXPLODING)
        val destination = location.transform(2, -1)
        val teleport: Teleport = object : Teleport {
            override fun getType(): TeleportType {
                return TeleportType.TELEOTHER_TELEPORT
            }

            override fun getDestination(): Location {
                return destination
            }

            override fun getLevel(): Int {
                return 0
            }

            override fun getExperience(): Double {
                return 0.0
            }

            override fun getRandomizationDistance(): Int {
                return 0
            }

            override fun getRunes(): Array<Item> {
                return emptyArray()
            }

            override fun getWildernessLevel(): Int {
                return 0
            }

            override fun isCombatRestricted(): Boolean {
                return false
            }

        }

        teleport.teleport(player)
        WorldTasksManager.schedule({
            unlock()
            val distance = player.location.getTileDistance(destination)
            if (distance <= 1) {
                val hit = Hit(this, 60, HitType.DEFAULT)
                hit.putAttribute("sire_explosion", true)
                player.applyHit(hit)
                perfectSire = false
            }
            phase = AbyssalSirePhase.POST_EXPLOSION
            setTransformation(SIRE_POST_EXPLOSION_ID)
        }, 3)
    }

    private fun thirdPhase() {
        lock()
        tentacles.riseIfNotActive()
        setFaceEntity(null)
        temporaryAttributes["ignoreWalkingRestrictions"] = true
        setTransformation(SIRE_CONTROLLED_WALKING_ID)
        animation = Animation.STOP
        val walkLocation = respawnTile.transform(0, -18)
        addWalkSteps(walkLocation.x, walkLocation.y, -1, false)
        WorldTasksManager.schedule({
            setAnimation(TRANSFORM_TO_THIRD_PHASE)
            WorldTasksManager.schedule({
                for (i in 1..3) {
                    spawns.forceSpawn(target!!.get()!!)
                }
                setTransformation(SIRE_PREPARING_BOMB_ID)
                attackTimer = 0
                phase = AbyssalSirePhase.PREPARING_EXPLOSION
                temporaryAttributes.remove("ignoreWalkingRestrictions")
                unlock()
            }, 2)
        }, 10)
    }

    private fun secondPhase(target: Player) {
        lock()
        tentacles.riseIfNotActive()
        setFaceEntity(null)
        temporaryAttributes["ignoreWalkingRestrictions"] = true
        setTransformation(SIRE_CONTROLLED_WALKING_ID)
        animation = Animation.STOP
        val walkLocation = respawnTile.transform(0, -10)
        addWalkSteps(walkLocation.x, walkLocation.y, -1, false)
        WorldTasksManager.schedule({
            setFaceEntity(target)
            attackTimer = 0
            setTransformation(SIRE_MELEE_FIGHT_ID)
            phase = AbyssalSirePhase.MELEE
            temporaryAttributes.remove("ignoreWalkingRestrictions")
            unlock()
        }, 10)
    }

    override fun setTransformation(id: Int) {
        val currentHitpoints = hitpoints

        super.setTransformation(id)

        if (hitpoints != currentHitpoints) {
            hitpoints = currentHitpoints
        }
        // Player's combat action seems to get cancelled during sire phase transitions.
        target {
            if (this == (it.actionManager.action as? PlayerCombat)?.target)
                it.cancelCombat()
        }
    }

    fun onScheduledAttack(attacker: Entity, weapon: Any?) {
        if (!target.exists() && attacker is Player) {
            target = WeakReference(attacker)
            phase = AbyssalSirePhase.AWAKE

            regainControl()
            tentacleWalls.removeAll()
        }

        disorientation.onScheduledAttack(weapon)
    }

    fun regainControl() {
        setTransformation(SIRE_THRONE_CONTROLLING_ID)
        setAnimation(sireRegainingControlAnimation)

        tentacles.rise()

        attackTimer = 0
    }

    override fun getMeleePrayerMultiplier(): Double {
        return 0.4
    }

    override fun getRangedPrayerMultiplier(): Double {
        return 0.4
    }

    override fun getMagicPrayerMultiplier(): Double {
        return 0.4
    }

    override fun finish() {
        super.finish()

        tentacles.reset()
        spawns.reset()
    }

    override fun getRespawnDelay(): Int {
        return 15
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        if (source is Player) {
            source.combatAchievements.checkKcTask("abyssal sire", 20, CAType.ABYSSAL_ADEPT)
            source.combatAchievements.checkKcTask("abyssal sire", 50, CAType.ABYSSAL_VETERAN)
            if (!hitByTentacles) {
                source.combatAchievements.complete(CAType.DONT_WHIP_ME)
            }
            if (!scionMatured) {
                source.combatAchievements.complete(CAType.THEY_GROW_UP_TOO_FAST)
            }
            if (!hitByPool) {
                source.combatAchievements.complete(CAType.DONT_STOP_MOVING)
            }
            if (stunCount == 1) {
                source.combatAchievements.complete(CAType.RESPIRATORY_RUNNER)
            }
            if (perfectSire) {
                source.combatAchievements.complete(CAType.PERFECT_SIRE)
            }
        }
    }

    companion object {

        const val MAXIMUM_ATTACK_DISTANCE = 15

        private const val SIRE_ASLEEP_ID = NpcId.ABYSSAL_SIRE
        private const val SIRE_CONTROLLED_WALKING_ID = NpcId.ABYSSAL_SIRE_5889
        private const val SIRE_MELEE_FIGHT_ID = NpcId.ABYSSAL_SIRE_5890
        private const val SIRE_PREPARING_BOMB_ID = NpcId.ABYSSAL_SIRE_5891
        private const val SIRE_POST_EXPLOSION_ID = NpcId.ABYSSAL_SIRE_5908
        const val SIRE_THRONE_CONTROLLING_ID = NpcId.ABYSSAL_SIRE_5887
        const val SIRE_THRONE_STUNNED_ID = NpcId.ABYSSAL_SIRE_5888

        private val sireThroneLocation = ImmutableLocation(2977, 4855, 0)
        private val sireRegainingControlAnimation = Animation(4528)
        private val MELEE_ATTACK_JAB = Animation(5366)
        private val MELEE_ATTACK_TENTACLE = Animation(5369)
        private val MELEE_ATTACK_TWO_TENTACLES = Animation(5755)
        private val TRANSFORM_TO_THIRD_PHASE = Animation(7096)
        private val EXPLODING = Animation(7098)

    }

}
