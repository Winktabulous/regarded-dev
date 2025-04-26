package com.near_reality.tools

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import com.near_reality.game.world.info.WorldProfile
import com.near_reality.osrsbox_db.ItemDefinitionDatabase
import com.near_reality.osrsbox_db.MonsterDefinitionDatabase
import com.zenyte.CacheManager
import com.zenyte.game.GameConstants
import com.zenyte.game.content.xamphur.Xamphur
import com.zenyte.game.item.Item
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.ReceivedDamage
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.AbstractNPCManager
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.PlayerInformation
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.PluginLoader
import com.zenyte.plugins.PluginManager
import com.zenyte.plugins.PluginType
import com.zenyte.plugins.events.PluginsLoadedEvent
import com.zenyte.utils.TextUtils
import io.mockk.MockKDsl
import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import it.unimi.dsi.fastutil.ints.IntLongImmutablePair
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.*
import mgi.types.config.enums.EnumDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import mgi.utilities.StringFormatUtil
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap

const val simulationCount = 20_000

object DropTableSimulator {

    val totalDrops = ConcurrentHashMap<Player,  ConcurrentHashMap<Int, IntLongImmutablePair>>()

    @JvmStatic
    fun main(args: Array<String>) {
        val file = File( "app/logback-dev.xml")
        val context: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val jc = JoranConfigurator()
        jc.context = context
        context.reset()
        jc.doConfigure(file.absolutePath)

        CacheManager.loadCache(Cache.openCache("cache/data/cache"))

        AnimationDefinitions().load()
        EnumDefinitions().load()
        ObjectDefinitions().load()
        StructDefinitions().load()
        VarbitDefinitions().load()

        ItemDefinitions().load()
        ItemDefinitionDatabase.loadFromFile()
        ItemDefinitionDatabase.buildConfigs()
        InventoryDefinitions().load()

        NPCDefinitions().load()
        NPCCDLoader.parse()
        NPCDrops.init()
        MonsterDefinitionDatabase.loadFromFile()

        PluginLoader.load(PluginType.DROP_PROCESSOR, PluginType.SPAWNABLE)

        val npcsIds = intArrayOf(
            NpcId.CERBERUS_5863,
//            NpcId.THE_NIGHTMARE,
//            NpcId.ZULRAH,
//            NpcId.DAGANNOTH_REX
//            NpcId.XAMPHUR
        )

        MonsterDefinitionDatabase.buildConfigs()

        PluginManager.post(PluginsLoadedEvent())

        GameConstants.WORLD_PROFILE = WorldProfile()

        doSimulations(*npcsIds)
    }

    private fun doSimulations(vararg npcIds: Int) {

        val sourceMap = npcIds.associateWith {
            if (NPCDrops.getTable(it) != null)
                "base\\cache\\data\\npcs\\drops"
            else if (MonsterDefinitionDatabase.definitions[it]?.drops != null)
                "monsters-complete.json"
            else
                "DropProcessor? Or none?"
        }

        val playersWithDamageContribution = mapOf(

//            mockPlayer("Stan") to 1.0,     // 30

            mockPlayer("Stan") to 0.50,     // 30
            mockPlayer("Harsh") to 0.30,    // 50
            mockPlayer("Jacmob") to 0.20,       // 60

//            mockPlayer("Stan") to 0.30,     // 30
//            mockPlayer("Harsh") to 0.20,    // 50
//            mockPlayer("Jacmob") to 0.10,       // 60
//            mockPlayer("Leanbow") to 0.05,  // 65
//            mockPlayer("Kryeus") to 0.05,   // 70
//            mockPlayer("Patrity") to 0.05,  // 75
//            mockPlayer("Player 1") to 0.025,// 77.5
//            mockPlayer("Player 2") to 0.025,// 80
//            mockPlayer("Player 3") to 0.005,// 80.5
//            mockPlayer("Player 4") to 0.005,// 81
//            mockPlayer("Player 5") to 0.05, // 86
//            mockPlayer("Player 6") to 0.05, // 91
//            mockPlayer("Player 7") to 0.09, // 91
        )
        simulateKills(npcIds, playersWithDamageContribution, sourceMap)
    }

    private fun mockPlayer(
        name: String,
        privilege: PlayerPrivilege = PlayerPrivilege.PLAYER,
        gameMode: GameMode = GameMode.REGULAR,
        memberRank: MemberRank = MemberRank.NONE
    ): Player {
        val formattedName = TextUtils.formatNameForProtocol(name)
        val player = Player(PlayerInformation(formattedName, "", 0, null, null), null)
            .apply {
                this.gameMode = gameMode
                this.memberRank = memberRank
                this.privilege = privilege
            }
        World.namedPlayers[formattedName] = player

        return player
    }

    lateinit var npc: NPC

