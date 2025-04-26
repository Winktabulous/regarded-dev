package com.near_reality.game.content.dt2.area

import com.near_reality.game.content.delay
import com.near_reality.game.content.dt2.area.obj.VardorvisBoundaryObj
import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.vardorvis.Vardorvis
import com.near_reality.game.content.dt2.npc.entangled
import com.near_reality.game.content.dt2.npc.entangledFreedomCounter
import com.near_reality.game.content.seq
import com.near_reality.game.world.Boundary
import com.zenyte.game.content.boons.impl.CantBeAxed
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.Tinting
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.DynamicArea
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import com.zenyte.plugins.dialogue.PlainChat

/**
 * Location - 1122, 3412, 0
 * NPC -
 * GraphicsObject for Spike 2510
 * @author John J. Woloszyk / Kryeus
 */
class VardorvisInstance(
    allocatedArea: AllocatedArea,
    val player: Player,
    val difficulty: DT2BossDifficulty = DT2BossDifficulty.NORMAL,
    private val debugMechanics : Boolean = true
) : DynamicArea(allocatedArea, 137, 424),
    FullMovementPlugin,
    CannonRestrictionPlugin {

    private var vardorvis: Vardorvis = Vardorvis(this.getLocation(1129, 3419, 0), this)

    var killAxes: Boolean = false
    var killBleed: Boolean = false


    companion object {
        fun createInstance(p: Player) : VardorvisInstance {
            val area: AllocatedArea = MapBuilder.findEmptyChunk(8, 6)
            return VardorvisInstance(area, p)
        }

        fun outside() = outsideLocation
        fun inside() = insideLocation

        private val insideLocation = Location(1128, 3416, 0)
        private val outsideLocation = Location(1150, 3444, 0)
    }

    override fun enter(player: Player) {

    }

    override fun leave(player: Player, logout: Boolean) {
        if(player.hpHud.isOpen)
            player.hpHud.close()
        player.blockIncomingHits(1)
        player.interfaceHandler.closeInterface(InterfacePosition.OVERLAY)
        player.packetDispatcher.sendClientScript(7021, 255)
        if (logout) {
            player.forceLocation(outside())
        }
    }

    override fun destroyRegion() {
        super.destroyRegion()
        vardorvis.finish()
        removeSpikes()
    }

    private fun removeSpikes() {
        for(spike in spikeObjects)
            World.removeObject(spike)
    }

    override fun name(): String = "Vardorvis Instance"

    override fun constructed() {
        vardorvis = Vardorvis(this.getLocation(1129, 3419, 0), this)
        vardorvis.spawn()

        initSpikes()

        transferPlayer()
    }

    private fun transferPlayer() {
        val screen = FadeScreen(player) {
            player.setLocation(getLocation(inside()))
            player.faceDirection(Direction.WEST)
            player.dialogueManager
                .start(PlainChat(player, "You crawl through the tunnels to an icy bluff."))
        }
        player.dialogueManager.start(PlainChat(player, "You crawl through the tunnels to an icy bluff.", false))
        screen.fade()
        schedule({ screen.unfade() }, 2)
    }

    private var spikePositions = mutableListOf<Location>()
    private var spikeObjects = mutableListOf<WorldObject>()

    data class AxeLocationInformation(
        val headLocation: Location,
        val axeLocation: Location,
        val destination: Location,
        val direction: Direction,
        val corner: Boolean = false
    )

    private val baseAxes = listOf(
        /* NorthWest -> SouthEast */
        AxeLocationInformation(
            Location(1124, 3421),
            Location(1124, 3421),
            Location(1132, 3413),
            Direction.SOUTH_EAST,
            true
        ),

        /* North -> South */
        AxeLocationInformation(
            Location(1128, 3421),
            Location(1128, 3421),
            Location(1128, 3413),
            Direction.SOUTH
        ),

        /* NorthEast -> SouthWest (good) */
        AxeLocationInformation(
            Location(1132, 3421),
            Location(1132, 3421),
            Location(1124, 3413),
            Direction.SOUTH_WEST,
            true
        ),

        /* East -> West (good) */
        AxeLocationInformation(
            Location(1132, 3417),
            Location(1132, 3417),
            Location(1124, 3417),
            Direction.WEST
        ),

        /* SouthEast -> NorthWest (good) */
        AxeLocationInformation(
            Location(1132, 3413),
            Location(1132, 3413),
            Location(1124, 3421),
            Direction.NORTH_WEST,
            true
        ),

        /* South -> North (good) */
        AxeLocationInformation(
            Location(1128, 3413),
            Location(1128, 3413),
            Location(1128, 3421),
            Direction.NORTH
        ),

        /* SouthWest -> NorthEast (good) */
        AxeLocationInformation(
            Location(1124, 3413),
            Location(1125, 3414),
            Location(1133, 3422),
            Direction.NORTH_EAST,
            true
        ),

        /* West -> East */
        AxeLocationInformation(
            Location(1124, 3417),
            Location(1124, 3417),
            Location(1132, 3417),
            Direction.EAST
        )
    )

    val translatedAxes = buildMap<AxeLocationInformation, Location> {
        for(axe in baseAxes)
            put(axe, getLocation(axe.axeLocation))
    }

    fun applyAxeDamage() {
        val scalar =
            if(player.boonManager.hasBoon(CantBeAxed::class.java))
                0.66
            else
                1.00

        val maxDamage =
            if(player.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
                (17 * scalar).toInt()
            else
                (35 * scalar).toInt()

        val applyDamage = Utils.random(maxDamage).coerceAtLeast(1)

        player.applyHit(Hit(applyDamage, HitType.TYPELESS))
        if(!player.boonManager.hasBoon(CantBeAxed::class.java))
            player.variables.schedule(15, TickVariable.VARDORVIS_BLEED)
    }

    private fun initSpikes() {
        spikePositions = mutableListOf()
        spikeObjects = mutableListOf()
        val spacing = 1

        val boundary = Boundary(1123, 3424, 1135, 3412)

        val x1 = boundary.minX
        val x2 = boundary.highX
        val y1 = boundary.minY
        val y2 = boundary.highY

        for (i in x1..x2 step spacing) {
            /* Min Y line from x1 to x2 */
            spikePositions.add(Location(i, y1))
            /* Min Y line from x1 to x2 */
            spikePositions.add(Location(i, y2))
        }

        for (i in y1..y2 step spacing) {
            /* Min X line from y1 to y2 */
            spikePositions.add(Location(x1, i))
            /* Max X line from y1 to y2 */
            spikePositions.add(Location(x2, i))
        }
        for (position in spikePositions) {
            val obj = VardorvisBoundaryObj(id = spikeIds.random(), tile = getLocation(position))
            spikeObjects.add(obj)
            World.spawnObject(obj)
        }
    }


    private val spikeIds = listOf(47599, 47600, 47601, 47602)

    /* TODO add wall awakened mechanic */
    override fun processMovement(player: Player, x: Int, y: Int): Boolean {
        if (player.entangled) {
            if(player.entangledFreedomCounter++ >= 3) {
                resetEntanglement(true)
                return true
            }
            else
                player.sendMessage(Colour.RS_GREEN.wrap("You feel Vardorvis' grip loosen slightly..."))
            return false
        }

        if (!player.entangled && !player.bleedNextTick) {
            debugMechanic("Marking player for bleed damage next tick")
            player.bleedNextTick = true
        }
        return true
    }

    fun debugMechanic(msg: String) {
        if(debugMechanics)
            player.sendDeveloperMessage(msg)
    }

    fun resetEntanglement(message: Boolean) {
        player.entangled = false
        player.entangledFreedomCounter = 0
        player.tinting = Tinting(-1, -1, -1, 0,0,0)
        if (message)
            player.sendMessage(Colour.GREEN.wrap("You have broken free of Vardorvis's grip!"))
        vardorvis seq 10344
        vardorvis.animation =  Animation(10345, 3)
        vardorvis.attack(player)
        player.unlock()
    }
}

