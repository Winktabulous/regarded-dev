package com.near_reality.network.login

/**
 * @author Jire
 */
data class ClientSettings(
    val lowMemory: Boolean,
    val isResizable: Boolean,
    val width: Int, val height: Int
)