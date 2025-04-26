package com.near_reality.api.responses

import com.near_reality.api.model.User
import kotlinx.serialization.Serializable

@Serializable
sealed class UserLoginResponse {

    @Serializable
    data class Success(val user: User) : UserLoginResponse()

    @Serializable
    data object UserNotExist : UserLoginResponse()

    @Serializable
    data object InvalidPassword : UserLoginResponse()
}

@Serializable
sealed class UserUpdateGameModeResponse {

    @Serializable
    data class Success(val user: User) : UserUpdateGameModeResponse()

    @Serializable
    data object UserNotExist : UserUpdateGameModeResponse()
}

@Serializable
sealed class UserUpdateHiScoresResponse {

    @Serializable
    data class Success(val user: User) : UserUpdateHiScoresResponse()

    @Serializable
    data object UserNotExist : UserUpdateHiScoresResponse()
}

@Serializable
sealed class UserClaimBondResponse {

    @Serializable
    data class Success(val user: User) : UserClaimBondResponse()

    @Serializable
    data object UserNotExist : UserClaimBondResponse()
}

@Serializable
sealed class UserSubtractCreditsResponse {

    @Serializable
    data class Success(val user: User) : UserSubtractCreditsResponse()

    @Serializable
    data object UserNotExist : UserSubtractCreditsResponse()

    @Serializable
    data class InsufficientFunds(val difference: Int) : UserSubtractCreditsResponse()
}
