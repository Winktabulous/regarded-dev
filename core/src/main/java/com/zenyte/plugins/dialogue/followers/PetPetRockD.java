package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:56.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetPetRockD extends Dialogue {

	public PetPetRockD(final Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		player("Who's a good rock then? Yes you are... You're such a good rock... ooga booga googa.");
		plain("Your rock seems a little happier.");
	}

}
