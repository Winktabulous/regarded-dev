package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.content.consumables.Edible;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 30-11-2018 | 19:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface EdiblePlugin {

    default boolean eat(final Player player, final Edible food) {
        return true;
    }
}
