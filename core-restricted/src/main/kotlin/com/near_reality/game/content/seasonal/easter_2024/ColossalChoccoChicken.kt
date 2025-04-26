package com.near_reality.game.content.seasonal.easter_2024

import com.near_reality.game.item.CustomNpcId
import com.zenyte.game.content.boss.wildernessbosses.callisto.Callisto
import com.zenyte.game.content.skills.firemaking.FireObject
import com.zenyte.game.content.skills.firemaking.Firemaking
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.CollisionUtil
import com.zenyte.game.util.Direction
import com.zenyte.game.util.DirectionUtil
import com.zenyte.game.util.LocationUtil
import com.zenyte.game.world.Position
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.EntityHitBar
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.*
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NPCCombat
import com.zenyte.game.world.entity.npc.combatdefs.*
import com.zenyte.game.world.entity.player.Action
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.CharacterLoop
import com.zenyte.game.world.region.GlobalAreaManager
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ColossalChoccoChicken : NPC(ChickenType.Red.npcId, Location(1892, 5154, 2), Direction.EAST, 5) {

    private val combatController = ColossalChoccoChickenCombatController(this)
    internal var type: ChickenType = ChickenType.Red
    internal var blockTargetFacing = false
    internal var transforming = false

    companion object {
        var configuredMaxHitpoints = 30_000
    }

    init {
        forceAggressive = true
        isForceMultiArea = true
        hitpoints = maxHitpoints
        maxDistance = 6
        hitBar = object : EntityHitBar(this) {
            override fun getType(): Int {
                return 21
            }
        }
        combat = combatController
    }

    override fun setRespawnTask() = Unit

    override fun updateCombatDefinitions() {
        setCombatDefinitions(NPCCombatDefinitions().apply {
            hitpoints = configuredMaxHitpoints
            aggressionType = AggressionType.ALWAYS_AGGRESSIVE
            attackDistance = 6
            aggressionDistance = 6
            maximumDistance = 6
            spawnDefinitions = SpawnDefinitions().apply {
                deathAnimation = Animation(17431)
                respawnDelay -1
            }
            attackDefinitions = AttackDefinitions().apply {
                type = AttackType.STAB
            }
            immunityTypes = EnumSet.allOf(ImmunityType::class.java)
            statDefinitions = StatDefinitions()
            blockDefinitions = BlockDefinitions()
        })
    }

    override fun spawn(): NPC {
        return super.spawn().also {
            switchType(ChickenType.Red)
            GlobalAreaManager.getArea(Easter2024EventArea::class.java).players.forEach {
                it.hpHud.open(true, id, maxHitpoints)
            }
            lock(5)
            setAnimation(Animation(17424))
            setForceTalk("Bawk bawk bawk!")
            blockTargetFacing = false
            transforming = false
            isCantInteract = false
        }
    }

    override fun setFaceEntity(entity: Entity?) {
        if (blockTargetFacing) return
        super.setFaceEntity(entity)
    }

    override fun faceEntity(target: Entity?) {
        if (blockTargetFacing) return
        super.faceEntity(target)
    }

    override fun setFaceLocation(tile: Location?) {
        if (blockTargetFacing) return
        super.setFaceLocation(tile)
    }

    override fun processEntity() {
        if(!Easter2024Manager.enabled && !isFinished) {
            finish()
            return
        }
        super.processEntity()
        maxDistance = 64
        isIntelligent = true
        isRun = true
        if (type.vulernableToFire) {
            World.forEachSpawnedObject(middleLocation, 2) {
                if (it is FireObject) {
                    val hit = Hit(this, Random.nextInt(1, 3), HitType.DEFAULT)
                    hit.putAttribute("fire", true)
//                    hit.setExecuteIfLocked()
                    it.owner?.let { player -> addReceivedDamage(player, hit.damage, HitType.DEFAULT) }
                    applyHit(hit)
                }
            }
        }
        GlobalAreaManager.getArea(Easter2024EventArea::class.java).players.forEach {
            if (it is Player) {
                it.hpHud.updateValue(getHitpoints())
            }
        }
    }

    override fun setInvalidAnimation(animation: Animation?) {
        super.setInvalidAnimation(animation)
    }

    override fun setAnimation(animation: Animation?) {
        super.setAnimation(animation)
    }

    override fun setUnprioritizedAnimation(animation: Animation?) {
        super.setUnprioritizedAnimation(animation)
    }

    override fun applyHit(hit: Hit?) {

        val isFire = hit?.getAttribute("fire") == true
        if (isFire && type.vulernableToFire) {
            super<NPC>.applyHit(hit)
            return
        }
        val isEarth = hit?.getAttribute("earth") == true
        if (isEarth && type.vulernableToEarth) {
            super<NPC>.applyHit(hit)
            return
        }
        val isWater = hit?.getAttribute("water") == true
        if (isWater && type.vulernableToWater) {
            super<NPC>.applyHit(hit)
            return
        }

        val spellUsed = hit?.getAttribute("spell")
        val negateDamage = if (spellUsed is CombatSpell) {
            val spellName = spellUsed.name
            when {
                spellName.startsWith("WATER") || spellName.startsWith("ICE") -> !type.vulernableToWater
                spellName.startsWith("FIRE") -> !type.vulernableToFire
                spellName.startsWith("EARTH") -> !type.vulernableToEarth
                else -> true
            }
        } else
            true
        if (negateDamage) {
            hit?.run { this.damage /= 5 }
            hit?.source?.let { if (it is Player) it.sendMessage("The chicken absorbs most of your damage, perhaps try the right elemental magic.") }
        }
        println("$spellUsed Negate damage: $negateDamage -> $hit $type")

        super<NPC>.applyHit(hit)
    }

    internal fun switchType(type: ChickenType) {
        this.type = type
        setTransformation(type.npcId)
    }

    override fun sendDeath() {
        super.sendDeath()
        Easter2024Manager.changeState(Easter2024State.CHICKEN_KILLED)
        GlobalAreaManager.getArea(Easter2024EventArea::class.java).players.forEach {
            it.hpHud.close()
        }
    }

    override fun isProjectileClipped(target: Position?, closeProximity: Boolean): Boolean {
        return super.isProjectileClipped(target, closeProximity)
    }

    override fun performDefenceAnimation(attacker: Entity?) = Unit
}

