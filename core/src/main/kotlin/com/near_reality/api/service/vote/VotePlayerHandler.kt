package com.near_reality.api.service.vote

import com.near_reality.api.APIClient
import com.near_reality.api.model.User
import com.near_reality.api.model.Vote
import com.near_reality.api.model.VoteSite
import com.near_reality.api.model.VoteSiteStatus
import com.near_reality.api.service.store.notify
import com.near_reality.api.service.user.updateVoteStatisticOnPlayerDetailsTab
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.cores.CoresManager
import com.zenyte.game.content.skills.afk.AfkSkilling
import com.zenyte.game.content.vote.VoteHandler
import com.zenyte.game.content.xamphur.XamphurHandler
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.plugins.dialogue.WiseOldManD
import com.zenyte.utils.TimeUnit
import io.ktor.http.*
import io.ktor.server.util.*
import mgi.utilities.StringFormatUtil
import org.slf4j.LoggerFactory

object VotePlayerHandler {

    private val logger = LoggerFactory.getLogger(VotePlayerHandler::class.java)
    internal var peopleVoted = 0

    fun startVote(player: Player, site: VoteSite) {
        ifEnabled(
            player,
            action = { user ->
                player.notify("Awaiting vote confirmation...", loading = true)
                val voteUrl = when(site) {
                    VoteSite.RUNELOCUS -> url {
                        protocol = URLProtocol.HTTPS
                        host = "www.runelocus.com"
                        path("top-rsps-list/near-reality-is-back/vote")
                        parameters.append("callback", user.id.toString())
                    }
                    VoteSite.RSPS_LIST -> url {
                        protocol = URLProtocol.HTTPS
                        host = "www.rsps-list.com"
                        path("index.php")
                        parameters.append("a", "in")
                        parameters.append("u", "nrpk")
                        parameters.append("id", user.id.toString())
                    }
                }
                player.packetDispatcher.sendURL(voteUrl)
            }
        )
    }

    fun requestStatuses(player: Player, onResponse: (Map<VoteSite, VoteSiteStatus>) -> Unit) {
        ifEnabled(player) { user ->
            VoteAPIService.requestStatuses(user) {
                WorldTasksManager.schedule {
                    onResponse(it)
                }
            }
        }
    }

    internal fun onVoteReceived(vote: Vote) {
        fun tryClaimVote(player: Player, vote: Vote, offline: Boolean) {
            WorldTasksManager.schedule {
                val votePoints = vote.votePointReward
                val votePointsWithBonus = (votePoints * VoteHandler.getVotePointsModifier(player)).toInt()
                val bonusVotePoints = votePointsWithBonus - votePoints
                GameLogger.log {
                    GameLogMessage.ClaimedVotes(username = player.username, votesClaimed = votePoints, votesBonus = bonusVotePoints)
                }
                if (votePointsWithBonus > 0) {
                    XamphurHandler.get().addVotes(player, votePoints)
                    AfkSkilling.addAfkTime(player, TimeUnit.HOURS.toMillis(12))
                    WiseOldManD.rollClues(player, votePointsWithBonus)
                    val coinRewardAmount = votePointsWithBonus * (if (player.authenticator.isEnabled) 150_000 else 75_000)
                    val coinRewardItem = Item(ItemId.COINS_995, coinRewardAmount)
                    player.inventory.addOrDrop(ItemId.TOME_OF_EXPERIENCE_30215, 2)
                    player.inventory.addOrDrop(coinRewardItem)
                    player.totalVoteCredits += votePointsWithBonus
                    player.lastVoteClaimTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12)
                    if (!offline) {
                        player.updateVoteStatisticOnPlayerDetailsTab()
                        player.dialogue {
                            plain(buildString {
                                append("Your vote on ${vote.siteType.fancyName} has been confirmed!")
                                append("<br>As a reward you received ${Colour.DARK_BLUE.wrap("$votePoints vote points${if (votePointsWithBonus > 1) "s" else ""}")}")
                                if (bonusVotePoints > 0)
                                    append(" (+${Colour.DARK_BLUE.wrap("$bonusVotePoints")} bonus)")
                                append("<br>and ${Colour.DARK_BLUE.wrap(StringFormatUtil.format(coinRewardAmount))} gold pieces.")
                                append("<br>Thank you for voting <3")
                            })
                        }
                    }
                    peopleVoted++
                    if (peopleVoted % 10 == 0) {
                        WorldBroadcasts.broadcast(null, BroadcastType.HELPFUL_TIP, "Another 10 people have just voted, type ::vote and get rewards!")
                    }
                }
            }
        }
        val user = vote.user
        val onlinePlayer = World.getPlayer(user.name)
        if (onlinePlayer.isPresent) {
            tryClaimVote(onlinePlayer.get(), vote, offline = false)
        } else {
            CoresManager.getLoginManager().load(true, vote.user.name) { offlinePlayer ->
                if (offlinePlayer.isPresent)
                    tryClaimVote(offlinePlayer.get(), vote, offline = true)
                else
                    logger.error("Failed to claim vote ${vote.id} for user: ${vote.user.name}, user not found.")
            }
        }
    }

    private fun ifEnabled(player: Player, action: (User) -> Unit) {
        when {
            !APIClient.enabled -> player.notify("You cannot vote on this world.", schedule = false)
            !VoteAPIService.enabled -> player.notify( "The voting service is currently unavailable.", schedule = false)
            World.isUpdating() -> player.notify("You cannot vote while the world is updating.", schedule = false)
            else -> {
                val user = player.user
                if (user != null)
                    action(user)
                else
                    player.notify("You must be logged in to vote.", schedule = false)
            }
        }
    }
}
