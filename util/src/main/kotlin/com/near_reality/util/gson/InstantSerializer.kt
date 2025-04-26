package com.near_reality.util.gson

import com.google.gson.*
import kotlinx.datetime.Instant
import java.lang.reflect.Type

object InstantSerializer : JsonSerializer<Instant>, JsonDeserializer<Instant?> {

    override fun serialize(src: Instant, srcType: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.toString())
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, type: Type?, context: JsonDeserializationContext?): Instant {
        return Instant.parse(json.asString)
    }
}