class ColossalChoccoChickenCombatController(val chicken: ColossalChoccoChicken) : NPCCombat(chicken) {

    override fun combatAttack(): Int {
        if (target == null) {
            return 0
        }
        if (CollisionUtil.collides(npc.x, npc.y, npc.size, target.x, target.y, target.size)) {
            return 0
        }
//        if (outOfRange(target, 6, target.size, false)) {
//            return 0
//        }
        addAttackedByDelay(target)
        val percentageHealth = chicken.hitpoints.toDouble() / chicken.maxHitpoints
        val attack = when {
            percentageHealth <= chicken.type.switchOnDamagePercentage -> ColossalChoccoChickenAttack.FlyAndStrike
            else -> ColossalChoccoChickenAttack.Peck
        }
        return attack.perform(chicken, target)
    }

    override fun outOfRange(
        targetPosition: Position?,
        maximumDistance: Int,
        targetSize: Int,
        checkDiagonal: Boolean,
    ): Boolean {
        return super.outOfRange(targetPosition, 2, targetSize, checkDiagonal)
    }
}

internal sealed class ColossalChoccoChickenAttack {
    abstract fun perform(chicken: ColossalChoccoChicken, target: Entity?): Int


    fun scheduleIfNotDead(chicken: ColossalChoccoChicken, delay: Int, task: () -> Unit) {
        if (chicken.isDead || chicken.isFinished || chicken.isDying) return
        schedule(delay) {
            if (chicken.isDead || chicken.isFinished || chicken.isDying) return@schedule
            task()
        }
    }

    data object Peck : ColossalChoccoChickenAttack() {

