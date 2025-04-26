package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:20.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class JalNibRekD extends Dialogue {

	public JalNibRekD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(2);
		switch(option) {
		case 0:
			player("Yo Nib, what's going on?");
			npc("Nibnib? Kl-Rek Nib?");
			plain("Jal-Nib-Rek nips you.");
			player("What'd you do that for?");
			npc("Heh Nib get you.");
			break;
		case 1:
			player("What'd you have for dinner?");
			npc("Nibblings!");
			player("Nibblings of what exactly?");
			npc("Nib.");
			player("Oh no! That's horrible.");
			break;
			default:
				player("Can you speak like a human can Nib?");
				npc("No, I most definitely can not.");
				player("Aren't you speaking like a human right now...?");
				npc("Jal-Nib-Rek Nib Kl-Jal, Zuk is mum.");
				player("Interesting.");
				break;
		}
	}

}
