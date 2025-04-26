package com.zenyte.game.content.theatreofblood

import com.near_reality.scripts.npc.drops.table.DropTable
import com.near_reality.scripts.npc.drops.table.DropTableContext
import com.near_reality.scripts.npc.drops.table.DropTableType
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.zenyte.CacheManager
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.content.xamphur.XamphurBoost
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import io.mockk.every
import io.mockk.mockk
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.items.ItemDefinitions
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

fun main() {

    val cache = CacheManager.loadCache(Cache.openCache("cache/data/cache"))

    ItemDefinitions().load(cache)

    val players = mutableListOf<Player>()
    players += mockPlayer("stan", 20)
    players += mockPlayer("chris", 20)
    players += mockPlayer("jacmob", 20)

    val party = mockk<RaidingParty>()
    every { party.players } returns players

    for (mode in TheatreOfBloodMode.values()) {
        if (mode == TheatreOfBloodMode.ENTRY) continue
        println("Rolling rewards for raid in mode $mode")
        val playerRewards = TheatreOfBloodRewardGenerator.roll(party, mode)
        println("Total points for party = ${party.totalContributionPoints()}/${party.maxContributionPoints()}")
        for ((player, rewards) in playerRewards) {
            println("Rewards for ${player.name}: (points = ${player.theatreContributionPoints})")
            val container = Container(ContainerType.BANK, ContainerPolicy.ALWAYS_STACK, 1000, Optional.empty())
            for (reward in rewards) {
                container.add(reward)
            }

            for (entry in container.items) {
                val reward = entry.value
                println("\t ${reward.amount.toString().padEnd(10)} x ${reward.name.padEnd(40)} (${reward.id})")
            }
        }
        println("---------------------------------------------")
    }
}

private fun mockPlayer(name: String, theatreContributionPoints: Int): Player {
    val player = mockk<Player>()
    every { player.name } returns name
    every { player.getNumericTemporaryAttribute(any()) } returns theatreContributionPoints
    every { player.sendDeveloperMessage(any()) } returns Unit
    return player
}

/**
 * Handles the generation of rewards for Theatre of Blood raids.
 *
 * @author Stan van der Bend
 */
internal object TheatreOfBloodRewardGenerator {

    private val logger = LoggerFactory.getLogger(TheatreOfBloodRewardGenerator::class.java)

    private val alwaysItems
        get() = listOf(Item(CABBAGE, 1), Item(MESSAGE_22475, 1))

