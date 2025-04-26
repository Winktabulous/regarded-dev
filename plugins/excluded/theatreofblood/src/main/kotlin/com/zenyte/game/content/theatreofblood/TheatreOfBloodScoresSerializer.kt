package com.zenyte.game.content.theatreofblood

import com.google.gson.reflect.TypeToken
import com.zenyte.cores.ScheduledExternalizable
import org.slf4j.Logger
import java.io.BufferedReader

object TheatreOfBloodScoresSerializer : ScheduledExternalizable {

	override fun getLog(): Logger = VerSinhazaArea.logger

	override fun writeInterval(): Int = 0

	override fun ifFileNotFoundOnRead() {
		VerSinhazaArea.statistics = Array(2){ TheatreOfBloodScores() }
	}

	override fun read(reader: BufferedReader) {
		try {
			VerSinhazaArea.statistics = gson.fromJson(reader, object : TypeToken<Array<TheatreOfBloodScores>>(){}.type)
			log.info("Loaded {}", VerSinhazaArea.statistics)
		} catch (e: Exception) {
			log.error("Failed to load tob statistics due to exception", e)
			ifFileNotFoundOnRead();
		}
	}

	override fun write() {
		val serialised = gson.toJson(VerSinhazaArea.statistics)
		out(serialised)
	}

	override fun path(): String = "data/tob_statistics.json"

}