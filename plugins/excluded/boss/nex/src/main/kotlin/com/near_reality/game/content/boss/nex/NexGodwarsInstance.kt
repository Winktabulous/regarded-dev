package com.near_reality.game.content.boss.nex

import com.zenyte.game.content.godwars.GodType
import com.zenyte.game.content.godwars.GodwarsInstancePortal.ANCIENT
import com.zenyte.game.content.godwars.instance.GodwarsInstance
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.ImmutableLocation
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import com.zenyte.utils.TimeUnit
import it.unimi.dsi.fastutil.ints.IntArrayList
import org.slf4j.LoggerFactory

/**
 * Represents a [GodwarsInstance] for the Nex boss.
 *
 * @author Stan van der bend
 *
 * @see com.zenyte.game.content.godwars.GodwarsInstancePortal instantiation of the area.
 */
@Suppress("unused")
class NexGodwarsInstance(clan: String, allocatedArea: AllocatedArea)
    : GodwarsInstance(clan, GodType.ANCIENT, allocatedArea, ANCIENT.chunkX, ANCIENT.chunkY) {

    internal val logger = LoggerFactory.getLogger(NexGodwarsInstance::class.java)
    private var nex: NexNPC? = null
    private var spawning: Boolean = false
    private val spawnTask = object : WorldTask {
        var tick = 0
        override fun run() {
            logger.info("Sequencing spawn task (tick = {})", tick)
            if (playersInsideInt() <= 0) {
                logger.info("Stopping spawn task because not enough players in prison.")
                stop()
                return
            } else if (++tick >= NexModule.NEX_SPAWN_DELAY) {
                spawnNex()
                stop()
            } else if (tick % 10 == 0) {
                getPlayers().forEach {
                    it.sendMessage("Nex will spawn in ${TimeUnit.TICKS.toSeconds((NexModule.NEX_SPAWN_DELAY - tick).toLong())} seconds.")
                }
            }
        }

        override fun stop() {
            super.stop()
            tick = 0
            spawning = false
        }
    }

    private val varTask = WorldTask {
        for (player in players) {
            player.varManager.sendBitInstant(13184, if (isNexSpawned()) 2 else 0)
        }
    }

    fun spawnNex() {
        logger.info("Spawning nex, last = {}", nex)
        WorldTasksManager.stop(spawnTask)
        spawning = false
        nex?.finish()
        nex = World.spawnNPC(
            NexNPC.NO_ATK,
            Location(getX(2924), getY(5202)),
            Direction.WEST,
            0
        ) as NexNPC
        nex!!.init(this)
        nex!!.switchStage(NexStage.SPAWN)
    }

    fun tryStartSpawnTimer() {
        if (!isNexSpawned()) {
            if (!spawning) {
                logger.info("Spawning nex")
                spawning = true
                WorldTasksManager.schedule(spawnTask, 3, 1)
            } else
                logger.info("Nex is already spawning")
        } else
            logger.info("Nex is already spawned")
    }

    fun playersInsideInt(): Int {
        var count = 0
        for (player in players) {
            if (polygon.contains(player)) {
                count++
            }
        }
        return count
    }

    fun playersInside(): List<Player> {
        val list = mutableListOf<Player>()
        for (player in players) {
            if (polygon.contains(player)) {
                list.add(player)
            }
        }
        return list
    }

    fun getNex(): NexNPC? {
        return nex
    }

    override fun sendDeath(player: Player, source: Entity): Boolean {
        if (isNexSpawned()) {
            nex!!.playerDied()
        }

        return super.sendDeath(player, source)
    }

    fun isNexSpawned(): Boolean {
        if (nex == null) {
            return false
        }

        return !nex!!.isFinished
    }

    fun getNexStatus(): String = nex?.phase?.name?.lowercase() ?: ""

    override fun name() =
        "GodWarsInstance Nex (CC: $clan)"

    override fun onLoginLocation() =
        Location(2904, 5203)

    override fun constructed() {
        super.constructed()

        polygon = RSPolygon(
            arrayOf(
                intArrayOf(getX(2909), getY(5189)),
                intArrayOf(getX(2909), getY(5217)),
                intArrayOf(getX(2939), getY(5217)),
                intArrayOf(getX(2939), getY(5189))
            )
        )

        killcountNPCS.add(NPC(NpcId.ASHUELOT_REIS_11289, getLocation(Location(2904, 5203, 0)), Direction.SOUTH, 4).spawn())
        WorldTasksManager.schedule(varTask, 0, 1)
    }

    override fun possibleKillcountMonsters(): IntArrayList = IntArrayList.of(
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,
        NpcId.SPIRITUAL_RANGER_11291,

        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,
        NpcId.SPIRITUAL_WARRIOR_11290,

        NpcId.SPIRITUAL_MAGE_11292,
        NpcId.SPIRITUAL_MAGE_11292,
        NpcId.SPIRITUAL_MAGE_11292,
        NpcId.SPIRITUAL_MAGE_11292,
        NpcId.SPIRITUAL_MAGE_11292,

        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
        NpcId.BLOOD_REAVER,
    )

    override fun possibleSpawnTiles(): List<Location> = listOf(
        ImmutableLocation(2881, 5208, 0),
        ImmutableLocation(2883, 5223, 0),
        ImmutableLocation(2889, 5220, 0),
        ImmutableLocation(2891, 5203, 0),
        ImmutableLocation(2856, 5204, 0),
        ImmutableLocation(2862, 5207, 0),
        ImmutableLocation(2863, 5199, 0),
        ImmutableLocation(2865, 5215, 0),
        ImmutableLocation(2873, 5218, 0),

        ImmutableLocation(2881, 5218, 0),
        ImmutableLocation(2883, 5204, 0),
        ImmutableLocation(2890, 5208, 0),
        ImmutableLocation(2894, 5200, 0),
        ImmutableLocation(2852, 5208, 0),
        ImmutableLocation(2860, 5203, 0),
        ImmutableLocation(2867, 5219, 0),
        ImmutableLocation(2869, 5212, 0),
        ImmutableLocation(2873, 5204, 0),

        ImmutableLocation(2880, 5213, 0),
        ImmutableLocation(2887, 5202, 0),
        ImmutableLocation(2864, 5210, 0),
        ImmutableLocation(2869, 5202, 0),
        ImmutableLocation(2876, 5223, 0),

        ImmutableLocation(2885, 5197, 0),
        ImmutableLocation(2885, 5208, 0),
        ImmutableLocation(2887, 5215, 0),
        ImmutableLocation(2895, 5207, 0),
        ImmutableLocation(2854, 5206, 0),
        ImmutableLocation(2858, 5208, 0),
        ImmutableLocation(2859, 5200, 0),
        ImmutableLocation(2864, 5203, 0),
        ImmutableLocation(2870, 5222, 0),
        ImmutableLocation(2873, 5208, 0),
        ImmutableLocation(2875, 5214, 0),
        ImmutableLocation(2878, 5201, 0),
    )

    override fun destroyed() {
         WorldTasksManager.stop(varTask)
    }
}