    /**
     * Rolls the rewards for all the players in the [party] for [theatreOfBloodMode].
     */
    fun roll(
        party: RaidingParty,
        theatreOfBloodMode: TheatreOfBloodMode,
        completedInTargetTime: Boolean = true,
    ): Map<Player, List<Item>> {

        val allPlayers = party.players
        if (allPlayers.isEmpty()) {
            logger.error("Could not roll rewards because there are no players in the party {}", party)
            return emptyMap()
        }

        val playerRewardsMap = mutableMapOf<Player, MutableList<Item>>()

        val nonContributingPlayers = allPlayers.filter { it.theatreContributionPoints <= 0 }
        for (player in nonContributingPlayers)
            playerRewardsMap.add(player, alwaysItems)

        val contributingPlayers = allPlayers.filter { it.theatreContributionPoints > 0 }
        if (contributingPlayers.isEmpty())
            logger.error("No contributing players found for party {}", party)
        else
            rollRewardsForContributingPlayers(party, theatreOfBloodMode, completedInTargetTime, contributingPlayers, playerRewardsMap, allPlayers)

        return playerRewardsMap
    }
    /**
     * Possibly roll a unique reward for a player in [contributingPlayers], the chance to roll a unique reward
     * is dependent upon the [mode] that the raid was completed in, and the chance for an individual player
     * to obtain the rewards depends on their performance relative to the total performance of the [party].
     */
    private fun rollRewardsForContributingPlayers(
        party: RaidingParty,
        mode: TheatreOfBloodMode,
        completedInTargetTime: Boolean,
        contributingPlayers: List<Player>,
        playerRewardsMap: MutableMap<Player, MutableList<Item>>,
        allPlayers: List<Player>
    ) {
        var booster: Player? = null
        for (player in allPlayers) {
            if (player.variables.tobBoosterleft > 0) {
                player.variables.tobBoosterleft--
                booster = player
                break
            }
        }

        // roll unique reward
        var (uniqueRollBaseChance, uniqueTable) = mode.toChanceUniqueTablePair()
        if (uniqueTable != null) {
            if (booster != null) {
                uniqueRollBaseChance = (uniqueRollBaseChance * 1.15).toInt()
            }
            if (World.hasBoost(XamphurBoost.TOB_PURPLE_BOOST)) {
                uniqueRollBaseChance = (uniqueRollBaseChance * 1.20).toInt()
            }

            val maxPoints = party.maxContributionPoints()
            val totalPoints = party.totalContributionPoints()
            val chanceReductionFactor = totalPoints.toDouble() / maxPoints.toDouble()
            val actualChance = when(mode) {
                TheatreOfBloodMode.BYPASS -> uniqueRollBaseChance
                else -> (uniqueRollBaseChance * chanceReductionFactor).roundToInt()
            }
            if (Random.nextInt(0..100) <= actualChance) {
                val uniqueItem = uniqueTable.roll().firstOrNull()?.rollItem()
                if (uniqueItem == null)
                    logger.error("Did not roll an unique item but should have, for party {}", party)
                else {
                    val dropRecipient: Player = rollDropRecipient(totalPoints, contributingPlayers)
                    playerRewardsMap.add(dropRecipient, uniqueItem)
                }
            }
        }

        val commonAmountMultiplier = when(mode) {
            TheatreOfBloodMode.ENTRY -> 0.15
            TheatreOfBloodMode.BYPASS -> 1.0
            TheatreOfBloodMode.NORMAL -> 1.4
            TheatreOfBloodMode.HARD -> 1.6
        }

        // roll common rewards
        for (player in contributingPlayers) {

            // skip if player already received a unique reward
            if (playerRewardsMap.any { it.key == player && it.value.isNotEmpty() })
                continue

            repeat(3) {
                val commonItems = commonRewardTable.roll(player).map { it.rollItem(commonAmountMultiplier) }
                playerRewardsMap.add(player, commonItems)
            }
        }

        // roll tertiary rewards
        for (player in contributingPlayers) {

            val tertiaryTable = if (mode == TheatreOfBloodMode.HARD)
                hardModeTertiaryRewards
            else
                normalModeTertiaryRewards

            var staticRollChanceRarityTransformer: (DropTableContext.(StaticRollChance) -> Int)? = null
            if (player == booster) {
                staticRollChanceRarityTransformer = { (it.rarity * 0.9).toInt() }
            }

            val tertiaryItems = tertiaryTable.roll(player, DropTableType.Main, staticRollChanceRarityTransformer).map(RollItemChance::rollItem)
            playerRewardsMap.add(player, tertiaryItems)

            playerRewardsMap.add(player, listOf(Item(995, Utils.random(100_000, 200_000))))
        }
    }

    /**
     * Chance out of 100 to roll a unique drop.
     */
    private fun TheatreOfBloodMode.toChanceUniqueTablePair(): Pair<Int, DropTable?> = when (this) {
        TheatreOfBloodMode.ENTRY -> 0 to null
        TheatreOfBloodMode.NORMAL -> 23 to uniqueRollTableNormal
        TheatreOfBloodMode.HARD -> 30 to uniqueRollTableHard
        TheatreOfBloodMode.BYPASS -> 3 to uniqueRollTableNormal
    }

    /**
     * The chance of being the recipient of the unique drop is based of
     * the percentage of [totalPoints] that was obtained by the player.
     */
    private fun rollDropRecipient(totalPoints: Int, players: List<Player>): Player {

        val playerChanceMap = players.associateWith {
            (100 * (it.theatreContributionPoints.toDouble() / totalPoints)).roundToInt()
        }

        playerChanceMap.forEach { (player, percentage) ->
            player.sendDeveloperMessage("You contributed ${percentage}% of the total contribution points ($totalPoints)")
        }

        val roll = Random.nextInt(0..100)
        var weight = 0
        var dropRecipient: Player? = null
        for ((player, chance) in playerChanceMap) {
            weight += chance
            if (weight >= roll) {
                dropRecipient = player
                break
            }
        }

        if (dropRecipient == null) {
            logger.error(
                "Did not find a drop recipient, " +
                        "but should have (roll = {}, weight = {}, chanceMap = {}), " +
                        "randomly assigning recipient", roll, weight, playerChanceMap
            )
            dropRecipient = players.random()
        }

        dropRecipient.sendDeveloperMessage("You are rolled as the drop recipient.")
        return dropRecipient
    }