    private fun simulateKills(
        npcIds: IntArray,
        playersWithDamageContribution: Map<Player, Double>,
        sourceMap: Map<Int, String>,
    ) {
        val mvp = playersWithDamageContribution.maxBy { it.value }.key
        for (npcId in npcIds) {

            val npcDefinition = NPCDefinitions.get(npcId)
            val npcCombatDefinition = NPCCDLoader.get(npcId)

            val receivedDamage: Object2ObjectMap<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>> = Object2ObjectOpenHashMap()
            for ((player, contribution) in playersWithDamageContribution) {
                val damageDealt = (npcCombatDefinition.hitpoints * contribution).toInt()
                receivedDamage[player.username to player.gameMode] = ObjectArrayList.of(
					ReceivedDamage(
						System.currentTimeMillis(),
						damageDealt,
                        HitType.REGULAR
					)
				)
            }

            val table = NPCDrops.getTable(npcId)
            val dropProcessors = DropProcessorLoader.get(npcId)
            println("-----------------------------------------------------")
            println("${npcDefinition.name.padEnd(30)} - $npcId")
            println("Using: ${sourceMap[npcId]}")
            println("DropsProcessors: ${dropProcessors.map { it::class.simpleName }}")
            println("-----------------------------------------------------")
            spykNpc(npcId, npcDefinition, npcCombatDefinition, mvp, receivedDamage)
            repeat(simulationCount) {
                simulateKill(it, table, dropProcessors, npc, mvp)
                MockKDsl.internalClearAllMocks(
                    answers = false,
                    recordedCalls = true,
                    childMocks = false,
                    regularMocks = true,
                    objectMocks = false,
                    staticMocks = false,
                    constructorMocks = false,
                    verificationMarks = true,
                    exclusionRules = true
                )
            }
        }
        printDrops()
    }

    private fun spykNpc(
        npcId: Int,
        npcDefinition: NPCDefinitions,
        npcCombatDefinition: NPCCombatDefinitions,
        mvp: Player,
        receivedDamage: Object2ObjectMap<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>>,
    ) {
        val killerSlot = slot<Player>()
        val itemSlot = slot<Item>()
        val location = when(npcId) {
            NpcId.CERBERUS_5863 -> Location(1302, 1314)
            else -> GameConstants.REGISTRATION_LOCATION
        }
        npc = when(npcId) {
            NpcId.XAMPHUR_10955 -> spyk(Xamphur())
            else -> (AbstractNPCManager.get(npcId, npcDefinition.name)
                ?.let {
                    spyk(it.getDeclaredConstructor(*World.npcInvocationArguments).let { constructor ->
                        constructor.isAccessible = true
                        constructor.newInstance(npcId, location, Direction.SOUTH, 0)
                    })
                }
                ?: spyk(NPC(npcId, location, Direction.SOUTH, 0)))
        }.apply {
            combatDefinitions = npcCombatDefinition
        }
        npc.receivedDamage.clear()
        npc.receivedDamage.putAll(receivedDamage)
        every { npc.dropItem(capture(killerSlot), capture(itemSlot)) } answers {
            addDrops(killerSlot.captured, itemSlot.captured)
        }
        every { npc.dropItem(capture(killerSlot), capture(itemSlot), any(), any()) } answers {
            addDrops(killerSlot.captured, itemSlot.captured)
        }
    }

    private fun simulateKill(
        it: Int,
        table: NPCDrops.DropTable?,
        dropProcessors: MutableList<DropProcessor>?,
        npc: NPC,
        mvp: Player,
    ) {
        if (it % 1_000 == 0) {
            println("Simulation $it/$simulationCount")
        }
        try {
            if (table != null)
                rollTables(table, dropProcessors, npc, mvp)

            dropProcessors?.forEach { processor ->
                processor.onDeath(npc, mvp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rollTables(
        table: NPCDrops.DropTable?,
        dropProcessors: MutableList<DropProcessor>?,
        npc: NPC,
        mvp: Player,
    ) {
        val drops = mutableListOf<Drop>()
        NPCDrops.rollTable(mvp, table) { drop ->
            drops += drop
        }
        drops.mapNotNull { drop ->
            var item = Item(drop.itemId, Utils.random(drop.minAmount, drop.maxAmount).coerceAtLeast(1))
            if (dropProcessors != null) {
                val baseItem = item
                for (processor in dropProcessors) {
                    val newItem = processor.drop(npc, mvp, drop, item)
                    if (newItem == null)
                        return@mapNotNull null
                    else
                        item = newItem
                    if (item !== baseItem)
                        break
                }
            }
            item
        }.forEach { item ->
            addDrops(mvp, item)
        }
    }

    private fun addDrops(player: Player, item: Item) {
        val dropMap = totalDrops.getOrPut(player) { ConcurrentHashMap() }
        dropMap.compute(item.id) { _, pair ->
            val previousTimesDropped = pair?.leftInt() ?: 0
            val previousAmountDropped = pair?.rightLong() ?: 0L
            IntLongImmutablePair(previousTimesDropped + 1, previousAmountDropped + item.amount)
        }
    }

    private fun printDrops() {
        println("--------------------------------------------------------------------")
        for ((player, drops) in totalDrops) {
            println("Player: ${player.name}")
            println("--------------------------------------------------------------------")
            println("${"".padEnd(30)} chance\t ${"".padEnd(10)}\t total amount dropped")
            drops
                .entries
                .sortedBy { it.value.firstInt() }
                .forEach { (itemId, timesAmountPair) ->
                    val timesDropped = timesAmountPair.leftInt()
                    val amountDropped = timesAmountPair.rightLong()
                    val percentageDropRate = timesDropped.toDouble().div(simulationCount).times(100).toFloat().let { "%.4f".format(it) }.padEnd(6)
                    val every = simulationCount.toDouble() / timesDropped
                    println(
                        ItemDefinitions.nameOf(itemId)?.let { if (it == "null") "$itemId" else it }?.padEnd(30) +
                                " ${"$percentageDropRate%".padEnd(6)}\t ${"(1/${every.toInt()})".padEnd(10)}" +
                                "\t ${StringFormatUtil.format(amountDropped)}"
                    )
                }
            println("--------------------------------------------------------------------")
        }
    }
}
