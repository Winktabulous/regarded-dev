package com.near_reality.osrsbox_db

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Stopwatch
import com.zenyte.utils.JacksonObjectMappers
import org.slf4j.Logger
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.concurrent.TimeUnit

/**
 * @author Jire
 */
interface DefinitionDatabase<T> {

    var definitions: Map<Int, T>

    val definitionClass: Class<T>

    val logger: Logger

    fun fileName(): String

    fun loadFromFile(objectMapper: ObjectMapper) {
        val stopwatch = Stopwatch.createStarted()

        val mapType = objectMapper.typeFactory.constructMapType(Map::class.java, Int::class.java, definitionClass)

        Files.newInputStream(Path.of(fileName()), StandardOpenOption.READ).use {
            definitions = objectMapper.readValue(it, mapType)
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        logger.info(
            "Loaded {} {} in {} ms.",
            definitions.size, definitionClass.simpleName, elapsed
        )
    }

    fun loadFromFile() = loadFromFile(JacksonObjectMappers())

    fun buildConfigs()

    operator fun get(id: Int): T?

}