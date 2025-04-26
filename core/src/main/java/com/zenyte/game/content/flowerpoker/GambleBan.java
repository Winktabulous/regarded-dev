package com.zenyte.game.content.flowerpoker;

import com.zenyte.game.world.entity.player.Player;

public class GambleBan {

    public static boolean hasGambleBan(Player player) {
        return player.getAttributes().containsKey("GAMBLE_BAN");
    }

    public static boolean isGambleBanValid(Player player) {
        return hasGambleBan(player) && (player.getNumericAttribute("GAMBLE_BAN").longValue() > System.currentTimeMillis());
    }
}
