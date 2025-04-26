package com.near_reality.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Handles the serialisation and de-serialisation of local date times.
 *
 * @author Stan van der Bend
 */
object LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {

    override fun write(out: JsonWriter, value: LocalDateTime) {
        out.value(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value))
    }

    override fun read(input: JsonReader): LocalDateTime =
        LocalDateTime.parse(input.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
