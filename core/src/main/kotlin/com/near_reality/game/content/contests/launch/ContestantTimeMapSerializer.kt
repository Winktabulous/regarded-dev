package com.near_reality.game.content.contests.launch

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import kotlinx.datetime.Instant
import java.lang.reflect.Type

object ContestantTimeMapSerializer :
    JsonSerializer<MutableMap<SoloContestant, Instant>>,
    JsonDeserializer<MutableMap<SoloContestant, Instant>> {

    private val memberType = object: TypeToken<SoloContestant>(){}.type
    private val instantType = object: TypeToken<Instant>(){}.type

    override fun serialize(
        src: MutableMap<SoloContestant, Instant>,
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
    ): MutableMap<SoloContestant, Instant> {
        return json.asJsonArray.let {
            val map = mutableMapOf<SoloContestant, Instant>()
            for (element in it) {
                val obj = element.asJsonObject
                val member = context.deserialize<SoloContestant>(obj.get("member"), memberType)
                val time = context.deserialize<Instant>(obj.get("time"), instantType)
                map[member] = time
            }
            map
        }
    }
}