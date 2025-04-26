package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.Position;

/**
 * @author Kris | 03/07/2019 15:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface LayableTrapRestrictionPlugin {

    default String trapRestrictionMessage() {
        return "You can't place a trap here.";
    }

    default boolean canPlaceTrap(Position position) {
        return false;
    }

}
