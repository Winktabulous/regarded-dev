package com.near_reality.game.model.item.protection

import com.zenyte.cores.CoresManager
import org.slf4j.LoggerFactory
import java.io.File

object ItemProtectionValueManager {

    private val logger = LoggerFactory.getLogger(ItemProtectionValueManager::class.java)
    private val protectionValuesFile = File("data/items/protection_values.csv")
    private val protectionValues = mutableMapOf<Int, Int>()

    internal fun loadProtectionValues() {
        if (protectionValuesFile.exists()) {
            logger.info("Loading protection values from {}", protectionValuesFile)
            val reader = protectionValuesFile.bufferedReader()
            val lines = reader.readLines()
            for ((index, line) in lines.withIndex()) {
                val split = line.split(",")
                try {
                    val id = split.first().toInt()
                    val value = split.last().toInt()
                    protectionValues[id] = value
                } catch (e: Exception) {
                    logger.error("Failed to load protection value at line $index `'$line'`", e)
                }
            }
        }
    }

    private fun saveProtectionValues() {
        val all = protectionValues.toMap()
        CoresManager.slowExecutor.submit {
            try {
                val printWriter = protectionValuesFile.printWriter()
                for ((id, value) in all) {
                    printWriter.println("$id,$value")
                }
                printWriter.flush()
                printWriter.close()
                logger.info("Saved protection values to {}", protectionValuesFile)
            } catch (e: Exception) {
                logger.error("Failed to save protection values", e)
            }
        }
    }

    fun updateProtectionValue(id: Int, value: Int) {
        protectionValues[id] = value
        logger.info("Updated protection value for item $id to $value")
        saveProtectionValues()
    }

    fun getProtectionValue(id: Int): Int {
        return protectionValues[id] ?: 0
    }
}
