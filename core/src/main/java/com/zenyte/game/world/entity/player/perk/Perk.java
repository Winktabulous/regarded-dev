package com.zenyte.game.world.entity.player.perk;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 3-1-2019 | 19:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public abstract class Perk {

    public abstract int getCost();

    public abstract String getName();

    public abstract String getDescription();

    public boolean valid() {
        return true;
    }

    public void consume(final Player player) {
    }

}
