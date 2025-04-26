package com.near_reality.api.requests

import com.near_reality.api.model.Sanction
import kotlinx.serialization.Serializable

@Serializable
data class SanctionSubmit(val sanction: Sanction)

@Serializable
data class SanctionRevoke(val sanction: Sanction)
