package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:16.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class TanglerootD extends Dialogue {

	public TanglerootD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(2);
		switch(option) {
		case 0:
			player("How are you doing today?");
			npc("I am Tangleroot!");
			break;
		case 1:
			player("Hello there pretty plant.");
			npc("I am Tangleroot!");
			break;
			default:
				player("I am Tangleroot!");
				npc("I am " + player.getPlayerInformation().getDisplayname() + ".");
				break;
		}
	}

}
