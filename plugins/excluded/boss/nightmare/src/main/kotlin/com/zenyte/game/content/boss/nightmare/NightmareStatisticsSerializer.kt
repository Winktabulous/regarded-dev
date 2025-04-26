package com.zenyte.game.content.boss.nightmare

import com.google.gson.reflect.TypeToken
import com.zenyte.cores.ScheduledExternalizable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader

object NightmareStatisticsSerializer : ScheduledExternalizable {

	internal val logger = LoggerFactory.getLogger(NightmareStatisticsSerializer::class.java)

	override fun getLog(): Logger = logger

	override fun writeInterval(): Int = 0

	override fun ifFileNotFoundOnRead() {
		NightmareGlobal.statistics = NightmareStatistics()
	}

	override fun read(reader: BufferedReader) {
		try {
			NightmareGlobal.statistics =
				gson.fromJson(reader, object : TypeToken<NightmareStatistics>() {}.type)
			log.info("Loaded {}", NightmareGlobal.statistics)
		} catch (e: Exception) {
			log.error("Failed to load gauntlet statistics due to exception", e)
			NightmareGlobal.statistics = NightmareStatistics()
		}
	}

	override fun write() {
		val serialised = gson.toJson(NightmareGlobal.statistics)
		out(serialised)
	}

	override fun path(): String = "data/nightmare_statistics.json"
}