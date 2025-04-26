package com.near_reality.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.lang.reflect.Type

/**
 * @author Jire
 */
object Int2ObjectMapDeserializer : JsonDeserializer<Int2ObjectMap<*>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Int2ObjectMap<*> = context.deserialize(json, Int2ObjectOpenHashMap::class.java)

}