package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:44.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetDagannothPrimeD extends Dialogue {

	public PetDagannothPrimeD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("So despite there being three kings, you're clearly the leader, right?");
		npc("Definitely.");
		player("I'm glad I got you as a pet.");
		npc("Ugh. Human, I'm not a pet.");
		player("Stop following me then.");
		npc("I can't seem to stop.");
		player("Pet.");
	}

}
