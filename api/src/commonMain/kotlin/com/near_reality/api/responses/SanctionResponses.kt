package com.near_reality.api.responses

import com.near_reality.api.model.Sanction
import kotlinx.serialization.Serializable

@Serializable
sealed class SanctionSubmitResponse {
    @Serializable
    data object OffenderAccountNotFound : SanctionSubmitResponse()
    @Serializable
    data class Success(val sanctionId: Long) : SanctionSubmitResponse()
    @Serializable
    data class Error(val message: String) : SanctionSubmitResponse()
}

@Serializable
sealed class SanctionRevokeResponse {
    @Serializable
    data object SanctionNotFound : SanctionRevokeResponse()
    @Serializable
    data class Success(val sanction: Sanction) : SanctionRevokeResponse()
    @Serializable
    data class Error(val message: String) : SanctionRevokeResponse()
}
