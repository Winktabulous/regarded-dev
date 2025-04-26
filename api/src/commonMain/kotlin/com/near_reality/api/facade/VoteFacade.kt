package com.near_reality.api.facade

import com.near_reality.api.model.VoteSite
import com.near_reality.api.model.VoteSiteStatus
import com.near_reality.api.responses.CreateVoteResponse

interface VoteFacade {

    /**
     * Gets a list of [VoteSiteStatus] instances specific to the [GameAccount] associated with the [accountId].
     */
    suspend fun voteSiteStatuses(accountId: Long): List<VoteSiteStatus>

    /**
     * Creates a [Vote] of [voteSite] for the [GameAccount] associated with the [userId].
     */
    suspend fun createVote(voteSite: VoteSite, userId: Long, userIp: String) : CreateVoteResponse

    suspend fun claimVote(voteId: Int): Boolean
}
