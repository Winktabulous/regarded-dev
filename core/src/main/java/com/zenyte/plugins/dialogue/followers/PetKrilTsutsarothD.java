package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:51.10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetKrilTsutsarothD extends Dialogue {

	public PetKrilTsutsarothD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("How's life in the light?");
		npc("Burns slightly.");
		player("You seem much nicer than your father. He's mean.");
		npc("If you were stuck in a very dark cave for centuries you'd be pretty annoyed too.");
		player("I guess.");
		npc("He's actually quite mellow really.");
		player("Uh.... Yeah.");
	}

}
