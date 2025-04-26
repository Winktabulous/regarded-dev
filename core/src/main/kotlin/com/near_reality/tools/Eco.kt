package com.near_reality.tools

import com.near_reality.api.service.vote.totalVoteCredits
import com.near_reality.api.util.defaultTimeZone
import com.near_reality.api.util.usernameSanitizer
import com.near_reality.game.item.CustomItemId
import com.near_reality.game.model.item.getItemValue
import com.near_reality.game.world.entity.player.exchangePoints
import com.near_reality.tools.discord.calculateValue
import com.near_reality.tools.discord.formatAmountText
import com.zenyte.game.content.grandexchange.ExchangeOffer
import com.zenyte.game.content.grandexchange.ExchangeType
import com.zenyte.game.content.grandexchange.JSONGEItemDefinitionsLoader
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.DefaultGson
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import kotlinx.coroutines.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import mgi.types.config.items.ItemDefinitions
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.zip.ZipFile
import kotlin.math.absoluteValue

object Eco {
    @JvmStatic
    fun main(args: Array<String>) {
        WealthScanner.loadDefault()
        JSONGEItemDefinitionsLoader().parse()

        val snapshot1 =
                    //EconomySnapshot.ofBackupArchive(File("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/data/backup_20240530_181501.zip"))

            EconomySnapshot.ofBackupArchive(File("C:\\Users\\Kryeus\\Documents\\NR\\backup_20240610_152501.zip"))

        val result = snapshot1.calculate()
        println(result)

//        compareEcoSnapshots()
//        println("Snapshot 2:")
//        println(snapshot2.calculate())
    }

    private fun compareEcoSnapshots() {
        val snapshot1 = EconomySnapshot.ofBackupArchive(File("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/data/backup_20240518_060401.zip"))
        val snapshot2 = EconomySnapshot.ofBackupArchive(File("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/data/backup_20240518_070401.zip"))
        val result1 = snapshot1.calculate()
        val result2 = snapshot2.calculate()
        val difference = result2 - result1
        println("|-------------------------------------------------------------------------------------|")
        println(
            "| Economy difference between ${snapshot1.time} and ${snapshot2.time} ${
                snapshot1.time.toInstant(
                    defaultTimeZone
                ) - snapshot2.time.toInstant(defaultTimeZone)
            }    |"
        )
        println("|-------------------------------------------------------------------------------------|")
        println(difference)
    }
}

val scope = CoroutineScope(Dispatchers.IO)


