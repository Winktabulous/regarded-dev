package com.near_reality.content.group_ironman.util

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.near_reality.content.group_ironman.IronmanGroupMember
import kotlinx.datetime.Instant
import java.lang.reflect.Type

object IronmanMemberTimeMapSerializer :
    JsonSerializer<MutableMap<IronmanGroupMember, Instant>>,
    JsonDeserializer<MutableMap<IronmanGroupMember, Instant>> {

    private val memberType = object: TypeToken<IronmanGroupMember>(){}.type
    private val instantType = object: TypeToken<Instant>(){}.type

    override fun serialize(
        src: MutableMap<IronmanGroupMember, Instant>,
        typeOfSrc: Type?,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonArray().apply {
            for ((member, instant) in src) {
                add(JsonObject().apply {
                    add("member", context.serialize(member))
                    add("time", context.serialize(instant))
                })
            }
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): MutableMap<IronmanGroupMember, Instant> {
       return json.asJsonArray.let {
           val map = mutableMapOf<IronmanGroupMember, Instant>()
           for (element in it) {
               val obj = element.asJsonObject
               val member = context.deserialize<IronmanGroupMember>(obj.get("member"), memberType)
               val time = context.deserialize<Instant>(obj.get("time"), instantType)
               map[member] = time
           }
           map
       }
    }
}
