package com.zenyte

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import com.google.common.base.Stopwatch
import com.near_reality.api.GameDatabase
import com.near_reality.api.dao.CreditStoreCheckoutLogEntity
import com.near_reality.api.dao.Db
import com.near_reality.api.model.CreditStoreCategory
import com.near_reality.game.world.info.WorldConfig
import com.near_reality.osrsbox_db.ItemDefinitionDatabase
import com.zenyte.cores.CoresManager
import com.zenyte.game.GameConstants
import com.zenyte.game.GameLoader
import com.zenyte.server.AttributesSerializable
import com.zenyte.server.ServerAttributes
import com.zenyte.utils.ElapsedTimes
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDateTime
import mgi.types.config.items.ItemDefinitions
import net.openhft.chronicle.core.Jvm
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess
import kotlin.time.Duration

object DatabaseRunner {
    private val log = LoggerFactory.getLogger(DatabaseRunner::class.java)

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


    private lateinit var serverAttributes: ServerAttributes

    private fun initializeServerAttributes() {
        serverAttributes = ServerAttributes()
        serverAttributes = AttributesSerializable.getFromFile(ServerAttributes.getSaveFile(), serverAttributes)
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
        runBlocking {
            pullAndParseLogs()
            printTopSalesMonthly2()
            //printTopSalesOverall()
        }
        exitProcess(0)
    }

