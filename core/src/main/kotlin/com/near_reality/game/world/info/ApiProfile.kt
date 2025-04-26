package com.near_reality.game.world.info

import kotlinx.serialization.Serializable

/**
 * Represents the API profile for a world.
 *
 * @param enabled whether the API is enabled.
 * @param scheme the scheme of the API.
 * @param host the host of the API.
 * @param port the port at which the API service listens.
 * @param token the bearer token for making requests to the API.
 *
 * @author Stan van der Bend
 */
@Serializable
data class ApiProfile(
    val enabled: Boolean,
    val scheme: String,
    val host: String,
    val port: Int,
    val token: String? = null
)
