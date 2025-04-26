package com.near_reality.game.world.entity.player

/**
 * Provides a [username] that can be used to identify a player.
 */
interface UsernameProvider {

    /**
     * The username of a player.
     */
    val username: String
}
