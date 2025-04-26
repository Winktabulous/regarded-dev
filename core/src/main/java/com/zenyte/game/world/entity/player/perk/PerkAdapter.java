package com.zenyte.game.world.entity.player.perk;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author Tommeh | 11-1-2019 | 20:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PerkAdapter implements JsonSerializer<Perk>, JsonDeserializer<Perk> {
    @Override
    public JsonElement serialize(Perk perk, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(PerkWrapper.get(perk.getClass()).name()));
        result.add("properties", context.serialize(perk, perk.getClass()));
        return result;
    }

    @Override
    public Perk deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final String type = json.getAsJsonObject().get("type").getAsString();
        final PerkWrapper perk = PerkWrapper.get(type);
        if (perk != null) {
            return context.deserialize(json, perk.getPerk());
        }
        throw new IllegalArgumentException("Unknown perk: " + type);
    }
}
