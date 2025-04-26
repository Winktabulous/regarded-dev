package com.near_reality.game.world.info

import com.near_reality.api.model.WorldLocation
import com.near_reality.api.model.WorldType
import com.near_reality.api.model.WorldVisibility
import kotlinx.serialization.Serializable

/**
 * Represents the config profile for a world.
 *
 * @param number the number of the game world.
 * @param host the host of the game world.
 * @param port the port at which the game world listens.
 * @param activity the activity of the world.
 * @param private whether the world is private (can others with right visibility rights access it?).
 * @param visibility the visibility of the world.
 * @param verifyPasswords whether passwords should be verified.
 * @param location the location of the world.
 * @param discordToken the discord token for the world.
 * @param logback the logback configuration for the world.
 * @param types the types of the world.
 * @param api the API profile for the world.
 *
 * @author Stan van der Bend
 */
@Serializable
data class WorldProfile(
    val number: Int = 1,
    val host: String = "localhost",
    val port: Int = 43594,
    val activity: String = "Near Reality",
    val private: Boolean = false,
    val visibility: WorldVisibility = WorldVisibility.DEVELOPER,
    val verifyPasswords: Boolean = false,
    val location: WorldLocation = WorldLocation.NETHERLANDS,
    val discordToken: String? = null,
    val logback: String = "logback-dev.xml",
    val types: List<WorldType> = emptyList(),
    val useWhitelist: Boolean = false,
    val whitelistedUsernames: Set<String> = emptySet(),
    val api: ApiProfile? = null,
    val logsDatabase: DatabaseProfile? = null,
    val mainDatabase: DatabaseProfile? = null
) {
    fun isDiscordEnabled() = !discordToken.isNullOrBlank() && visibility != WorldVisibility.DEVELOPER
    fun isDevelopment() = visibility == WorldVisibility.DEVELOPER
    fun isBeta() = visibility == WorldVisibility.BETA
    fun isPublic() = visibility == WorldVisibility.PUBLIC
    fun isApiEnabled() = api?.enabled == true
    fun isLogsDatabaseEnabled() = logsDatabase?.enabled == true
    fun isMainDatabaseEnabled() = mainDatabase?.enabled == true
    fun verify2FA() = !isBeta() || !verifyPasswords
}

