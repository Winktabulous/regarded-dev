package com.near_reality.game.model.ui.vote

import com.google.common.eventbus.Subscribe
import com.near_reality.api.dao.Db
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.VoteSites
import com.near_reality.api.dao.Votes
import com.near_reality.api.model.VoteSite
import com.near_reality.api.service.vote.VotePlayerHandler
import com.near_reality.api.util.currentTime
import com.near_reality.util.capitalize
import com.zenyte.game.GameConstants
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.utils.TimeUnit
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.Count
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.month
import org.jetbrains.exposed.sql.kotlin.datetime.year
import org.jetbrains.exposed.sql.stringLiteral
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

/**
 * Represents the voting [Interface], shows vote statistics for each site players can vote for us.
 * Additionally, players can start a vote from this interface.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class VoteInterface : Interface() {

    override fun attach() {
        put(12, "runelocus")
        put(13, "rsps list")
    }

    override fun build() {
        bind("runelocus") { player, _, _, _ ->
            VoteModel.startVote(player, VoteSite.RUNELOCUS)
        }
        bind("rsps list") { player, _, _, _ ->
            VoteModel.startVote(player, VoteSite.RSPS_LIST)
        }
    }

    override fun open(player: Player) {
        super.open(player)
        VoteModel.update(player)
    }

    override fun getInterface(): GameInterface =
        GameInterface.VOTE
}

/**
 * Represents the [VoteModel] which is responsible for updating the vote statistics and handling vote requests.
 */
object VoteModel {

    private val voteSiteStatistics = ConcurrentHashMap<VoteSite, VoteSiteStatistics>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val logger = LoggerFactory.getLogger(VoteModel::class.java)

    @Subscribe
    @JvmStatic
    fun onGameLaunched(serverLaunchEvent: ServerLaunchEvent) {
        logger.info("Starting vote site statistics fetcher, fetching every 10 seconds.")
        suspend fun updateVoteSiteStatisticsFromDatabase() {
            val (year, month) = currentTime.let { it.year to it.monthNumber }
            val count = Count(stringLiteral("*"))
            val statsBySite = Db.dbQueryMain {
                val totalCountBySite = Votes
                    .innerJoin(VoteSites)
                    .select(count, VoteSites.type)
                    .where { (Votes.voteTime.year() eq year) and (Votes.voteTime.month() eq month) }
                    .groupBy(VoteSites.type)
                    .associate { it[VoteSites.type] to it[count] }
                totalCountBySite.mapValues {
                    val topVotersBySite = Votes
                        .innerJoin(VoteSites)
                        .innerJoin(Users)
                        .select(count, VoteSites.type, Users.username)
                        .where { (Votes.voteTime.year() eq year) and (Votes.voteTime.month() eq month) and (VoteSites.type eq it.key) }
                        .groupBy(VoteSites.type, Users.username)
                        .orderBy(count, SortOrder.DESC)
                        .limit(5)
                        .associate { it[Users.username] to it[count].toInt() }
                    VoteSiteStatistics(
                        it.key,
                        it.value.toInt(),
                        topVotersBySite
                    )
                }
            }
            voteSiteStatistics.clear()
            voteSiteStatistics.putAll(statsBySite)
        }
        scope.launch {
            var first = true
            var previousVoteCount = VotePlayerHandler.peopleVoted
            while(true) {
                try {
                    if (GameConstants.WORLD_PROFILE.isMainDatabaseEnabled()) {
                        if (first || previousVoteCount != VotePlayerHandler.peopleVoted) {
                            logger.info("Fetching vote statistics from database")
                            previousVoteCount = VotePlayerHandler.peopleVoted
                            first = false
                            updateVoteSiteStatisticsFromDatabase()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    delay(10.seconds)
                }
            }
        }
    }

    fun startVote(player: Player, site: VoteSite) {
        VotePlayerHandler.startVote(player, site)
    }

    fun update(player: Player) {
        player.packetDispatcher.sendClientScript(32004, VoteSite.entries.size)
        VotePlayerHandler.requestStatuses(player) { voteSiteStatuses ->
            var componentId = 12
            // TODO: maybe sort based on which cooldown is lowest
            VoteSite.entries.forEach {
                val siteComponent = GameInterface.VOTE.id component componentId++
                val siteName = it.name.lowercase().capitalize().replace("_", " ")
                val siteTop = voteSiteStatistics[it]
                val siteTopVoters = siteTop
                    ?.topVoters
                    ?.entries
                    ?.joinToString(separator = "|") { (username, votes) -> "$username|$votes" }?:""
                val siteTotalVotesThisMonth = siteTop?.totalVotesThisMonth?:0
                val siteCooldown = voteSiteStatuses[it]
                    ?.secondsTillCanVote?.toLong()
                    ?.let(TimeUnit.SECONDS::toTicks)?.toInt()
                    ?: 0
                player.packetDispatcher.sendClientScript(32043, siteComponent, siteName, siteTopVoters, siteTotalVotesThisMonth, siteCooldown)
            }
        }
    }
}

private data class VoteSiteStatistics(
    val site: VoteSite,
    val totalVotesThisMonth: Int,
    val topVoters: Map<String, Int>
)
