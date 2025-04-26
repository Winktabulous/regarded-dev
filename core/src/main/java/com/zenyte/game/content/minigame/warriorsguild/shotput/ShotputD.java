package com.zenyte.game.content.minigame.warriorsguild.shotput;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 21:48.00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShotputD extends Dialogue {

	public ShotputD(Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		options("Choose your style",
				"Standing throw",
				"Step and throw",
				"Spin and throw")
		.onOptionOne(() -> {
			finish();
			ShotputArea.throwShot(player, 0);
		})
		.onOptionTwo(() -> {
			finish();
			ShotputArea.throwShot(player, 1);
		})
		.onOptionThree(() -> {
			finish();
			ShotputArea.throwShot(player, 2);
		});
	}

}