    private fun MutableMap<Player, MutableList<Item>>.add(dropRecipient: Player, item: Item) =
        getOrPut(dropRecipient) { mutableListOf() }.add(item)

    private fun MutableMap<Player, MutableList<Item>>.add(dropRecipient: Player, items: Collection<Item>) =
        getOrPut(dropRecipient) { mutableListOf() }.addAll(items)
}

enum class TheatreOfBloodMode {
    ENTRY,
    NORMAL,
    HARD,
    BYPASS
}

private val uniqueRollTableNormal = StandaloneDropTableBuilder {
    limit = 15
    static {
        AVERNIC_DEFENDER_HILT quantity 1 rarity 4//80 -> 40 balance change so it's easier to get other items
        GHRAZI_RAPIER quantity 1 rarity 2
        SANGUINESTI_STAFF_UNCHARGED quantity 1 rarity 2
        JUSTICIAR_FACEGUARD quantity 1 rarity 2
        JUSTICIAR_CHESTGUARD quantity 1 rarity 2
        JUSTICIAR_LEGGUARDS quantity 1 rarity 2
        SCYTHE_OF_VITUR_UNCHARGED quantity 1 rarity 1
    }
}.staticTable

private val uniqueRollTableHard = StandaloneDropTableBuilder {
    limit = 170
    static {
        AVERNIC_DEFENDER_HILT quantity 1 rarity 40
        GHRAZI_RAPIER quantity 1 rarity 25
        SANGUINESTI_STAFF_UNCHARGED quantity 1 rarity 25
        JUSTICIAR_FACEGUARD quantity 1 rarity 22
        JUSTICIAR_CHESTGUARD quantity 1 rarity 22
        JUSTICIAR_LEGGUARDS quantity 1 rarity 22
        SCYTHE_OF_VITUR_UNCHARGED quantity 1 rarity 14
    }
}.staticTable

private val commonRewardTable = StandaloneDropTableBuilder {
    limit = 30
    static {
        VIAL_OF_BLOOD_22446 quantity (50..60) rarity 2
        DEATH_RUNE quantity 500..600 rarity 1
        BLOOD_RUNE quantity 500..600 rarity 1
        SWAMP_TAR quantity 500..600 rarity 1
        COAL quantity (500..600) rarity 1
        GOLD_ORE quantity (300..360) rarity 1
        MOLTEN_GLASS quantity (200..240) rarity 1
        ADAMANTITE_ORE quantity (130..156) rarity 1
        RUNITE_ORE quantity (60..72) rarity 1
        WINE_OF_ZAMORAK quantity (50..60) rarity 1
        POTATO_CACTUS quantity (50..60) rarity 1
        GRIMY_CADANTINE quantity (50..60) rarity 1
        GRIMY_AVANTOE quantity (40..48) rarity 1
        GRIMY_TOADFLAX quantity (37..44) rarity 1
        GRIMY_KWUARM quantity (36..43) rarity 1
        GRIMY_IRIT_LEAF quantity (34..40) rarity 1
        GRIMY_RANARR_WEED quantity (30..36) rarity 1
        GRIMY_SNAPDRAGON quantity (27..32) rarity 1
        GRIMY_LANTADYME quantity (26..31) rarity 1
        GRIMY_DWARF_WEED quantity (24..28) rarity 1
        GRIMY_TORSTOL quantity (20..24) rarity 1
        BATTLESTAFF quantity (15..18) rarity 1
        RUNE_BATTLEAXE quantity 4 rarity 1
        RUNE_PLATEBODY quantity 4 rarity 1
        RUNE_CHAINBODY quantity 4 rarity 1
        PALM_TREE_SEED quantity 3 rarity 1
        YEW_SEED quantity 3 rarity 1
        MAGIC_SEED quantity 3 rarity 1
        MAHOGANY_SEED quantity 10..12 rarity 1
    }
}.staticTable

private val normalModeTertiaryRewards = StandaloneDropTableBuilder {
    static {
        SCROLL_BOX_ELITE quantity 1 oneIn 6
        LIL_ZIK quantity 1 oneIn 550
    }
}.staticTable

private val hardModeTertiaryRewards = StandaloneDropTableBuilder {
    static {
        SCROLL_BOX_ELITE quantity 1 oneIn 5
        HOLY_ORNAMENT_KIT quantity 1 oneIn 80
        SANGUINE_ORNAMENT_KIT quantity 1 oneIn 125
        SANGUINE_DUST quantity 1 oneIn 225
        LIL_ZIK quantity 1 oneIn 400
    }
}.staticTable
