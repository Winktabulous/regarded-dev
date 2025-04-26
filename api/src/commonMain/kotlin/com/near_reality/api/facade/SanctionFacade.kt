package com.near_reality.api.facade

import com.near_reality.api.model.Sanction
import com.near_reality.api.model.SanctionLevel
import com.near_reality.api.responses.SanctionRevokeResponse
import com.near_reality.api.responses.SanctionSubmitResponse

interface SanctionFacade {

    suspend fun submitSanction(sanction: Sanction) : SanctionSubmitResponse

    suspend fun revokeSanction(sanctionId: Long, level: SanctionLevel) : SanctionRevokeResponse
}
