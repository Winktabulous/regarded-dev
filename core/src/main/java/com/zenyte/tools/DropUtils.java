package com.zenyte.tools;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.world.entity.player.Player;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DropUtils {

    private static int randomRoll(final Random random, final int i) {
        return random.nextInt(i + 1);
    }

    public static int randomRoll(Player p, int i) {
        double rate = (p.getDropRateBonus() / 100D) + 1.0D;
        if (PlayerAttributesKt.getFlaggedAsBot(p))
            rate += 0.01D;
        double actualBound = i / rate;
        return randomRoll(ThreadLocalRandom.current(), (int) Math.ceil(actualBound));
    }
}
