package com.near_reality.game.content.wilderness.event.ganodermic_beast

import com.near_reality.game.content.wilderness.event.ganodermic_beast.GanodermicBeastAttack.*
import com.near_reality.game.item.CustomNpcId
import com.near_reality.game.world.entity.player.blackSkulled
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.CombatScriptsHandler
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NPCCombat
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import com.zenyte.game.world.region.CharacterLoop
import kotlin.random.Random

/**
 * Handles the behaviour of the Ganodermic beast.
 *
 * @author Stan van der Bend
 */
class GanodermicBeast(val spawnLocation: SpawnLocation) :
    NPC(CustomNpcId.GANODERMIC_BEAST, spawnLocation.location, Direction.NORTH, 0), CombatScript {

    internal val lightningTiles = mutableSetOf<LightningTile>()

    var phase = Phase.PHASE_1

    lateinit var combatScript: GanodermicBeastAttack

    init {
        isForceMultiArea = true
        attackDistance = 15
        aggressionDistance = 15
        maxDistance = 15
        combat = object : NPCCombat(this) {
            override fun combatAttack(): Int {
                if (target == null) {
                    return 0
                }
                val melee = isMelee
                if (npc.isProjectileClipped(target, melee)) {
                    return 0
                }
                val distance = if (melee || npc.isForceFollowClose) 0 else npc.attackDistance
                if (outOfRange(target, distance, target.size, melee)) {
                    return 0
                }
                addAttackedByDelay(target)
                return CombatScriptsHandler.specialAttack(npc, target)
            }

            override fun isMelee() = false
        }
    }

    override fun processNPC() {

        super.processNPC()

        if (isDying || isDead)
            return

        val players = findPlayerTargetsInRange(20)

        lightningTiles.removeIf { it.pulse(this, players) }

        if (this::combatScript.isInitialized) {
            if (combatScript.attackType == AttackType.MELEE) {
                val target = combat.target
                if (target == null || !combatScript.isWithinMeleeDistance(this, target)) {
                    val playerInDistance = players.find { combatScript.isWithinMeleeDistance(this, target) }
                    if (playerInDistance != null)
                        combat.forceTarget(playerInDistance)
                    else
                        combatScript = phase.rollAttack(this, target)
                }
            }
        }

        val percentageHealth = (maxHitpoints - hitpoints)
            .takeIf { it != 0 }
            ?.toDouble()
            ?.div(maxHitpoints)
            ?: return

        phase = Phase.values().findLast { percentageHealth < it.percentage } ?: Phase.PHASE_5
    }

    internal fun findPlayerTargetsInRange(range: Int): MutableList<Player> =
        CharacterLoop.find(location, range, Player::class.java) { true }

    override fun attack(target: Entity): Int {

        combatScript = phase.rollAttack(this, target)

        return combatScript.attack(target)
    }

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)
        if (phase == Phase.PHASE_1 || phase == Phase.PHASE_2) {
            if (hit.hitType != HitType.MELEE) {
                hit.damage = 0
                (hit.source as? Player)?.sendFilteredMessage("The Ganodermic beast's defence fully absorbs your attacks!")
            }
        }
        (hit.source as? Player)?.run {
            if (!blackSkulled) {
                blackSkulled = true
                variables.setSkull(true)
                sendMessage(Colour.RED.wrap("You have been skulled by the Ganodermic beast, players from any combat level can now attack you!"))
            }
        }
    }

    override fun isFreezeImmune(): Boolean {
        return true
    }

    override fun setRespawnTask() {}

    override fun isTolerable(): Boolean = false
    override fun isEntityClipped(): Boolean = false
    override fun isIntelligent(): Boolean = true
    override fun isForceAggressive(): Boolean = true
    override fun getAttackDistance(): Int = if (this::combatScript.isInitialized)
        combatScript.range
    else
        1
    override fun addWalkStep(nextX: Int, nextY: Int, lastX: Int, lastY: Int, check: Boolean): Boolean =
        false

    override fun canMove(fromX: Int, fromY: Int, direction: Int): Boolean = false
    override fun checkProjectileClip(player: Player?, melee: Boolean): Boolean  = false

    class SpawnLocation(val name: String, val location: Location)

    enum class Phase(val percentage: Float) {
        PHASE_1(0.70F) {
            override fun rollAttack(beast: GanodermicBeast, target: Entity) = when(Utils.random(100)) {
                in 0..30 -> if (CombatUtilities.isWithinMeleeDistance(beast, target)) Melee(beast) else Magic(beast)
                in 30..60 -> Magic(beast)
                in 60..80 -> ShadowArrow(beast)
                in 80..90 -> Teleblock(beast)
                else ->  if (beast.lightningTiles.isEmpty())
                    LightningTiles(beast)
                else
                    Melee(beast)
            }
        },
        PHASE_2(0.60F){
            override fun rollAttack(beast: GanodermicBeast, target: Entity) = when(Utils.random(100)) {
                in 0..20 -> if (CombatUtilities.isWithinMeleeDistance(beast, target)) Melee(beast) else Magic(beast)
                in 20..40 -> Magic(beast)
                in 40..55 -> ShadowArrow(beast)
                in 55..60 -> Teleblock(beast)
                in 60..80 -> LightningTornado(beast)
                else -> ShadowSpikes(beast)
            }
        },
        PHASE_3(0.50F){
            override fun rollAttack(beast: GanodermicBeast, target: Entity) = when(Utils.random(100)) {
                in 0..30 -> Magic(beast)
                in 30..50 -> ShadowArrow(beast)
                in 50..60 -> Teleblock(beast)
                in 60..80 -> LightningTornado(beast)
                else -> if (beast.lightningTiles.isEmpty() && Utils.randomBoolean())
                    LightningTiles(beast)
                else
                    ShadowSpikes(beast)
            }
        },
        PHASE_4(0.40F){
            override fun rollAttack(beast: GanodermicBeast, target: Entity) = when(Utils.random(100)) {
                in 0..30 -> LightningTornado(beast)
                in 30..50 -> Magic(beast)
                in 50..60 -> Teleblock(beast)
                in 60..80 -> ShadowArrow(beast)
                else ->  if (beast.lightningTiles.isEmpty() && Utils.randomBoolean())
                    LightningTiles(beast)
                else
                    ShadowSpikes(beast)
            }
        },
        PHASE_5(0.20F){
            override fun rollAttack(beast: GanodermicBeast, target: Entity) = when(Utils.random(100)) {
                in 0..15 -> Melee(beast)
                in 15..30 -> Magic(beast)
                in 30..40 -> Teleblock(beast)
                in 40..65 -> ShadowArrow(beast)
                else ->  if (beast.lightningTiles.isEmpty() && Utils.randomBoolean())
                    LightningTiles(beast)
                else
                    ShadowSpikes(beast)
            }
        };

        abstract fun rollAttack(beast: GanodermicBeast, target: Entity) : GanodermicBeastAttack
    }


    class LightningTile(private var lifeTime: Int, private var location: Location) {

        fun pulse(beast: GanodermicBeast, players: List<Player>): Boolean {
            players
                .filter { it.location == location }
                .forEach { CombatUtilities.delayHit(beast, 0, it, Hit(beast, Utils.random(1, 3), HitType.MAGIC)) }
            if (--lifeTime % 5 == 0){
                if (Random.nextBoolean())
                    location = location.transform(Direction.values.random())
                World.sendGraphics(Graphics(1913), location)
            }
            return lifeTime <= 0
        }

    }
}