        override fun perform(chicken: ColossalChoccoChicken, target: Entity?): Int {
            val faceDirection = chicken.faceLocation?.let { Direction.getDirection(chicken.middleLocation, it) }?:chicken.direction
            val availableDirections = Direction.entries.filter { it != faceDirection }
            val direction = availableDirections.random()
            val targetTile = chicken.middleLocation.transform(direction, chicken.size-1)
            chicken.setFaceEntity(null)
            chicken.faceDirection(direction)
            chicken.blockTargetFacing = true
            chicken.walkSteps.clear()
            chicken.freeze(5)
            scheduleIfNotDead(chicken, 1) {
                chicken.setForceTalk("Tok tok tok!")
                chicken.animation = Animation(17430, 60)
                chicken.setInvalidAnimation(Animation(17430, 60))
                scheduleIfNotDead(chicken, 2) {
                    CharacterLoop.forEach(targetTile, 2, Player::class.java) {
                        knockback(chicken, it)
                    }
                    chicken.blockTargetFacing = false
                }
            }
            return 6
        }
    }

    data object FlyAndStrike : ColossalChoccoChickenAttack() {

        private const val DURATION_IN_TICKS = 10

        override fun perform(chicken: ColossalChoccoChicken, target: Entity?): Int {
            chicken.animation = Animation(17425, 50)
            chicken.blockTargetFacing = true
            chicken.transforming = true
            chicken.isCantInteract = true
            chicken.receivedHits.clear()
            chicken.setFaceEntity(null)
            chicken.setForceTalk("Bawk bawk bawk!")
            chicken.lock(DURATION_IN_TICKS)
            scheduleIfNotDead(chicken, DURATION_IN_TICKS - 5) {
                chicken.animation = Animation(17432)
                chicken.switchType(
                    when (chicken.type) {
                        ChickenType.Red -> ChickenType.Green
                        ChickenType.Green -> ChickenType.Blue
                        ChickenType.Blue -> ChickenType.Rainbow
                        ChickenType.Rainbow -> ChickenType.Red
                    }
                )
                scheduleIfNotDead(chicken, 2) {
                    chicken.setForceTalk("Bawk bawk bawk!")
                    chicken.blockTargetFacing = false
                    chicken.transforming = false
                    chicken.isCantInteract = false
                    chicken.animation = Animation(17420)
                    CharacterLoop.forEach(chicken.middleLocation, 1, Player::class.java) { target ->
                        knockback(chicken, target)
                    }
                }
            }
            return DURATION_IN_TICKS
        }
    }

    internal fun knockback(chicken: ColossalChoccoChicken, target: Entity) {
        val middle: Location = chicken.getMiddleLocation()
        var degrees = Math.toDegrees(atan2((target.y - middle.y).toDouble(), (target.x - middle.x).toDouble()))
        if (degrees < 0) {
            degrees += 360.0
        }
        val angle = Math.toRadians(degrees)
        val px = Math.round(middle.x + (chicken.size + 6) * cos(angle)).toInt()
        val py = Math.round(middle.y + (chicken.size + 6) * sin(angle)).toInt()
        val tiles = LocationUtil.calculateLine(target.x, target.y, px, py, target.plane)
        if (!tiles.isEmpty()) tiles.removeAt(0)
        val destination = Location(target.location)
        for (tile in tiles) {
            val dir = DirectionUtil.getMoveDirection(tile.x - destination.x, tile.y - destination.y)
            if (dir == -1) {
                continue
            }
            if (!World.checkWalkStep(
                    destination.plane,
                    destination.x,
                    destination.y,
                    dir,
                    target.size,
                    false,
                    false
                )
            ) break
            destination.setLocation(tile)
        }
        val direction = DirectionUtil.getFaceDirection(
            (target.x - destination.x).toDouble(),
            (target.y - destination.y).toDouble()
        )
        if (!destination.matches(target)) {
            target.forceMovement = ForceMovement(destination, 30, direction)
            target.lock()
        }
        target.faceEntity(chicken)
        val from =
            Location(chicken.location.getCoordFaceX(chicken.size), chicken.location.getCoordFaceY(chicken.size), chicken.plane)
        World.sendGraphics(Callisto.KNOCKBACK_PLAYER_GRAPHICS, target.location)
        target.animation = Callisto.KNOCKBACK_ANIMATION
        schedule({
            target.setLocation(destination)
            target.unlock()
            target.graphics = Graphics(254, 0, 92)
            target.stun(5)
        })
    }
}

