package com.zenyte.game.content.boss.nightmare

import com.google.gson.reflect.TypeToken
import com.zenyte.cores.ScheduledExternalizable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader

object PhosanisNightmareStatisticsSerializer : ScheduledExternalizable {

	internal val logger = LoggerFactory.getLogger(PhosanisNightmareStatisticsSerializer::class.java)

	override fun getLog(): Logger = logger

	override fun writeInterval(): Int = 0

	override fun ifFileNotFoundOnRead() {
		NightmareGlobal.phosanisStatistics = NightmareStatistics()
	}

	override fun read(reader: BufferedReader) {
		try {
			NightmareGlobal.phosanisStatistics =
				gson.fromJson(reader, object : TypeToken<NightmareStatistics>() {}.type)
			log.info("Loaded {}", NightmareGlobal.phosanisStatistics)
		} catch (e: Exception) {
			log.error("Failed to load gauntlet statistics due to exception", e)
			NightmareGlobal.phosanisStatistics = NightmareStatistics()
		}
	}

	override fun write() {
		val serialised = gson.toJson(NightmareGlobal.phosanisStatistics)
		out(serialised)
	}

	override fun path(): String = "data/phosanis_nightmare_statistics.json"
}