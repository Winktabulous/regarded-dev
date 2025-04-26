package com.near_reality.util.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectCollection
import java.lang.reflect.Type

/**
 * @author Jire
 */
object ObjectCollectionDeserializer : JsonDeserializer<ObjectCollection<*>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ObjectCollection<*> = context.deserialize(json, ObjectArrayList::class.java)

}