package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 23. mai 2018 : 03:37:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface NameDialogue {

	void run(final String name);
	
	default void execute(final Player player, final String name) {
		if (name == null || name.isEmpty()) {
			return;
		}
        if (player.getTemporaryAttributes().get("interfaceInput") != this) {
            return;
        }
        run(name);
        //The block inside run can change the attribute, so we re-check it.
        if (player.getTemporaryAttributes().get("interfaceInput") != this) {
            return;
        }
		player.getTemporaryAttributes().remove("interfaceInput");
	}
	
}
