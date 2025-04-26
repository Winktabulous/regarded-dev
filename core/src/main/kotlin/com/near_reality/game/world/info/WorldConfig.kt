package com.near_reality.game.world.info

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.Serializable
import java.io.File

/**
 * Represents the configuration for the worlds.
 *
 * @param worlds the map of world id's to world profiles.
 *
 * @see WorldProfile for more information.
 *
 * @author Stan van der Bend
 */
@Serializable
data class WorldConfig(
    val worlds: Map<String, WorldProfile>
) {

    operator fun get(key: String) : WorldProfile? = worlds[key]

    companion object {

        fun fromYAML(file: File) : WorldConfig =
            file.inputStream().use(Yaml.default::decodeFromStream)
    }
}
