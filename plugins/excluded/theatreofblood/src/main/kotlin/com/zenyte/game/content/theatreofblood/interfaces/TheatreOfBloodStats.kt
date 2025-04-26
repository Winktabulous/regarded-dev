package com.zenyte.game.content.theatreofblood.interfaces

import com.zenyte.game.content.theatreofblood.TheatreOfBloodScores
import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.tobStats
import com.zenyte.game.content.theatreofblood.tobStatsHard
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player

class TheatreOfBloodStats : Interface() {

	override fun attach() {
		put(4, "Entry")
		put(5, "Normal")
		put(6, "Hard")
	}

    override fun open(player: Player) {
        super.open(player)

		updateInterface(player)
    }

	override fun build() {
		bind("Normal") { player: Player ->
			player.varManager.sendBit(12270, 0)
			updateInterface(player)
		}
		bind("Entry") { player: Player ->
			player.sendMessage("Entry mode is unavailable.")
			player.varManager.sendBit(12270, player.varManager.getBitValue(12270))
        }
        bind("Hard") { player: Player ->
            player.varManager.sendBit(12270, 2)
			updateInterface(player)
        }
    }

	private fun updateInterface(player: Player) {
		for (i in 41..66) {
			player.packetDispatcher.sendComponentText(`interface`, i, "$i")
		}

		var stats = if (player.varManager.getBitValue(12270) == 0) player.tobStats else player.tobStatsHard
		player.packetDispatcher.sendComponentText(`interface`, 41, stats.attempts)
		player.packetDispatcher.sendComponentText(`interface`, 43, stats.completions)
		player.packetDispatcher.sendComponentText(`interface`, 45, stats.deaths)

		player.packetDispatcher.sendComponentText(`interface`, 47, formatTimeOverall(stats, 0))
		player.packetDispatcher.sendComponentText(`interface`, 51, formatTimeOverall(stats, 1))
		player.packetDispatcher.sendComponentText(`interface`, 55, formatTimeOverall(stats, 2))
		player.packetDispatcher.sendComponentText(`interface`, 59, formatTimeOverall(stats, 3))
		player.packetDispatcher.sendComponentText(`interface`, 63, formatTimeOverall(stats, 4))

		player.packetDispatcher.sendComponentText(`interface`, 48, formatTimeChallenge(stats, 0))
		player.packetDispatcher.sendComponentText(`interface`, 52, formatTimeChallenge(stats, 1))
		player.packetDispatcher.sendComponentText(`interface`, 56, formatTimeChallenge(stats, 2))
		player.packetDispatcher.sendComponentText(`interface`, 60, formatTimeChallenge(stats, 3))
		player.packetDispatcher.sendComponentText(`interface`, 64, formatTimeChallenge(stats, 4))

		stats = if (player.varManager.getBitValue(12270) == 0) VerSinhazaArea.statistics[0] else VerSinhazaArea.statistics[1]
		player.packetDispatcher.sendComponentText(`interface`, 42, stats.attempts)
		player.packetDispatcher.sendComponentText(`interface`, 44, stats.completions)
		player.packetDispatcher.sendComponentText(`interface`, 46, stats.deaths)

		player.packetDispatcher.sendComponentText(`interface`, 49, formatTimeOverall(stats, 0))
		player.packetDispatcher.sendComponentText(`interface`, 53, formatTimeOverall(stats, 1))
		player.packetDispatcher.sendComponentText(`interface`, 57, formatTimeOverall(stats, 2))
		player.packetDispatcher.sendComponentText(`interface`, 61, formatTimeOverall(stats, 3))
		player.packetDispatcher.sendComponentText(`interface`, 65, formatTimeOverall(stats, 4))

		player.packetDispatcher.sendComponentText(`interface`, 50, formatTimeChallenge(stats, 0))
		player.packetDispatcher.sendComponentText(`interface`, 54, formatTimeChallenge(stats, 1))
		player.packetDispatcher.sendComponentText(`interface`, 58, formatTimeChallenge(stats, 2))
		player.packetDispatcher.sendComponentText(`interface`, 62, formatTimeChallenge(stats, 3))
		player.packetDispatcher.sendComponentText(`interface`, 66, formatTimeChallenge(stats, 4))
	}

	private fun formatTimeOverall(stats: TheatreOfBloodScores, index: Int): String {
		val ticks = stats.bestTimesOverall[index]
		if (ticks == 0L || ticks == Long.MAX_VALUE) return "-"
		return Utils.ticksToTime(ticks)
	}

	private fun formatTimeChallenge(stats: TheatreOfBloodScores, index: Int): String {
		val ticks = stats.bestTimesChallenge[index]
		if (ticks == 0L || ticks == Long.MAX_VALUE) return "-"
		return Utils.ticksToTime(ticks)
	}

	override fun getInterface() = GameInterface.TOB_STATS

}
