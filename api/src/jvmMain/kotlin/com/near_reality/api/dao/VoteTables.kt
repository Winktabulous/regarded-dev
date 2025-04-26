package com.near_reality.api.dao

import com.near_reality.api.model.Vote
import com.near_reality.api.model.VoteSite
import com.near_reality.api.util.currentTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.duration

object Votes : IntIdTable("votes") {
    val user = reference("user", Users).index()
    val ip = ip("ip").index()
    val site = reference("site", VoteSites).index()
    val claimTime = datetime("claim_time").nullable().default(null)
    val voteTime = datetime("vote_time")
}

class VoteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VoteEntity>(Votes) {
        fun new(account: UserEntity, site: VoteSiteEntity, ip: String) = new {
            this.user = account
            this.ip = ip
            this.site = site
            this.voteTime = currentTime
        }
    }
    var user by UserEntity referencedOn Votes.user
    var ip by Votes.ip
    var site by VoteSiteEntity referencedOn Votes.site
    var claimTime by Votes.claimTime
    var voteTime by Votes.voteTime

    fun toApiModel() = Vote(
        id = id.value,
        siteType = site.type,
        user = user.toApiModel(includeCredentials = true),
        userIp = ip,
        voteTime = voteTime,
        claimed = claimTime != null,
        votePointReward = site.voteReward
    )
}

/**
 * Represents an [IntIdTable] which has entries for each site a player can vote on.
 */
object VoteSites : IntIdTable("vote_sites") {
    val type = enumeration<VoteSite>("type").index()
    val url = varchar("url", 100)
    val creditReward = integer("credit_reward").default(1)
    val minDurationBetweenVotes = duration("min_duration_between_votes")
}

/**
 * Represents a DAO for [VoteSites].
 */
class VoteSiteEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<VoteSiteEntity>(VoteSites) {
        fun findBy(voteSite: VoteSite) = find { VoteSites.type eq voteSite }.firstOrNull()
    }

    /**
     * The name of the vote site.
     */
    var type by VoteSites.type

    /**
     * The link to the vote site.
     */
    var url by VoteSites.url

    /**
     * The number of vote credits rewarded to the player for voting at this link.
     */
    var voteReward by VoteSites.creditReward

    /**
     * The minimal [duration][kotlin.time.Duration] between two consecutive votes by the same player.
     */
    var minDurationBetweenVotes by VoteSites.minDurationBetweenVotes
}

