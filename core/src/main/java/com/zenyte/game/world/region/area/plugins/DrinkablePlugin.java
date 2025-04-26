package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 30-11-2018 | 19:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface DrinkablePlugin {

    default boolean drink(final Player player, final Drinkable potion) {
        return true;
    }
}
