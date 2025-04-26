package com.zenyte.game.world.entity.player.teleportsystem;

import com.zenyte.utils.TextUtils;

/**
 * @author Tommeh | 13-11-2018 | 17:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum TeleportCategory {

    CITIES,
    SKILLING,
    TRAINING,
    MINIGAMES,
    BOSSES,
    DUNGEONS,
    WILDERNESS,
    MISC,
    AGILITY,
    WOODCUTTING,
    BACK;

    public static final TeleportCategory[] VALUES = new TeleportCategory[] {
      CITIES, SKILLING, TRAINING, MINIGAMES, BOSSES, DUNGEONS, WILDERNESS, MISC,
    };

    public static final TeleportCategory[] SKILLING_C = new TeleportCategory[] {
            BACK,
            AGILITY,
            WOODCUTTING
    };

    public static TeleportCategory get(final int index) {
        return VALUES[index];
    }

    @Override
    public String toString() {
        return TextUtils.capitalize(name().toLowerCase().replace('_', ' '));
    }

}
