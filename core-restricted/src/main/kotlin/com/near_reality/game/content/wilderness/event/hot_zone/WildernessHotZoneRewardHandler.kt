package com.near_reality.game.content.wilderness.event.hot_zone

import com.near_reality.game.content.wilderness.event.hot_zone.WildernessHotZoneEvent.message
import com.near_reality.game.content.wilderness.event.hot_zone.WildernessHotZoneEvent.messagePlayersInHotZone
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.utils.StringUtilities.toRanking

/**
 * The handler for the wilderness hot zone rewards.
 *
 * Registers kills and experience gained by players in the hot zone and rewards the top 3 players when an even is over.
 *
 * @author Stan van der Bend
 */
internal class WildernessHotZoneRewardHandler {

    private val killsByUsername = mutableMapOf<String, Int>()
    private val experienceGainedByUsername = mutableMapOf<String, Double>()
    private val monsterDamageDealtByUsername = mutableMapOf<String, Int>()

    internal fun registerKill(player: Player) =
        killsByUsername.compute(player.username) { _, kills -> (kills?:0) + 1 }

    internal fun registerExperienceGained(player: Player, amount: Double) =
        experienceGainedByUsername.compute(player.username) { _, experience -> (experience?:0.0) + amount }

    internal fun registerMonsterDamageDealt(player: Player, amount: Int) =
        monsterDamageDealtByUsername.compute(player.username) { _, damage -> (damage?:0) + amount }

    internal fun reset() {
        killsByUsername.clear()
        experienceGainedByUsername.clear()
        monsterDamageDealtByUsername.clear()
    }

    internal fun giveRewards(area: WildernessHotZoneArea) {
        rewardPlayersWithMostKills(area)
        rewardPlayersWithMostExperienceGained()
        rewardPlayersWithMostMonsterDamageDealt()
    }

    private fun rewardPlayersWithMostKills(area: WildernessHotZoneArea) {
        val multi = area.wildernessLevelRange.first >= 30
        killsByUsername.rewardPlayers("player kills") { rank ->
            when (rank) {
                1 -> if (multi) Item(CustomItemId.PVP_MYSTERY_BOX, 1) else Item(ItemId.BLOOD_MONEY, 5_000)
                2 -> Item(ItemId.BLOOD_MONEY, if (multi) 2_500 else 500)
                3 -> Item(ItemId.BLOOD_MONEY, if (multi) 1_000 else 250)
                else -> null
            }
        }
    }

    private fun rewardPlayersWithMostExperienceGained() {
        experienceGainedByUsername.rewardPlayers("skilling experience gained") { rank ->
            when (rank) {
                1 -> Item(ItemId.BLOOD_MONEY, 5_000)
                2 -> Item(ItemId.BLOOD_MONEY, 2_500)
                3 -> Item(ItemId.BLOOD_MONEY, 1_000)
                else -> null
            }
        }
    }

    private fun rewardPlayersWithMostMonsterDamageDealt() {
        monsterDamageDealtByUsername.rewardPlayers("monster damage dealt") { rank ->
            when (rank) {
                1 -> Item(ItemId.BLOOD_MONEY, 5_000)
                2 -> Item(ItemId.BLOOD_MONEY, 2_500)
                3 -> Item(ItemId.BLOOD_MONEY, 1_000)
                else -> null
            }
        }
    }

    private fun <V : Number> Map<String, V>.rewardPlayers(
        metricName: String,
        rewardProvider: (Int) -> Item?
    ) {
        sortedPlayerNumberPairs().forEachIndexed { index, (player, metric) ->
            val rank = index + 1
            if (rank == 1)
                messagePlayersInHotZone("The hot zone event ended, ${player.name} came out on top with $metric $metricName!")
            val reward = rewardProvider(rank)
            message(player, buildString {
                append("You finished in ${toRanking(index + 1)} place with $metric $metricName")
                if (reward != null)
                    append(" and received ${reward.amount}x ${reward.name}")
                append("!")
            })
            if (reward != null)
                player.inventory.addOrDrop(reward)
        }
    }

    private fun<V : Number> Map<String, V>.sortedPlayerNumberPairs(): List<Pair<Player, V>> = entries
        .mapNotNull { entry ->
            entry.key
                .let { username -> World.getPlayer(username).orElse(null) }
                ?.let { player -> player to entry.value }
        }
        .sortedByDescending { it.second.toInt() }
}