class EconomySnapshot(
    val time: LocalDateTime,
    val charactersDir: File,
    val grandExchangeOffers: File,
) {
    lateinit var players : List<Player>
    lateinit var sellOffers : Map<String, List<ExchangeOffer>>
    lateinit var buyOffers : Map<String, List<ExchangeOffer>>

    private val usernamesToOmit = setOf(
        "Effigyswiper",
        "Stan",
        "Kryeus",
        "Mr Peanut",    // banned
        "Jacmob",
        "Jimba",
        "Cayleb",
        "woez",
        "killerrun",
        "i_kerim_i",    // banned
        "inked",        // banned
        "god",
        "l1me"
    ).map(::sanitize)

    private fun load() {
        players = runBlocking {  allAccounts().filterNot { omitUser(it.username) } }
        val offers = loadGEOffersByPlayer()
        sellOffers = offers[ExchangeType.SELLING]?: emptyMap()
        buyOffers = offers[ExchangeType.BUYING]?: emptyMap()
    }

    private suspend fun allAccounts(): List<Player> {
        LoginManager.PLAYER_SAVE_DIRECTORY = charactersDir.toPath()
        return charactersDir
            .walkTopDown()
            .mapNotNull { file ->
                scope.async {
                    try {
                        LoginManager.deserializePlayerFromFile(file.nameWithoutExtension)
                    } catch (e: Exception) {
                        System.err.println("Failed to load player from file: ${file.nameWithoutExtension}")
                        e.printStackTrace()
                        null
                    }
                }
            }.toList().awaitAll().filterNotNull()
    }

    private fun loadGEOffersByPlayer(): Map<ExchangeType, Map<String, List<ExchangeOffer>>> {
        val br = BufferedReader(FileReader(grandExchangeOffers))
        val loadedOffers = DefaultGson.getGson().fromJson(
            br,
            Array<ExchangeOffer>::class.java
        )
        return loadedOffers
            .filterNot { omitUser(it.username) }
            .groupBy { it.type }
            .mapValues { it.value.groupBy { sanitize(it.username) } }
    }

    private fun omitUser(username: String) =
        usernamesToOmit.contains(sanitize(username))

    fun calculate(): Result {
        load()
        val containerWealthByPlayer = players.associateWith {
            it.bank.container.items.calculateValue() +
                    it.inventory.container.items.calculateValue() +
                    it.equipment.container.items.calculateValue() +
                    it.lootingBag.container.items.calculateValue() +
                    it.gravestone.container.items.calculateValue() +
                    it.runePouch.container.items.calculateValue() +
                    it.secondaryRunePouch.container.items.calculateValue()
        }
        val totalPointsPerPlayer = players.map {
            PlayerPoints(
                player = it,
                remnantPoints = it.exchangePoints.toLong(),
                votePoints = it.totalVoteCredits.toLong()
            )
        }
        val totalWealPerPLayer = containerWealthByPlayer.map {
            val player = it.key
            val buyOffersValue = buyOffers[sanitize(player.username)]?.sumOf { Item(995, it.remainder * it.price).value }?:0
            val sellOffersValue = sellOffers[sanitize(player.username)]?.sumOf { Item(it.item.id, it.remainder).value }?:0
            val allContainerItems = player.bank.container.items.values +
                    player.inventory.container.items.values +
                    player.equipment.container.items.values +
                    player.lootingBag.container.items.values +
                    player.gravestone.container.items.values +
                    player.runePouch.container.items.values +
                    player.secondaryRunePouch.container.items.values
            val allContainerItemsCount = allContainerItems.groupBy { it.definitions.unnotedOrDefault }.mapValues { it.value.sumOf { it.amount.toLong() } }
            val allExchangeItemCount = ((buyOffers[sanitize(player.username)]?.map { Item(995, it.remainder * it.price) }?: emptyList()) +
                    (sellOffers[sanitize(player.username)]?.map { Item(it.item.id, it.remainder) }?: emptyList())).groupBy { it.id }.mapValues { it.value.sumOf { it.amount.toLong() } }
            PlayerWealth(
                player = player,
                inContainers = it.value,
                inExchange = buyOffersValue + sellOffersValue,
                allContainerItemsCount = allContainerItemsCount,
                allExchangeItemCount = allExchangeItemCount
            )
        }
        val totalWealth = totalWealPerPLayer.sumOf { it.inContainers + it.inExchange }
        val totalWealth2 = containerWealthByPlayer.values.sum() + sellOffers.values.sumOf { it.sumOf { it.container.items.calculateValue() } }
        val totalItemCount = mutableMapOf<Int, Long>()
        for (playerWealth in totalWealPerPLayer) {
            for ((itemId, count) in playerWealth.allContainerItemsCount) {
                totalItemCount[itemId] = (totalItemCount[itemId]?:0) + count
            }
            for ((itemId, count) in playerWealth.allExchangeItemCount) {
                totalItemCount[itemId] = (totalItemCount[itemId]?:0) + count
            }
        }
        return Result(
            totalWealth = totalWealth,
            totalWealth2 = totalWealth2,
            playerPoints = totalPointsPerPlayer,
            playerWealth = totalWealPerPLayer,
            totalItemCount = totalItemCount
        )
    }

    private fun sanitize(username: String) =
        usernameSanitizer(username.lowercase().replace("_", " "))

    data class Result(
        val totalWealth : Long,
        val totalWealth2 : Long,
        val playerWealth : List<PlayerWealth>,
        val playerPoints: List<PlayerPoints>,
        val totalItemCount : Map<Int, Long>
    ){
        override fun toString(): String {

            return buildString {
                appendLine("|------------------------------------------------|")
                appendLine("| Total wealth: ${formatAmountText(totalWealth).padEnd(33)}|")
                appendLine("|------------------------------------------------|")
                appendLine("| Top Wealth Holders by containers:              |")
                for ((index, playerWealth) in playerWealth.sortedByDescending { it.inContainers }.take(20).withIndex()) {
                    appendLine("| #${"${index+1}".padEnd(3)}${playerWealth.player.username.padEnd(20)} ${formatAmountText(playerWealth.inContainers).padEnd(20)}  |")
                }
                appendLine("|------------------------------------------------|")
                appendLine("| Top Wealth Holders by exchange:                |")
                for ((index, playerWealth) in playerWealth.sortedByDescending { it.inExchange }.take(20).withIndex()) {
                    appendLine("| #${"${index+1}".padEnd(3)}${playerWealth.player.username.padEnd(20)} ${formatAmountText(playerWealth.inExchange).padEnd(20)}  |")
                }
                appendLine("|------------------------------------------------|")
                appendLine("| Most of items in economy with top 3 holders:   |")
                for (entry in totalItemCount.toList().sortedByDescending { it.second }.take(20).withIndex()) {
                    val (index, itemWithCount) = entry
                    val (itemId, count) = itemWithCount
                    appendLine("| #${"${index+1}".padEnd(3)}${ItemDefinitions.nameOf(itemId).padEnd(20)} ${formatAmountText(count).padEnd(20)}  |")
                    playerWealth.sortedByDescending { it.countOf(itemId) }.take(3).forEachIndexed { idx, playerWealth ->
                        appendLine("|  ${"${idx+1}".padEnd(2)} ${playerWealth.player.username.padEnd(20)} ${formatAmountText(playerWealth.countOf(itemId)).padEnd(20)}  |")
                    }
                }
                appendLine("|-------------------------------------------------------------|")
                appendLine("| Rare Item Statistics:                                       |")
                appendLine("|                                                             |")
                appendLine("| ${"name".padEnd(30)} ${"total".padEnd(15)} ${"avg price".padEnd(12)} |")
                appendLine("|                                                             |")
                commonRares.sortedByDescending { totalItemCount[it.id] }.forEach { rareItem ->
                    val amountOf = totalItemCount[rareItem.id]?:0
                    if (amountOf > 5) {
                        appendLine("| ${ItemDefinitions.nameOf(rareItem.id).padEnd(30)} ${formatAmountText(amountOf).padEnd(15)} ${formatAmountText(rareItem.value).padEnd(12)} |")
                        playerWealth.filter { it.countOf(rareItem.id) > 0 }.sortedByDescending { it.countOf(rareItem.id) }.take(5).forEachIndexed { idx, playerWealth ->
                            appendLine("|    ${"${idx+1}".padEnd(2)} ${playerWealth.player.username.padEnd(24)} ${formatAmountText(playerWealth.countOf(rareItem.id)).padEnd(28)} |")
                        }
                    }
                }
                appendLine("|-------------------------------------------------------------|")
                appendLine("| Remnant Point Statistics:                                   |")
                appendLine("| ${"name".padEnd(30)} ${"remnant points".padEnd(15)} ${"".padEnd(12)} |")
                playerPoints.sortedByDescending {
                    it.remnantPoints
                }.take(20).forEach {
                    appendLine("| ${it.player.username.padEnd(30)} ${formatAmountText(it.remnantPoints).padEnd(15)} ${"".padEnd(12)} |")
                }
                appendLine("|-------------------------------------------------------------|")
                appendLine("| Vote Point Statistics:                                      |")
                appendLine("| ${"name".padEnd(30)} ${"vote points".padEnd(15)} ${"".padEnd(12)} |")
                playerPoints.sortedByDescending {
                    it.votePoints
                }.take(20).forEach {
                    appendLine("| ${it.player.username.padEnd(30)} ${formatAmountText(it.votePoints).padEnd(15)} ${"".padEnd(12)} |")
                }
                appendLine("|-------------------------------------------------------------|")

            }
        }

        operator fun minus(other: Result) = Difference(
            wealth = totalWealth - other.totalWealth,
            itemCount = totalItemCount.mapValues { (itemId, count) -> count - (other.totalItemCount[itemId]?:0) },
            playerWealthDifference = playerWealth.associate { playerWealth ->
                val otherPlayerWealth = other.playerWealth.find { it.player.username == playerWealth.player.username }
                playerWealth.player.username to (playerWealth.inContainers + playerWealth.inExchange - (otherPlayerWealth?.inContainers?:0) - (otherPlayerWealth?.inExchange?:0))
            }
        )
    }

    data class Difference(
        val wealth: Long,
        val itemCount: Map<Int, Long>,
        val playerWealthDifference: Map<String, Long>
    ) {
        override fun toString(): String {
            return buildString {
                appendLine("|-------------------------------------------------------------------------------------|")
                appendLine("| Wealth difference: ${formatAmountText(wealth).padEnd(45)}                    |")
                appendLine("|-------------------------------------------------------------------------------------|")
                appendLine("| Items added to eco (sorted by impact on eco):                                       |")
                appendLine("|-------------------------------------------------------------------------------------|")
                val tradeableItems = itemCount.mapKeys { ItemDefinitions.get(it.key).unnotedOrDefault }.filter { showUp(
                    it.key
                ) }.entries
                for ((idx, itemWithCount) in tradeableItems.filter { it.value > 0 }.sortedByDescending { it.value * getItemValue(it.key) }.take(10).withIndex()) {
                    val (itemId, count) = itemWithCount
                    appendLine("| #${"${idx+1}".padEnd(2)} ${ItemDefinitions.nameOf(itemId).padEnd(40)} ${if (count >= 0) "+" else "-"} ${formatAmountText(count.absoluteValue).padEnd(14)} eco ${if (count >= 0) "+" else "-"} ${formatAmountText(count.absoluteValue * getItemValue(itemId)).padEnd(15)} |")
                }
                appendLine("|-------------------------------------------------------------------------------------|")
                appendLine("| Items removed from eco (sorted by impact on eco):                                   |")
                appendLine("|-------------------------------------------------------------------------------------|")
                for ((idx, itemWithCount) in tradeableItems.filter { it.value < 0 }.sortedBy { it.value * getItemValue(it.key) }.take(10).withIndex()) {
                    val (itemId, count) = itemWithCount
                    appendLine("| #${"${idx+1}".padEnd(2)} ${ItemDefinitions.nameOf(itemId).padEnd(40)} ${if (count >= 0) "+" else "-"} ${formatAmountText(count.absoluteValue).padEnd(14)} eco ${if (count >= 0) "+" else "-"} ${formatAmountText(count.absoluteValue * getItemValue(itemId)).padEnd(15)} |")
                }
                appendLine("|-------------------------------------------------------------------------------------|")
                appendLine("| Wealth difference by player:                                                        |")
                appendLine("|-------------------------------------------------------------------------------------|")
                for ((idx, pair) in playerWealthDifference.toList().sortedByDescending { it.second }.take(30).withIndex()) {
                    val (username, wealthDifference) = pair
                    appendLine("| #${"${idx+1}".padEnd(2)} ${username.padEnd(40)} ${if (wealthDifference >= 0) "+" else "-"} ${formatAmountText(wealthDifference.absoluteValue).padEnd(14)} eco ${if (wealthDifference >= 0) "+" else "-"} ${formatAmountText(wealthDifference.absoluteValue).padEnd(15)} |")
                }
            }
        }
    }

    data class PlayerPoints(
        val player: Player,
        val remnantPoints: Long,
        val votePoints: Long
    )
    data class PlayerWealth(
        val player: Player,
        val inContainers : Long,
        val inExchange: Long,
        val allContainerItemsCount: Map<Int, Long>,
        val allExchangeItemCount: Map<Int, Long>
    ) {
        val total = inContainers + inExchange
        fun countOf(itemId: Int) = (allContainerItemsCount[itemId]?:0) + (allExchangeItemCount[itemId]?:0)
    }

    companion object {

        fun ofBackupArchive(backupArchive: File): EconomySnapshot {
            val (date, time) = backupArchive.nameWithoutExtension.split("_").let { it[1] to it[2] }
            val year = date.substring(0, 4).toInt()
            val month = date.substring(4, 6).toInt()
            val day = date.substring(6, 8).toInt()
            val hour = time.substring(0, 2).toInt()
            val minute = time.substring(2, 4).toInt()
            val second = time.substring(4, 6).toInt()
            val dateTime = LocalDateTime(year, month, day, hour, minute, second)

            val tempDir = File.createTempFile("backup", "dir_${date}_$time")
            tempDir.delete()
            tempDir.mkdirs()
            ZipFile(backupArchive).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    val file = File(tempDir, entry.name)
                    if (entry.isDirectory) {
                        file.mkdirs()
                    } else {
                        file.parentFile.mkdirs()
                        file.outputStream().use { output ->
                            zip.getInputStream(entry).use { input ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }
            val characters = tempDir.resolve("home/world/server-production/data/characters")
            val grandExchangeOffers = tempDir.resolve("home/world/server-production/data/grandexchange/offers.json")
            return EconomySnapshot(dateTime, characters, grandExchangeOffers)
        }

        val commonRares = listOf(
            CustomItemId.REMNANT_POINT_VOUCHER_1,
            CustomItemId.KORASI,
            CustomItemId.ANCIENT_EYE,
            CustomItemId.ANCIENT_BOOK_32004,
            CustomItemId.ARMADYL_SOUL_CRYSTAL,
            CustomItemId.ARMADYL_BOW,
            CustomItemId.BANDOS_SOUL_CRYSTAL,
            CustomItemId.BANDOS_BOW,
            CustomItemId.SARADOMIN_SOUL_CRYSTAL,
            CustomItemId.SARADOMIN_BOW,
            CustomItemId.ZAMORAK_SOUL_CRYSTAL,
            CustomItemId.ZAMORAK_BOW,
            CustomItemId.DRAGON_KITE,
            CustomItemId.ANCIENT_MEDALLION_32024,
            CustomItemId.IMBUED_ANCIENT_CAPE,
            CustomItemId.IMBUED_ARMADYL_CAPE,
            CustomItemId.IMBUED_BANDOS_CAPE,
            CustomItemId.IMBUED_SEREN_CAPE,
            CustomItemId.POLYPORE_STAFF_DEG,
            CustomItemId.POLYPORE_STAFF,
            CustomItemId.BRONZE_KEY,
            CustomItemId.SILVER_KEY,
            CustomItemId.GOLD_KEY,
            CustomItemId.PLATINUM_KEY,
            CustomItemId.DIAMOND_KEY,
            CustomItemId.DEATH_CAPE,
            CustomItemId.LIME_WHIP,
            CustomItemId.LIME_WHIP_SPECIAL,
            CustomItemId.LAVA_WHIP,
            CustomItemId.PINK_PARTYHAT,
            CustomItemId.ORANGE_PARTYHAT,
            CustomItemId.DONATOR_PIN_10,
            CustomItemId.DONATOR_PIN_25,
            CustomItemId.DONATOR_PIN_50,
            CustomItemId.DONATOR_PIN_100,
            CustomItemId.GAUNTLET_SLAYER_HELM,
            CustomItemId.CORRUPTED_GAUNTLET_SLAYER_HELM,
            CustomItemId.NEAR_REALITY_PARTY_HAT,
            CustomItemId.OSNR_MYSTERY_BOX,
            CustomItemId.GANODERMIC_RUNT,
            CustomItemId.BLUE_ANKOU_SOCKS,
            CustomItemId.BLUE_ANKOU_GLOVES,
            CustomItemId.BLUE_ANKOUS_LEGGINGS,
            CustomItemId.BLUE_ANKOU_MASK,
            CustomItemId.BLUE_ANKOU_TOP,
            CustomItemId.GREEN_ANKOU_SOCKS,
            CustomItemId.GREEN_ANKOU_GLOVES,
            CustomItemId.GREEN_ANKOUS_LEGGINGS,
            CustomItemId.GREEN_ANKOU_MASK,
            CustomItemId.GREEN_ANKOU_TOP,
            CustomItemId.GOLD_ANKOU_SOCKS,
            CustomItemId.GOLD_ANKOU_GLOVES,
            CustomItemId.GOLD_ANKOUS_LEGGINGS,
            CustomItemId.GOLD_ANKOU_MASK,
            CustomItemId.GOLD_ANKOU_TOP,
            CustomItemId.WHITE_ANKOU_SOCKS,
            CustomItemId.WHITE_ANKOU_GLOVES,
            CustomItemId.WHITE_ANKOUS_LEGGINGS,
            CustomItemId.WHITE_ANKOU_MASK,
            CustomItemId.WHITE_ANKOU_TOP,
            CustomItemId.BANDOS_CHESTPLATE_OR,
            CustomItemId.BANDOS_TASSETS_OR,
            CustomItemId.BANDOS_ORNAMENT_KIT,
            CustomItemId.VESTAS_HELM,
            CustomItemId.HOLY_GREAT_WARHAMMER,
            CustomItemId.HOLY_GREAT_LANCE,
//            CustomItemId.DEGRADED_ESSENCE,
            CustomItemId.BLOOD_TENTACLES,
//            CustomItemId.ANGELIC_ARTIFACT,
            CustomItemId.FOUNDERS_CAPE,
            ItemId.TUMEKENS_SHADOW,
            ItemId.TUMEKENS_SHADOW_UNCHARGED,
            ItemId.TWISTED_BOW,
            ItemId.SCYTHE_OF_VITUR,
            ItemId.SCYTHE_OF_VITUR_22664,
            ItemId.HOLY_SCYTHE_OF_VITUR,
            ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED,
            ItemId.SANGUINE_SCYTHE_OF_VITUR,
            ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED,
            ItemId.SCYTHE_OF_VITUR_UNCHARGED,
            ItemId.NR_VOTE_SHARD,
            ItemId.ZARYTE_CROSSBOW,
            ItemId.ZARYTE_CROSSBOW_27186,
            ItemId.ZARYTE_BOW_UNCHARGED,
            ItemId.BLOOD_MONEY
        ).map {
            Item(it)
        }.filter {
            showUp(it.id)
        }
    }
}

private fun showUp(itemId: Int) = true
private val Item.value: Long
    get() = amount *  when(id) {
        ItemId.DONATOR_PIN_10 -> 55_000_000
        ItemId.DONATOR_PIN_25 -> 120_000_000
        ItemId.DONATOR_PIN_50 -> 250_000_000
        ItemId.DONATOR_PIN_100 -> 500_000_000
        ItemId.DEATH_CAPE -> 90_000_000
        ItemId.DRAGON_KITE -> 80_000_000
        ItemId.HOLY_GREAT_WARHAMMER -> 150_000_000
        ItemId.KORASI -> 27_500_000
        ItemId.ARMADYL_SOUL_CRYSTAL,
        ItemId.SARADOMIN_SOUL_CRYSTAL,
        ItemId.ZAMORAK_SOUL_CRYSTAL,
        ItemId.BANDOS_SOUL_CRYSTAL -> 10_000_000
        ItemId.ARMADYL_BOW,
        ItemId.SARADOMIN_BOW,
        ItemId.ZAMORAK_BOW,
        ItemId.BANDOS_BOW -> 50_000_000
        ItemId.WHITE_ANKOU_MASK -> 50_000_000
        ItemId.VESTAS_HELM -> 30_000_000
        ItemId.HOLY_GREAT_LANCE -> 175_000_000
        ItemId.LAVA_WHIP -> 35_000_000
        ItemId.ANCIENT_MEDALLION -> 110_000_000
        ItemId.BLUE_PARTYHAT,
        ItemId.GREEN_PARTYHAT,
        ItemId.RED_PARTYHAT,
        ItemId.WHITE_PARTYHAT,
        ItemId.YELLOW_PARTYHAT -> 100_000_000
        ItemId.PINK_PARTYHAT,
        ItemId.PURPLE_PARTYHAT,
        ItemId.ORANGE_PARTYHAT -> 150_000_000
        ItemId.BLACK_PARTYHAT -> 500_000_000
        ItemId.TUMEKENS_SHADOW,
        ItemId.TUMEKENS_SHADOW_UNCHARGED -> 1_500_000_000
        ItemId.TWISTED_BOW -> 900_000_000
        ItemId.SCYTHE_OF_VITUR,
        ItemId.SCYTHE_OF_VITUR_22664,
        ItemId.HOLY_SCYTHE_OF_VITUR,
        ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED,
        ItemId.SANGUINE_SCYTHE_OF_VITUR,
        ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED,
        ItemId.SCYTHE_OF_VITUR_UNCHARGED -> 1_000_000_000
        ItemId.NR_VOTE_SHARD -> 1_000_000
        ItemId.ZARYTE_CROSSBOW,
        ItemId.ZARYTE_CROSSBOW_27186,
        ItemId.ZARYTE_BOW_UNCHARGED -> 1_100_000_000
        ItemId.BLOOD_MONEY -> 10_000
        ItemId.DRAGON_BOLTS -> 10_000
        else -> getItemValue(id).toLong()
    }