    private fun printTopSalesWeekly() {
        weeks.sort()
        for(week in weeks) {
            val salesGrouped = checkoutReports.filter { it.weekNumber == week }.groupBy({it.itemId}, {it.totalSpend}).mapValues { it.value.sum() }
            println("|-------------------------------------------------------------------------------------")
            println("|                Sales by Total Credit Spend (Week $week)                             ")
            println("|-------------------------------------------------------------------------------------")
            for ((item, spend) in salesGrouped.entries.sortedByDescending { it.value }) {
                println("|  ${checkoutReports.find { it.itemId == item }!!.itemName} ")
                println(
                    "|  Sale Count: ${
                        checkoutReports.filter { it.weekNumber == week && it.itemId == item }.run {
                            var count = 0
                            this.forEach { count += it.quantityPurchased }
                            count
                        }
                    } "
                )
                println("|  Credits Spent: $spend ")
                println("|  USD Spent: $${spend.toUSD()} ")
                println("|")
            }
            println("|-------------------------------------------------------------------------------------")
            println("")
            println("")
        }

    }

    val timeframes = ObjectArrayList<Pair<LocalDate, LocalDate>>().apply {
        add(Pair(LocalDate(2024,3,15), LocalDate(2024, 4, 14)))
        add(Pair(LocalDate(2024,4,15), LocalDate(2024, 5, 14)))
        add(Pair(LocalDate(2024,5,15), LocalDate(2024, 6, 14)))
        add(Pair(LocalDate(2024,6,15), LocalDate(2024, 7, 14)))
    }

    private fun printTopSalesMonthly2() {
        for(tf in timeframes) {
            val salesGrouped = checkoutReports.filter { it.localDate >= tf.first && it.localDate <= tf.second }.groupBy({it.itemId}, {it.totalSpend}).mapValues { it.value.sum() }
            println("|-------------------------------------------------------------------------------------")
            println("|                Sales by Total Credit Spend (${tf.first.month}-${tf.first.dayOfMonth}-${tf.first.year} through ${tf.second.month}-${tf.second.dayOfMonth}-${tf.second.year})                             ")
            println("|-------------------------------------------------------------------------------------")
            for ((item, spend) in salesGrouped.entries.sortedByDescending { it.value }) {
                println("|  ${checkoutReports.find { it.itemId == item }!!.itemName} ")
                println(
                    "|  Sale Count: ${
                        checkoutReports.filter {  it.localDate >= tf.first && it.localDate <= tf.second && it.itemId == item }.run {
                            var count = 0
                            this.forEach { count += it.quantityPurchased }
                            count
                        }
                    } "
                )
                println("|  Credits Spent: $spend ")
                println("|  USD Spent: $${spend.toUSD()} ")
                println("|")
            }
            println("|-------------------------------------------------------------------------------------")
            println("")
            println("")
        }
    }

    private fun printTopSalesMonthly() {
        months.sort()
        for(month in months) {
            val salesGrouped = checkoutReports.filter { it.weekNumber <= month * 4 }.groupBy({it.itemId}, {it.totalSpend}).mapValues { it.value.sum() }
            println("|-------------------------------------------------------------------------------------")
            println("|                Sales by Total Credit Spend (Month $month)                             ")
            println("|-------------------------------------------------------------------------------------")
            for ((item, spend) in salesGrouped.entries.sortedByDescending { it.value }) {
                println("|  ${checkoutReports.find { it.itemId == item }!!.itemName} ")
                println(
                    "|  Sale Count: ${
                        checkoutReports.filter { it.weekNumber <= month * 4 && it.itemId == item }.run {
                            var count = 0
                            this.forEach { count += it.quantityPurchased }
                            count
                        }
                    } "
                )
                println("|  Credits Spent: $spend ")
                println("|  USD Spent: $${spend.toUSD()} ")
                println("|")
            }
            println("|-------------------------------------------------------------------------------------")
            println("")
            println("")
        }

    }

    private fun printTopSalesOverall() {
            val salesGrouped = checkoutReports.groupBy({it.itemId}, {it.totalSpend}).mapValues { it.value.sum() }
            println("|-------------------------------------------------------------------------------------")
            println("|                Sales by Total Credit Spend (Overall)                             ")
            println("|-------------------------------------------------------------------------------------")
            for ((item, spend) in salesGrouped.entries.sortedByDescending { it.value }) {
                println("|  ${checkoutReports.find { it.itemId == item }!!.itemName} ")
                println(
                    "|  Sale Count: ${
                        checkoutReports.filter { it.itemId == item }.run {
                            var count = 0
                            this.forEach { count += it.quantityPurchased }
                            count
                        }
                    } "
                )
                println("|  Credits Spent: $spend ")
                println("|  USD Spent: $${spend.toUSD()} ")
                println("|")
            }
            println("|-------------------------------------------------------------------------------------")
            println("")
            println("")

    }

    private fun Long.toUSD() : Long {
        return this / 13
    }
    private val checkoutReports = mutableListOf<CheckoutLogReport>()
    private val uniqueSaleIds = mutableListOf<Int>()
    private val weeks = mutableListOf<Int>()
    private val months = mutableListOf<Int>()

    data class CheckoutLogReport(val localDate: LocalDate, val weekNumber: Int, val username: String, val itemId: Int, val quantityPurchased: Int, val quantityEach: Int, val totalSpend: Long) {
        val itemName
            get() = run {
                val baseName = ItemDefinitions.get(itemId)?.name ?: "null"
                if(quantityEach > 1) {
                    "$baseName ($quantityEach pack)"
                } else baseName
            }
    }

    private suspend fun pullAndParseLogs() =
        Db.dbQueryLogs {
            CreditStoreCheckoutLogEntity.all().forEach{
                it.cart.forEach { item ->
                    if(!uniqueSaleIds.contains(item.product.itemId) && !item.product.categories.contains(CreditStoreCategory.Pins)) {
                        uniqueSaleIds.add(item.product.itemId)
                    }
                    if(!item.product.categories.contains(CreditStoreCategory.Pins)) {
                        checkoutReports.add(
                            CheckoutLogReport(
                                it.time.date,
                                determineWeek(it.time),
                                it.username,
                                item.product.itemId,
                                item.amount,
                                item.product.itemAmount,
                                (item.product.price * item.amount).toLong()
                            )
                        )
                    }
                    }
            }
        }

    private fun determineWeek(time: LocalDateTime): Int {
        val launch = LocalDateTime(LocalDate(2024, 3, 15), LocalTime(0, 0))
        val days = java.time.Duration.between(launch.toJavaLocalDateTime(), time.toJavaLocalDateTime())
        val weekNumber = (days.toDays() / 7).plus(1).toInt()
        val monthNumber = (weekNumber / 4).plus(1)
        if(!weeks.contains(weekNumber))
            weeks.add(weekNumber)
        if(!months.contains(monthNumber))
            months.add(monthNumber)
        return weekNumber
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

    private fun logElapsed(message: String, runnable: Runnable) =
        ElapsedTimes.runLogElapsed(log, message, runnable = runnable)
}