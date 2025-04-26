package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 20. mai 2018 : 18:35:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface CountDialogue {

	void run(final int amount);
	
	default void execute(final Player player, final int amount) {
		if (amount < 0) {
			return;
		}
        if (player.getTemporaryAttributes().get("interfaceInput") != this) {
            return;
        }
        run(amount);
        //The block inside run can change the attribute, so we re-check it.
        if (player.getTemporaryAttributes().get("interfaceInput") != this) {
            return;
        }
		player.getTemporaryAttributes().remove("interfaceInput");
		player.getTemporaryAttributes().remove("interfaceInputNoCloseOnButton");
	}
	
}
