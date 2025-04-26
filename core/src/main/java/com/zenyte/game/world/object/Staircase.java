package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 17. dets 2017 : 16:05.38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Staircase {

	public static final void climb(final Player player, final Location tile) {
		player.setLocation(tile);
	}
	
	public static final void climb(final Player player, final Location up, final Location down) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Where would you like to go?",
						"Climb up.",
						"Climb down.")
				.onOptionOne(() -> {
					finish();
					player.setLocation(up);
				})
				.onOptionTwo(() -> {
					finish();
					player.setLocation(down);
				});
			}
		});
	}
	
}
