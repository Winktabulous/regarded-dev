package com.zenyte.game.content.boons;

import com.google.gson.*;
import com.zenyte.game.content.boons.impl.RelentlessPrecision;
import com.zenyte.game.content.boons.impl.DivineHealing;
import com.zenyte.game.content.boons.impl.FourSure;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.lang.reflect.Type;

public class BoonAdapter implements JsonSerializer<Boon>, JsonDeserializer<Boon> {
    private static final Object2ObjectOpenHashMap<String, Class<? extends Boon>> REMAPPED_PERKS = new Object2ObjectOpenHashMap<>();

    static {
        REMAPPED_PERKS.put("TwistedTradeOff", RelentlessPrecision.class);
        REMAPPED_PERKS.put("VitursOffering", FourSure.class);
        REMAPPED_PERKS.put("TumekensTribute", DivineHealing.class);
    }


    @Override
    public JsonElement serialize(Boon perk, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(BoonWrapper.get(perk.getClass()).name()));
        result.add("properties", context.serialize(perk, perk.getClass()));
        return result;
    }

    @Override
    public Boon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final String type = json.getAsJsonObject().get("type").getAsString();

        /* Used to migrate perk names cleanly across changes */
        if(REMAPPED_PERKS.containsKey(type))
            return context.deserialize(json, REMAPPED_PERKS.get(type));

        final BoonWrapper perk = BoonWrapper.getByString(type);
        if (perk != null) {
            return context.deserialize(json, perk.getPerk());
        }
        throw new IllegalArgumentException("Unknown perk: " + type);
    }
}