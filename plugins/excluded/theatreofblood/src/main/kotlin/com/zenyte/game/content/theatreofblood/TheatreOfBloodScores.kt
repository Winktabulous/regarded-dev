package com.zenyte.game.content.theatreofblood

import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player

data class TheatreOfBloodScores(
	var attempts: Int = 0,
	var completions: Int = 0,
	var deaths: Int = 0,
	val bestTimesChallenge: LongArray = LongArray(5) { Long.MAX_VALUE },
	val bestTimesOverall: LongArray = LongArray(5) { Long.MAX_VALUE }
) {

	fun complete(player: Player?, partySize: Int, challengeTicks: Long, overallTicks: Long) {
		completions++

		val bestTimeIndex = partySize - 1
		val bestTimeChallenge = bestTimesChallenge[bestTimeIndex]
		if (challengeTicks < bestTimeChallenge) {
			bestTimesChallenge[bestTimeIndex] = challengeTicks
			player?.sendMessage("Theatre of Blood wave completion time: ${Colour.RED.wrap(Utils.ticksToTime(challengeTicks))} (Personal best!)")
		} else {
			player?.sendMessage("Theatre of Blood wave completion time: ${Colour.RED.wrap(Utils.ticksToTime(challengeTicks))}")
			player?.sendMessage("Personal best: ${Utils.ticksToTime(bestTimeChallenge)}")
		}

		val bestTimeOverall = bestTimesOverall[bestTimeIndex]
		if (overallTicks < bestTimeOverall) {
			bestTimesOverall[bestTimeIndex] = overallTicks
			player?.sendMessage("Theatre of Blood total completion time: ${Colour.RED.wrap(Utils.ticksToTime(overallTicks))} (Personal best!)")
		} else {
			player?.sendMessage("Theatre of Blood total completion time: ${Colour.RED.wrap(Utils.ticksToTime(overallTicks))}")
			player?.sendMessage("Personal best: ${Utils.ticksToTime(bestTimeOverall)}")
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TheatreOfBloodScores

		if (attempts != other.attempts) return false
		if (completions != other.completions) return false
		if (deaths != other.deaths) return false
		if (!bestTimesChallenge.contentEquals(other.bestTimesChallenge)) return false
		if (!bestTimesOverall.contentEquals(other.bestTimesOverall)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = attempts
		result = 31 * result + completions
		result = 31 * result + deaths
		result = 31 * result + bestTimesChallenge.contentHashCode()
		result = 31 * result + bestTimesOverall.contentHashCode()
		return result
	}

}