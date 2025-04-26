package com.near_reality.game.content.middleman.trade

import com.google.common.io.Files
import com.google.gson.reflect.TypeToken
import com.near_reality.game.content.middleman.MiddleManConstants
import com.near_reality.game.content.middleman.MiddleManManager
import com.near_reality.game.content.middleman.MiddleManStaffOption
import com.zenyte.cores.ScheduledExternalizable
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import org.slf4j.Logger
import java.io.BufferedReader
import java.io.File
import java.time.LocalDateTime

/**
 * Represents the data of a confirmed trade that has been handled by a staff.
 *
 * @author Stan van der Bend
 *
 * @param handledDate           the date at which this trade was handled.
 * @param requesterUsername     the [Player.getUsername] of the player that initiated the trade.
 * @param accepterUsername      the [Player.getUsername] of the player that accepted the trade.
 * @param requesterDonatorPin   the [Item] (must be a Donator pin) that the requester offers.
 * @param accepterContainer     a [Container] of items that the accepter offers.
 * @param accepterOSRSMillions  the amount of OSRS GP (in millions) that the accepter offers.
 * @param staffOption           the [MiddleManStaffOption] selected during creation of trade request.
 */
data class MiddleManHandledTrade(
    val handledDate: LocalDateTime,
    override val requesterUsername: String,
    override val accepterUsername: String,
    override val requesterDonatorPin: Item,
    override val accepterContainer: Container,
    override val accepterOSRSMillions: Int,
    override val staffOption: MiddleManStaffOption
) : MiddleManTrade() {

    companion object : ScheduledExternalizable {

        override fun getLog(): Logger = MiddleManManager.logger

        override fun writeInterval(): Int = 5

        override fun ifFileNotFoundOnRead() = write()

        override fun read(reader: BufferedReader) {
            try {
                val trades: List<MiddleManHandledTrade> =
                    MiddleManConstants.gson.fromJson(reader, object : TypeToken<List<MiddleManHandledTrade>>() {}.type)
                log.info("Loaded {} middle man trades that have been handled", trades.size)
                MiddleManManager.handledTrades.clear()
                MiddleManManager.handledTrades.addAll(trades)
            } catch (e: Exception) {
                log.error("Failed to load middle man trade history due to exception, saving backup", e)
                val originalFile = File(path())
                val backupFileName = "${originalFile.nameWithoutExtension}_${LocalDateTime.now()}.json"
                val backupFile = originalFile.parentFile.resolve(backupFileName)
                Files.copy(originalFile, backupFile)
            }
        }

        override fun write() {
            try {
                val serialised = MiddleManConstants.gson.toJson(MiddleManManager.handledTrades)
                out(serialised)
            } catch (e: Exception) {
                log.error("Failed to save middle man trade history due to exception", e)
            }
        }

        override fun path() = MiddleManConstants.HANDLED_MM_TRADE_PATH
    }
}
