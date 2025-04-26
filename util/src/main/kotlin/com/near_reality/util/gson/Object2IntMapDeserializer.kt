package com.near_reality.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import java.lang.reflect.Type

/**
 * @author Jire
 */
object Object2IntMapDeserializer : JsonDeserializer<Object2IntMap<*>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Object2IntMap<*> = context.deserialize(json, Object2IntOpenHashMap::class.java)

}