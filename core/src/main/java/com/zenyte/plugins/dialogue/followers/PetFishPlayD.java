package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 1:46.21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetFishPlayD extends Dialogue {

	public PetFishPlayD(Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		if (player.getAppearance().isMale())
			player("Jump! 'Cmon girl, Jump!");
		else
			player("Jump! 'Cmon boy, Jump!");
		plain("The fish bumps into the side of the fishbowl. Then it swims some more.");
		player("Good fish...");
	}

}
