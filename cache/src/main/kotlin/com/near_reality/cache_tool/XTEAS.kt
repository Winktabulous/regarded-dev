package com.near_reality.cache_tool

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

/**
 * @author Jire
 */
object XTEAS {

    private const val DEFAULT_FILE_PATH = "data/cache-211-keys.json"

    fun load(file: File, mapper: ObjectMapper = jacksonObjectMapper()): List<XTEA> =
        mapper.readValue(file)

    fun load(
        filePath: String = DEFAULT_FILE_PATH,
        mapper: ObjectMapper = jacksonObjectMapper()
    ) = load(File(filePath), mapper)

}
