@file:Suppress("unused")

package com.near_reality.game.content.gauntlet

import com.google.common.eventbus.Subscribe
import com.zenyte.cores.CoresManager
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.utils.TimeUnit
import org.slf4j.LoggerFactory

/**
 * @author Stan van der bend
 * @author Leanbow
 */
object GauntletModule {

    /**
     * Contains global statistics related to Gauntlet.
     */
    lateinit var statistics : GauntletStatistics

    internal val logger = LoggerFactory.getLogger(GauntletModule::class.java)

    @Subscribe
    @JvmStatic
    fun onServerLaunch(event: ServerLaunchEvent?) {
        GauntletCommands.register()
    }

    /**
     * Updates the active [statistics] after [gauntlet] is completed and writes it to a file.
     */
    @JvmStatic
    fun updateCompletionStatistics(duration: Long) {
        statistics.globalCompletionCount++
        val lengthInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration).toInt()
        logger.info("Gauntlet is killed in {}ms ({}s)",duration, lengthInSeconds)
        val oldRecord = statistics.globalBestCompletionTimeSeconds
        if (oldRecord <= 0 || lengthInSeconds < oldRecord) {
            statistics.globalBestCompletionTimeSeconds = lengthInSeconds
        }
        CoresManager.slowExecutor.execute(GauntletStatistics::write)
    }

    /**
     * Updates the active [statistics] after corrupted [gauntlet] is completed and writes it to a file.
     */
    @JvmStatic
    fun updateCorruptedCompletionStatistics(duration: Long) {
        statistics.globalCorruptedCompletionCount++
        val lengthInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration).toInt()
        logger.info("Gauntlet is killed in {}ms ({}s)",duration, lengthInSeconds)
        val oldRecord = statistics.globalCorruptedBestCompletionTimeSeconds
        if (oldRecord <= 0 || lengthInSeconds < oldRecord) {
            statistics.globalCorruptedBestCompletionTimeSeconds = lengthInSeconds
        }
        CoresManager.slowExecutor.execute(GauntletStatistics::write)
    }

    @JvmStatic
    fun updateDeathStatistics() {
        statistics.globalDeathCount++
        CoresManager.slowExecutor.execute(GauntletStatistics::write)
    }

    @JvmStatic
    fun updateCorruptedDeathStatistics() {
        statistics.globalDeathCount++
        CoresManager.slowExecutor.execute(GauntletStatistics::write)
    }

}
