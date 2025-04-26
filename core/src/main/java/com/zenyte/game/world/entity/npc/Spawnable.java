package com.zenyte.game.world.entity.npc;

import com.zenyte.plugins.Plugin;

/**
 * @author Kris | 16. juuni 2018 : 20:14:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface Spawnable extends Plugin {

	/** Used to validate whether the given class(implementing this interface) matches the id or string in the parameters. */
    boolean validate(final int id, final String name);

}
