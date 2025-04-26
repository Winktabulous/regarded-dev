package com.zenyte.plugins.dialogue.lumbridge;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 30. apr 2018 : 22:42:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class CookingTutorD extends Dialogue {

	public CookingTutorD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Last time I made a mistake in the kitchen, the chef put two pieces of bread either side of my head and called me an 'idiot sandwhich'.");
	}

}
