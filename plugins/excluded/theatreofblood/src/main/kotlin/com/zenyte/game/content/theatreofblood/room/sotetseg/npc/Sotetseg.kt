package com.zenyte.game.content.theatreofblood.room.sotetseg.npc

import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.content.theatreofblood.party.RaidingParty.Companion.getPlayer
import com.zenyte.game.content.theatreofblood.room.TheatreBossNPC
import com.zenyte.game.content.theatreofblood.room.TheatreRoomType
import com.zenyte.game.content.theatreofblood.room.sotetseg.ShadowRealmRoom
import com.zenyte.game.content.theatreofblood.room.sotetseg.SotetsegRoom
import com.zenyte.game.content.theatreofblood.room.sotetseg.SotetsegRoomTile
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Position
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NPCCombat
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import com.zenyte.game.world.entity.player.calog.CAType
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import it.unimi.dsi.fastutil.doubles.DoubleArraySet
import it.unimi.dsi.fastutil.doubles.DoubleSet
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author Tommeh
 * @author Jire
 */
internal class Sotetseg(room: SotetsegRoom) :
    TheatreBossNPC<SotetsegRoom>(room, NpcId.SOTETSEG_8388, room.getLocation(3277, 4326)),
    CombatScript {

    val mazePhases: DoubleSet = DoubleArraySet(ShadowRealmPhase.phases.size)

    private val mazeTiles: ObjectSet<Location> = ObjectLinkedOpenHashSet()
    private val damageTiles: ObjectSet<Location> = ObjectOpenHashSet(4)

    var ballsTanked = 0
    var started = false
    private var magicAttackCount = 0
    var isMazePhase = false
        private set
    var shadowRealm: ShadowRealmRoom? = null
        private set
    private var mazeStorm: RedStorm? = null
    private var activeMazeTile: Location? = null

    init {
        aggressionDistance = 64
        setAttackDistance(64)
        combat = object : NPCCombat(this) {
            override fun isMelee() = false
        }
    }

    override fun processNPC() {
        super.processNPC()

        if (!started) return
        if (isMazePhase) {
            mazePhaseProcess()
            return
        }
        if (ShadowRealmPhase.shouldInitiate(this)) {
            commenceMazePhase()
            return
        }

        // set target to random party member
        val target = Utils.getRandomCollectionElement(room.validTargets) ?: return
        setTarget(target)
    }

    override fun attack(target: Entity): Int {
        if (target !is Player) return 0

        return if (!isWithinMeleeDistance(this, target) || Utils.randomBoolean())
            performMagicAttack(target)
        else performMeleeAttack(target)
    }

    private fun performMeleeAttack(target: Player): Int {
        setAnimation(meleeAttackAnimation)
        delayHit(1, target, Hit(this, getRandomMaxHit(this, MELEE_MAX_HIT, CombatScript.CRUSH, target), HitType.MELEE))
        return ATTACK_SPEED
    }

    override fun isProjectileClipped(target: Position?, closeProximity: Boolean) = false

    private fun performMagicAttack(target: Player): Int {
        setAnimation(magicAttackAnimation)
        if (magicAttackCount == 10) {
            performBombAttack(target)
            magicAttackCount = 0
            return 9
        } else {
            magicAttackCount++

            /*
                Solo = only mage, no ricochet
                Duos = 50/50 ricochet into range or mage
                Trio+ = 1 magic + 1 range
             */

            val delay = fireBall(target)
            if(room.raid.hardMode) {
                WorldTasksManager.schedule({
                    fireBall(target)
                }, 2)
                return delay + 4
            }
            return delay + 2
        }
    }

    private fun fireBall(target: Player) : Int {
        // initial magic ball projectile
        val delay = World.sendProjectile(this, target, BallAttack.MAGIC.createProjectile(false))

        // delay hit for initial magic ball
        WorldTasksManager.schedule({
            if (!shouldAttack() || !room.isValidTarget(target)) return@schedule

            BallAttack.MAGIC.applyHit(target, this)

            val validTargets = room.validTargets
            if (validTargets.size == 1) {
                // no ricochet occurs in solos
                return@schedule
            }

            WorldTasksManager.schedule(schedule2@{
                if (!shouldAttack() || !room.isValidTarget(target)) return@schedule2

                val members = ObjectArrayList(validTargets)
                Collections.shuffle(members) // randomize players
                var firstRicochet: BallAttack? = null
                for (m in members) {
                    if (m == target) continue
                    if (firstRicochet == null) {
                        firstRicochet = BallAttack.randomBall()
                        performBallAttack(target, m, firstRicochet)
                    } else {
                        if (firstRicochet == BallAttack.MAGIC) performBallAttack(target, m, BallAttack.RANGE)
                        else performBallAttack(target, m, BallAttack.MAGIC)
                        break
                    }
                }
            }, 2)
        }, delay)

        return delay
    }

    private fun performBallAttack(initialPlayer: Player, target: Player, ball: BallAttack?) {
        var projectile = ball!!.createProjectile(true)
        projectile.delay = 0
        val delay = World.sendProjectile(initialPlayer, target, projectile)
        WorldTasksManager.schedule({
            if (!shouldAttack() || !room.isValidTarget(target)) return@schedule
            ball.applyHit(target, this)
        }, delay)
    }

    private fun performBombAttack(target: Player) {
        World.sendSoundEffect(middleLocation, SoundEffect(3994, 10, 0))
        target.sendMessage(Colour.RED.wrap("A large ball of energy is shot your way..."))
        val delay = World.sendProjectile(this, target, bombProjectile)
        val teamMatesAlive = room.validTargets.size
        WorldTasksManager.schedule({
            if (!shouldAttack() || !room.isValidTarget(target)) return@schedule
            val membersInRange = playersInRange(target)
            val numberOfMembersOutOfRange = teamMatesAlive - membersInRange.size
            val totalDamage = BOMB_BASE_DAMAGE + numberOfMembersOutOfRange * BOMB_EXTRA_DAMAGE_PER_PLAYER
            val damagePerPlayer = totalDamage / membersInRange.size
            if (membersInRange.size <= 1) {
                ballsTanked++
            }
            for (member in membersInRange) {
                member.applyHit(Hit(CombatUtilities.clampMaxHit(this, member, damagePerPlayer), HitType.REGULAR))
                member.graphics = bombHitGraphic
                // TODO sound
            }
        }, delay)
    }

    private fun playersInRange(target: Player) = room.validTargets.filter { target.location.withinDistance(it, 1) }

    private fun commenceMazePhase() {
        isMazePhase = true
        room.setMazeTileIds(SotetsegRoomTile.DARK_GREY)
        room.refreshHealthBar()
        faceDirection(Direction.SOUTH)
        combatDefinitions.resetStats() // reset defence

        // TODO handle solo
        val target = Utils.getRandomCollectionElement(room.validTargets) ?: return
        PartyOverlayInterface.fadeWhite(target, Colour.GREY.wrap("Sotetseg chooses you..."))
        WorldTasksManager.schedule({
            val shadowRealm = constructShadowRealm(target)!!
            this.shadowRealm = shadowRealm
            translateMaze(shadowRealm)
            PartyOverlayInterface.fade(target, 200, 0, Colour.GREY.wrap("Sotetseg chooses you..."))
        }, 2)
        for (m in room.raid.party.members) {
            val member = getPlayer(m) ?: continue
            if (member == target) continue
            PartyOverlayInterface.fadeWhite(member, "")
            WorldTasksManager.schedule({
                PartyOverlayInterface.fade(member, 200, 0, "")
                member.setLocation(room.getLocation(3274, 4307, 0))
                member.cancelCombat()
            }, 2)
        }
    }

    private fun translateMaze(shadowRealm: ShadowRealmRoom) {
        mazeTiles.clear()
        val path = shadowRealm.mazePath!!
        for (tile in path)
            mazeTiles.add(translateShadowRealmTile(shadowRealm, tile))
    }

    private fun translateShadowRealmTile(shadowRealm: ShadowRealmRoom, input: Location): Location {
        val mazeStart = Location(room.mazeTopLeft.x, room.mazeBottomRight.y)
        val shadowRealmStart = Location(shadowRealm.mazeTopLeft.x, shadowRealm.mazeBottomRight.y)
        return mazeStart.transform(input.x - shadowRealmStart.x, input.y - shadowRealmStart.y)
    }

    private fun mazePhaseProcess() {
        val shadowRealm = shadowRealm
            ?: return // shadow realm hasn't been constructed yet
        if (shadowRealm.shouldStartMazeStorm() && (mazeStorm == null || mazeStorm!!.isFinished)) {
            mazeStorm = RedStorm(shadowRealm, mazeTiles, RedStorm.transformCoord(mazeTiles.first())).apply(NPC::spawn)
        }
        removeDamageTiles()

        val translatedTile = translateShadowRealmTile(shadowRealm, shadowRealm.player.location)
        if (room.isInMaze(translatedTile)) {
            val activeMazeTile = activeMazeTile
            if (activeMazeTile == null || activeMazeTile != translatedTile) {
                if (activeMazeTile != null) {
                    // remove previous red tile
                    World.spawnObject(WorldObject(SotetsegRoomTile.DARK_GREY.id, 22, 0, activeMazeTile))
                    this.activeMazeTile = null
                }
                if (room.isInMaze(translatedTile)) {
                    this.activeMazeTile = translatedTile
                    // add red tile to where shadow realm player is standing
                    World.spawnObject(WorldObject(SotetsegRoomTile.RED.id, 22, 0, translatedTile))
                }
            }
        }

        // TODO check everyone has made it across the maze
        val validTargets = room.validTargets
        for (p in validTargets) {
            if (room.isInMaze(p) && !mazeTiles.contains(p.location)) {
                val surroundingPlayers = validTargets.filter { it != p && p.location.withinDistance(it, 1) }
                addDamageTile(p.location)
                // TODO do gfx
                for (player in surroundingPlayers)
                    player.applyHit(Hit(Utils.random(10, 25), HitType.REGULAR))
                WorldTasksManager.schedule {
                    if (room.isValidTarget(p))
                        p.applyHit(Hit(Utils.random(10, 25), HitType.REGULAR))
                }
            }
        }
    }

    private fun removeDamageTiles() {
        if (shadowRealm == null || !isMazePhase) {
            // delete all tiles
            for (tile in damageTiles) {
                if (!room.isInMaze(tile)) continue
                World.spawnObject(WorldObject(SotetsegRoomTile.DARK_GREY.id, 22, 0, tile))
            }
            damageTiles.clear()
            return
        }
        val playerLocations: MutableList<Location> = ArrayList()
        for (p in room.validTargets) {
            if (p != shadowRealm!!.player)
                playerLocations.add(p.location)
        }
        val toRemove: MutableList<Location> = ArrayList()
        for (tile in damageTiles) {
            if (!playerLocations.contains(tile)) {
                toRemove.add(tile)
                World.spawnObject(WorldObject(SotetsegRoomTile.DARK_GREY.id, 22, 0, tile))
            }
        }
        for (tile in toRemove)
            damageTiles.remove(tile)
    }

    private fun addDamageTile(location: Location) {
        if (damageTiles.add(location))
            World.spawnObject(WorldObject(SotetsegRoomTile.RED_DAMAGE.id, 22, 0, location))
    }

    override fun finish() {
        super.finish()

        if (ballsTanked >= 3) {
            for (p in room.validTargets) {
                p.combatAchievements.complete(CAType.A_TIMELY_SNACK)
            }
        }

        val shadowRealm = shadowRealm ?: return
        completeMaze(shadowRealm.player)
    }

    fun completeMaze(player: Player) {
        player.graphics = Graphics(65535)
        PartyOverlayInterface.fadeWhite(player, "")
        WorldTasksManager.schedule({
            PartyOverlayInterface.fade(player, 200, 0, "")
            player.setLocation(room.getLocation(3275, 4327, 0))
            room.refreshHealthBar()
            room.entered.add(player)
        }, 2)
        room.setMazeTileIds(SotetsegRoomTile.LIGHT_GREY)
        isMazePhase = false
        shadowRealm = null
        mazeStorm?.finish()
        mazeStorm = null
        activeMazeTile = null
        removeDamageTiles()
    }

    private fun constructShadowRealm(player: Player): ShadowRealmRoom? {
        val room = TheatreRoomType.SHADOW_REALM
        return try {
            val allocatedArea = MapBuilder.findEmptyChunk(room.sizeX, room.sizeY)
            ShadowRealmRoom(this@Sotetseg.room.raid, allocatedArea, room, player, this)
                .apply(ShadowRealmRoom::constructRegion)
        } catch (e: Exception) {
            logger.error("", e)
            null
        }
    }

    override fun getMagicPrayerMultiplier() = 0.0

    override fun getMeleePrayerMultiplier() = 0.0

    override fun getRangedPrayerMultiplier() = 0.0

    override fun addWalkStep(nextX: Int, nextY: Int, lastX: Int, lastY: Int, check: Boolean) = false

    fun shouldAttack() = !isDead && !isFinished && !isMazePhase

    override fun checkProjectileClip(player: Player, melee: Boolean) = false

    companion object {

        private val logger = LoggerFactory.getLogger(Sotetseg::class.java)

        private const val MELEE_MAX_HIT = 50
        private const val ATTACK_SPEED = 6
        private const val BOMB_BASE_DAMAGE = 70
        private const val BOMB_EXTRA_DAMAGE_PER_PLAYER = 12

        private val meleeAttackAnimation = Animation(8138)
        private val magicAttackAnimation = Animation(8139)
        private val bombHitGraphic = Graphics(1605)
        private val bombProjectile = Projectile(1604, 40, 30, 0, 0, 15 * 30, 0, 0)

    }

}
