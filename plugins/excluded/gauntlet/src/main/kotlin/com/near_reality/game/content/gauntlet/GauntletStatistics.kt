package com.near_reality.game.content.gauntlet

import com.google.gson.reflect.TypeToken
import com.zenyte.cores.ScheduledExternalizable
import org.slf4j.Logger
import java.io.BufferedReader

/**
 * @author Stan van der Bend
 * @author Leanbow
 */
data class GauntletStatistics(
    var globalCompletionCount: Long = 0,
    var globalDeathCount: Long = 0,
    var globalBestCompletionTimeSeconds: Int = -1,
    var globalCorruptedCompletionCount: Long = 0,
    var globalCorruptedDeathCount: Long = 0,
    var globalCorruptedBestCompletionTimeSeconds: Int = -1,
) {

    /**
     * Handles the serialization of Gauntlet Statistics, the active instance is stored in [GauntletModule.statistics].
     */
    companion object : ScheduledExternalizable {

        override fun getLog(): Logger = GauntletModule.logger

        override fun writeInterval(): Int = 0

        override fun ifFileNotFoundOnRead() {
            GauntletModule.statistics = GauntletStatistics()
        }

        override fun read(reader: BufferedReader) {
            try {
                GauntletModule.statistics = gson.fromJson(reader, object : TypeToken<GauntletStatistics>(){}.type)
                log.info("Loaded {}", GauntletModule.statistics)
            } catch (e: Exception) {
                log.error("Failed to load gauntlet statistics due to exception", e)
                GauntletModule.statistics = GauntletStatistics()
            }
        }

        override fun write() {
            val serialised = gson.toJson(GauntletModule.statistics)
            out(serialised)
        }

        override fun path(): String = "data/gauntlet_statistics.json"
    }
}
