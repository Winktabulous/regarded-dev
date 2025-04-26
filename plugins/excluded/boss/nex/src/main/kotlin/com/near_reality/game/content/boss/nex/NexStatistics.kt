package com.near_reality.game.content.boss.nex

import com.google.gson.reflect.TypeToken
import com.zenyte.cores.ScheduledExternalizable
import org.slf4j.Logger
import java.io.BufferedReader

/**
 * Contains global statistics related to Nex, which are showcased on the Ancient Scoreboard.
 *
 * @author Stan van der Bend
 *
 * @param globalKillCount           the total number of Nex kills.
 * @param globalDeathCount          the total number of Player deaths killed in the Ancient Prison.
 * @param globalBestKillTimeSeconds the minimum amount of seconds it has taken to kill an instance of Nex.
 */
data class NexStatistics(
    var globalKillCount: Long = 0,
    var globalDeathCount: Long = 0,
    var globalBestKillTimeSeconds: Int = 0,
) {

    /**
     * Handles the serialization of Nex Statistics, the active instance is stored in [NexModule.statistics].
     */
    companion object : ScheduledExternalizable {

        override fun getLog(): Logger = NexModule.logger

        override fun writeInterval(): Int = 0

        override fun ifFileNotFoundOnRead() {
            NexModule.statistics = NexStatistics()
        }

        override fun read(reader: BufferedReader) {
            try {
                NexModule.statistics = gson.fromJson(reader, object : TypeToken<NexStatistics>(){}.type)
                log.info("Loaded {}", NexModule.statistics)
            } catch (e: Exception) {
                log.error("Failed to load nex statistics due to exception", e)
                NexModule.statistics = NexStatistics()
            }
        }

        override fun write() {
            val serialised = gson.toJson(NexModule.statistics)
            out(serialised)
        }

        override fun path(): String = "data/nex_statistics.json"
    }
}
