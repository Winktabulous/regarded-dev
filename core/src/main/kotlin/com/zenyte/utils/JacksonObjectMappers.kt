package com.zenyte.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.kotlinModule

/**
 * @author Jire
 */
object JacksonObjectMappers {

    private val threadLocal: ThreadLocal<ObjectMapper> = ThreadLocal.withInitial {
        ObjectMapper()
            .registerModule(AfterburnerModule())
            .registerModule(kotlinModule())
    }

    operator fun invoke(): ObjectMapper = threadLocal.get()

}