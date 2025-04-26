package com.zenyte.game.content.minigame.motherlode;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 26/06/2019 00:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Paydirt {
    //Took the weights from a video of "Loot from 10,000 paydirt - weights are basically the amounts he got out of it.
    COAL(453, 3028, 30, 0),
    GOLDEN_NUGGET(12012, 0, 30, 0), //Keep golden nugget weight at 0, it's an exception!
    GOLD_ORE(444, 2267, 40, 15),
    MITHRIL_ORE(447, 2786, 55, 30),
    ADAMANTITE_ORE(449, 1651, 70, 45),
    RUNITE_ORE(451, 178, 85, 75);
    private static final Paydirt[] values = values();
    private final int id;
    private final int weight;
    private final int level;
    private final int xp;

    public static final Optional<Paydirt> generate(@NotNull final Player player) {
        //Get total weight based on user's current level - or if current is below physical level, physical. (Prevents debuff level from being used)
        final int total = Utils.random(total(Math.max(player.getSkills().getLevel(SkillConstants.MINING), player.getSkills().getLevelForXp(SkillConstants.MINING))));
        int current = 0;
        for (final Paydirt value : values) {
            if ((current += value.weight) >= total) {
                /*Quote by Mod Ash: "Sorry, I just rechecked. It's actually a flat rate of 2.74% at all levels for a paydirt to become a gold nugget." */
                if (Utils.randomDouble() < 0.0674F) {
                    return Optional.of(GOLDEN_NUGGET);
                }
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static final Optional<Paydirt> get(final int id) {
        return Optional.ofNullable(CollectionUtils.findMatching(values, value -> value.id == id));
    }

    private static final int total(final int level) {
        int total = 0;
        for (final Paydirt value : values) {
            if (value.level <= level) {
                total += value.weight;
            }
        }
        return total;
    }

    Paydirt(int id, int weight, int level, int xp) {
        this.id = id;
        this.weight = weight;
        this.level = level;
        this.xp = xp;
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }
}
