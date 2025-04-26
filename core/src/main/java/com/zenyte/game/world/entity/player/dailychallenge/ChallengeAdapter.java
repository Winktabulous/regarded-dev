package com.zenyte.game.world.entity.player.dailychallenge;

import com.google.gson.*;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.DailyChallenge;

import java.lang.reflect.Type;

/**
 * @author Tommeh | 04/05/2019 | 00:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ChallengeAdapter implements JsonSerializer<DailyChallenge>, JsonDeserializer<DailyChallenge> {

    @Override
    public JsonElement serialize(DailyChallenge challenge, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(challenge.name());
    }

    @Override
    public DailyChallenge deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ChallengeWrapper.get(json.getAsString());
    }
}
