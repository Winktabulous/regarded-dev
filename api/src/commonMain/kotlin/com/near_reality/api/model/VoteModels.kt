package com.near_reality.api.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * Represents the status of a specific vote site for an [GameAccount].
 *
 * @author Stan van der Bend
 *
 * @param type                  the [VoteSite] of which this is the status.
 * @param url                   the url at which players can vote.
 * @param voteReward          the number of voting credits rewarded for voting at this site.
 * @param secondsTillCanVote    the number of seconds until the player can vote at this site again.
 */
@Serializable
data class VoteSiteStatus(
    val type: VoteSite,
    val url: String,
    val voteReward: Int,
    val secondsTillCanVote: Int
)

@Serializable
enum class VoteSite {
    RUNELOCUS,
    RSPS_LIST;

    val fancyName get() = name.lowercase().replaceFirstChar { it.uppercase() }.replace("_", " ")
}

/**
 * Represents a vote by a player that may be claimed or unclaimed.
 *
 * @author Stan van der Bend
 */
@Serializable
data class Vote(
    val id: Int,
    val user: User,
    val userIp: String,
    val siteType: VoteSite,
    val voteTime: LocalDateTime,
    val claimed: Boolean,
    val votePointReward: Int
)
