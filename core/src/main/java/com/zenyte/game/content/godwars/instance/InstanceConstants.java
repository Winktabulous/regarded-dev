package com.zenyte.game.content.godwars.instance;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;

/**
 * @author Kris | 12/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InstanceConstants {

    public static final int INSTANCE_COST = 150_000;
    public static final int ZAROS_INSTANCE_COST = 300_000;

    public static final int getInstanceCost(final Player player, int cost) {
        if (player.getCombatAchievements().hasTierCompleted(CATierType.ELITE)) {
            cost -= 25_000;
        }
        if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER)) {
            cost -= 25_000;
        }
        if (player.getCombatAchievements().hasTierCompleted(CATierType.GRANDMASTER)) {
            cost -= 25_000;
        }
        return cost;
    }

}
