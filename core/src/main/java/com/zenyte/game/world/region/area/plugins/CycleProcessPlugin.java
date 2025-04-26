package com.zenyte.game.world.region.area.plugins;

/**
 * @author Kris | 29. juuni 2018 : 00:10:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface CycleProcessPlugin {

    void process();

    default void postProcess() {
    }
	
}
