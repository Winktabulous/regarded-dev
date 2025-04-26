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
 * Represents the data of a confirmed trade that is awaiting for staff to be handled.
 * At this point the trading players can no longer cancel the trade,
 * only the designated (or any) staff member can depending on the [staffOption].
 *
 * @author Stan van der Bend
 *
 * @param requesterUsername     the [Player.getUsername] of the player that initiated the trade.
 * @param accepterUsername      the [Player.getUsername] of the player that accepted the trade.
 * @param requesterDonatorPin   the [Item] (must be a Donator pin) that the requester offers.
 * @param accepterContainer     a [Container] of items that the accepter offers.
 * @param accepterOSRSMillions  the amount of OSRS GP (in millions) that the accepter offers.
 * @param staffOption           the [MiddleManStaffOption] selected during creation of trade request.
 */
data class MiddleManConfirmedTrade(
    override val requesterUsername: String,
    override val accepterUsername: String,
    override val requesterDonatorPin: Item,
    override val accepterContainer: Container,
    override val accepterOSRSMillions: Int,
    override val staffOption: MiddleManStaffOption,
) : MiddleManTrade() {

    fun toHandledTrade() = MiddleManHandledTrade(
        LocalDateTime.now(),
        requesterUsername,
        accepterUsername,
        requesterDonatorPin,
        accepterContainer,
        accepterOSRSMillions,
        staffOption
    )

    companion object : ScheduledExternalizable {

        override fun getLog(): Logger = MiddleManManager.logger

        override fun writeInterval(): Int = 0

        override fun ifFileNotFoundOnRead() = write()

        override fun read(reader: BufferedReader) {
            try {
                val trades: List<MiddleManConfirmedTrade> =
                    MiddleManConstants.gson.fromJson(
                        reader,
                        object : TypeToken<List<MiddleManConfirmedTrade>>() {}.type
                    )
                log.info("Loaded {} middle man trades awaiting to be handled", trades.size)
                MiddleManManager.toBeHandledTrades.clear()
                MiddleManManager.toBeHandledTrades.addAll(trades)
            } catch (e: Exception) {
                log.error("Failed to load middle man trades due to exception, saving backup", e)
                val originalFile = File(path())
                val backupFileName = "${originalFile.nameWithoutExtension}_${LocalDateTime.now()}.json"
                val backupFile = originalFile.parentFile.resolve(backupFileName)
                Files.copy(originalFile, backupFile)
            }
        }

        override fun write() {
            val serialised = MiddleManConstants.gson.toJson(MiddleManManager.toBeHandledTrades)
            out(serialised)
        }

        override fun path() = MiddleManConstants.CONFIRMED_MM_TRADE_PATH
    }
}