internal sealed class ChickenType(
    val npcId: Int,
    val switchOnDamagePercentage: Float,
    val vulernableToWater: Boolean = false,
    val vulernableToFire: Boolean = false,
    val vulernableToEarth: Boolean = false,
) {
    companion object {
        val values by lazy { arrayOf(Red, Green, Blue, Rainbow) }
    }

    data object Red : ChickenType(CustomNpcId.CHICKEN_RED_EASTER_2024, 0.75f, vulernableToWater = true)
    data object Green : ChickenType(CustomNpcId.CHICKEN_GREEN_EASTER_2024, 0.5f, vulernableToFire = true)
    data object Blue : ChickenType(CustomNpcId.CHICKEN_BLUE_EASTER_2024, 0.25f, vulernableToEarth = true)
    data object Rainbow : ChickenType(CustomNpcId.CHICKEN_RAINBOW_EASTER_2024, 0.0f, vulernableToWater = true, vulernableToFire = true, vulernableToEarth = true)
}

@Suppress("unused")
class ItemOnChicken : ItemOnNPCAction {

    override fun handleItemOnNPCAction(player: Player?, item: Item?, slot: Int, npc: NPC?) {
        player ?: return
        item ?: return
        if (npc !is ColossalChoccoChicken) return
        val inventory = player.inventory
        inventory.replaceItem(ItemId.BUCKET, 1, slot)
        val vulnerable = when (item.id) {
            ItemId.BUCKET_OF_WATER -> npc.type.vulernableToWater
            ItemId.BUCKET_OF_SAND -> npc.type.vulernableToEarth
            else -> return
        }
        val contents = when (item.id) {
            ItemId.BUCKET_OF_WATER -> "water"
            ItemId.BUCKET_OF_SAND -> "sand"
            else -> return
        }
        if (vulnerable) {
            player.animation = Animation(2450)
            player.sendMessage("You empty the $contents on the chicken, it does not like it.")
            val hit = Hit(player, Random.nextInt(15, 30), HitType.DEFAULT)
            if (item.id == ItemId.BUCKET_OF_WATER)
                hit.putAttribute("water", true)
            else if (item.id == ItemId.BUCKET_OF_SAND)
                hit.putAttribute("earth", true)
            npc.addReceivedDamage(player, hit.damage, HitType.DEFAULT)
            npc.applyHit(hit)
        } else
            player.sendMessage("You empty the $contents on the chicken, but nothing happens.")
    }

    override fun getItems() = arrayOf(ItemId.BUCKET_OF_WATER, ItemId.BUCKET_OF_SAND)

    override fun getObjects() = ChickenType.values.map { it.npcId }.toTypedArray()
}

@Suppress("unused")
class ItemOnFountain : ItemOnObjectAction {

    override fun handleItemOnObjectAction(player: Player?, item: Item?, slot: Int, `object`: WorldObject?) {
        player ?: return
        item ?: return
        `object`?: return
        player.actionManager.setAction(BucketWaterFilling(`object`))
    }

    override fun getItems() = arrayOf(ItemId.BUCKET)
    override fun getObjects() = arrayOf(5125)


    class BucketWaterFilling(private val `object`: WorldObject) : Action() {
        override fun start(): Boolean {
            player.animation = animation
            player.sendSound(synth)
            delay(1)
            return true
        }

        override fun process(): Boolean {
            return player.inventory.containsItem(ItemId.BUCKET, 1)
        }

        override fun processWithDelay(): Int {
            player.animation = animation
            player.sendSound(synth)
            player.faceObject(`object`)
            player.sendFilteredMessage("You fill the bucket with water.")
            player.inventory.ifDeleteItem(
                Item(ItemId.BUCKET, 1)
            ) { player.inventory.addOrDrop(Item(ItemId.BUCKET_OF_WATER, 1)) }
            return 2
        }

        companion object {
            private val animation = Animation(895)
            private val synth = SoundEffect(2584)
        }
    }
}
//22726 sand pit

val fireObjectIds = Firemaking.entries.map { it.objectId }.toSet()


