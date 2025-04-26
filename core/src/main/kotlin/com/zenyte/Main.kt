package com.zenyte

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import com.google.common.base.Stopwatch
import com.near_reality.api.GameDatabase
import com.near_reality.game.world.info.WorldConfig
import com.near_reality.network.NetworkService
import com.near_reality.osrsbox_db.ItemDefinitionDatabase
import com.near_reality.osrsbox_db.MonsterDefinitionDatabase
import com.zenyte.cores.CoresManager
import com.zenyte.game.GameConstants
import com.zenyte.game.GameLoader
import com.zenyte.game.content.consumables.Consumable
import com.zenyte.game.content.grandexchange.GrandExchangeHandler
import com.zenyte.game.content.multicannon.DwarfMultiCannon
import com.zenyte.game.content.skills.mining.MiningDefinitions
import com.zenyte.game.model.item.ItemActionHandler
import com.zenyte.game.model.shop.Shops
import com.zenyte.game.model.ui.testinterfaces.DropViewerInterface
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader
import com.zenyte.game.world.flooritem.GlobalItem
import com.zenyte.game.world.`object`.Door
import com.zenyte.game.world.`object`.ObjectExamineLoader
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.XTEALoader
import com.zenyte.game.world.region.areatype.AreaTypes
import com.zenyte.plugins.PluginLoader
import com.zenyte.plugins.PluginManager
import com.zenyte.plugins.events.PluginsLoadedEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.server.AttributesSerializable
import com.zenyte.server.ServerAttributes
import com.zenyte.utils.ElapsedTimes.runLogElapsed
import io.netty.util.ResourceLeakDetector
import mgi.types.Definitions
import mgi.types.config.items.ItemDefinitions
import net.openhft.chronicle.core.Jvm
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

/**
 * @author Jire
 * @author Tommeh
 */
object Main {

    private val log = LoggerFactory.getLogger(Main::class.java)

    @JvmField
    val serverStartTime = System.nanoTime()

    init {
        Jvm.init()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Thread.currentThread().name = "Main Launch Thread"
        initializeServerAttributes()
        try {
            launch(args)
        } catch (t: Throwable) {
            log.error("", t)
            t.printStackTrace()
        }
    }

    private fun launch(args: Array<String>) {
        val stopwatch = Stopwatch.createStarted()

        Locale.setDefault(Locale.US)
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"))

        System.setProperty("io.netty.allocator.maxCachedBufferCapacity", "65535")

        configureWorldProfile(*args)

        GameDatabase.init(GameConstants.WORLD_PROFILE)
//        UserLoginQueue.start()

        val pool = Executors.newWorkStealingPool()

        CoresManager.init()

        logElapsed("Loaded game.") { GameLoader.load(pool) }
        logElapsed("Loaded item definitions.") {
            ItemDefinitions.loadDefinitions(
                pool,
                ItemDefinitionDatabase::loadFromFile
            ) { ItemDefinitionDatabase.buildConfigs() }

        }
        logElapsed("Created game engine.") {
            pool.invokeAll(World::init, Consumable::initialize, GlobalItem::load)
        }

        load(pool)

        val worldThread = CoresManager.startWorldThread()
        val serverLaunchEvent = ServerLaunchEvent(worldThread)
        logElapsed("Posted server launch event.") {
            PluginManager.post(serverLaunchEvent)
        }

        val port = GameConstants.WORLD_PROFILE.port
        logElapsed("Starting network service...") {
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED) // XXX: in production, use DISABLED

            val networkService = NetworkService()
            networkService.initialize()

            networkService.listen(port)
            networkService.listen(443)

            val worldID = GameConstants.WORLD_PROFILE.number
            networkService.listen(40000 + worldID)
            networkService.listen(50000 + worldID)
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        log.info("${GameConstants.SERVER_NAME} took {} milliseconds to launch.", elapsed)

        pool.execute(::notifyErrorLogs)

        pool.shutdown()
        if (!pool.awaitTermination(5, TimeUnit.MINUTES)) {
            log.error("Could not finish loading server within timeout!")
            exitProcess(-1)
        }
    }
    private lateinit var serverAttributes: ServerAttributes

    private fun initializeServerAttributes() {
        serverAttributes = ServerAttributes()
        serverAttributes = AttributesSerializable.getFromFile(ServerAttributes.getSaveFile(), serverAttributes)
    }

    fun load(pool: ExecutorService) {
        logElapsed("Loaded low priority definitions.") {
            pool.invokeAll(*Definitions.lowPriorityDefinitions.map(Definitions::load).toTypedArray())
        }
        logElapsed("Loaded NPC combat definitions.") {
            pool.invokeAll(
                NPCCDLoader::parse,
                MiningDefinitions::load,
                DwarfMultiCannon::init,
                NPCSpawnLoader::parseDefinitions,
                MonsterDefinitionDatabase::loadFromFile
            )
            MonsterDefinitionDatabase.buildConfigs()
        }
        World.initTasks()
        logElapsed("Loaded shops, examines, drops, grand exchange, XTEAs, and area types.") {
            pool.invokeAll(
                Shops::load,
                ObjectExamineLoader::loadExamines,
                NPCDrops::init,
                GrandExchangeHandler::init,
                Door::load,
                { XTEALoader.load("cache/data/objects/xteas.json") },
                AreaTypes.MULTIWAY,
                AreaTypes.SINGLES_PLUS
            )
        }
        PluginLoader.load()
        PluginManager.post(PluginsLoadedEvent())

        ItemActionHandler.setDefaults()
        GlobalAreaManager.setInheritance()
        NPCPlugin.filter()
        NPCSpawnLoader.loadNPCSpawns()
        //SQLManager.init()

        logElapsed("Populated NPC drop viewer data, and mapped global area manager.") {
            pool.invokeAll(
                DropViewerInterface::populateDropViewerData,
                GlobalAreaManager::map
            )
        }

        CoresManager.getLoginManager().launch()
    }

    private fun notifyErrorLogs() {
        val errorLogPath = Path.of("data", "logs", "error.log")
        if (Files.exists(errorLogPath) && Files.size(errorLogPath) > 0) {
            log.warn("Some errors from previous session(s) are logged at {}; review and delete them.", errorLogPath)
        }
    }

    @JvmStatic
    fun configureWorldProfile(vararg args: String) {
        val key = if (args.isNotEmpty()) args[0] else "localhost"
        val worldConfig = WorldConfig.fromYAML(File("worlds.yml"))
        val worldProfile = worldConfig[key] ?: error("World profile '$key' not found.")
        GameConstants.WORLD_PROFILE = worldProfile
        CacheManager.DEVELOPMENT_MODE = worldProfile.isDevelopment()
        configureLogging()
    }

    private fun configureLogging() {
        val file = File( "./app/${GameConstants.WORLD_PROFILE.logback}.xml")
        val context: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val jc = JoranConfigurator()
        jc.context = context
        context.reset()
        jc.doConfigure(file.absolutePath)
    }

    private fun ExecutorService.invokeAll(vararg runnables: Runnable) = invokeAll(runnables.map(::callable))

    private fun callable(runnable: Runnable): Callable<Void> = Callable {
        try {
            runnable.run()
        } catch (e: Exception) {
            log.error("Failure loading callable", e)
        }
        null
    }

    private fun logElapsed(message: String, runnable: Runnable) = runLogElapsed(log, message, runnable = runnable)

}
