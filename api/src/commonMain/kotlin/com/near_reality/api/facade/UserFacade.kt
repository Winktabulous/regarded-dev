package com.near_reality.api.facade

import com.near_reality.api.model.ApiGameMode
import com.near_reality.api.model.Bond
import com.near_reality.api.responses.UserClaimBondResponse
import com.near_reality.api.responses.UserSubtractCreditsResponse
import com.near_reality.api.responses.UserUpdateGameModeResponse

interface UserFacade {

    suspend fun findUserIdByUsername(username: String): Long?

    suspend fun hasTwoFactorAuthEnabled(identityId: Long): Boolean
    suspend fun getTwoFactorAuthSecret(identityId: Long): String

    suspend fun setGameMode(identityId: Long, gameMode: ApiGameMode): UserUpdateGameModeResponse

    suspend fun claimBond(identityId: Long, bond: Bond): UserClaimBondResponse

    suspend fun subtractCredits(identityId: Long, amount: Int): UserSubtractCreditsResponse
}
