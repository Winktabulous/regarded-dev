package com.near_reality.game.world.info

import kotlinx.serialization.Serializable

@Serializable
data class DatabaseProfile(
    val enabled: Boolean,
    val databaseUrl: String,
    val databasePort: Int,
    val databaseName: String,
    val databaseUser: String,
    val databasePassword: String,
)
