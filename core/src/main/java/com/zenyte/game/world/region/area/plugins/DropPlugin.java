package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 30-11-2018 | 19:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface DropPlugin extends IDropPlugin {

    default boolean drop(final Player player, final Item item) {
        return true;
    }

    default boolean dropOnGround(final Player player, final Item item) {
        return true;
    }

    default int visibleTicks(final Player player, final Item item) {
        return 200;
    }
    default int invisibleTicks(final Player player, final Item item) {
        return 100;
    }
}
