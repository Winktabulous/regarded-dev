package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:58.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetSnakelingD extends Dialogue {

	public PetSnakelingD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Hey little snake!");
		npc("Soon, Zulrah shall establish dominion over this plane.");
		player("Wanna play fetch?");
		npc("Submit to the almighty Zulrah.");
		player("Walkies? Or slidies...?");
		npc("Zulrah's wilderness as a God will soon be demonstrated.");
		player("I give up...");
	}

}
