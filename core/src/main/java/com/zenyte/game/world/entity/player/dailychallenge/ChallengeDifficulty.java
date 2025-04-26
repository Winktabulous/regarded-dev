package com.zenyte.game.world.entity.player.dailychallenge;

import com.zenyte.utils.TextUtils;

/**
 * @author Tommeh | 02/05/2019 | 22:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum ChallengeDifficulty {
    EASY(100_000),
    MEDIUM(200_000),
    HARD(500_000),
    ELITE(1_000_000);

    private final int coins;

    ChallengeDifficulty(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        return TextUtils.formatName(name().toLowerCase());
    }
}
