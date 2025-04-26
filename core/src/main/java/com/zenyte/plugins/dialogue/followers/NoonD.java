package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:28.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class NoonD extends Dialogue {

	public NoonD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(2);
		switch(option) {
		case 0:
			player("Hello little one.");
			npc("I may be small but at least I'm perfectly formed.");
			break;
		case 1:
			player("What's your favourite rock?");
			npc("You're going tufa with that question. That's personal.");
			player("Was just trying to make light conversation, not trying to aggregate you.");
			break;
			default:
				player("Metaphorically speaking, do you have a heart of stone?");
				npc("Yes, but you're not having it.");
				break;
		}
	}

}
