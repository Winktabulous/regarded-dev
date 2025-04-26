package com.zenyte.game.world.region.area.plugins;

/**
 * @author Kris | 25/06/2019 22:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface RandomEventRestrictionPlugin {

    default boolean disablesRandomEvents() {
        return true;
    }

}
