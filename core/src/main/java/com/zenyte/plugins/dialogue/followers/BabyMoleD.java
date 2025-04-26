package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:00.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class BabyMoleD extends Dialogue {

	public BabyMoleD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Hey, Mole. How is life above the ground?");
		npc("Well, the last time I was above ground, I was having to contend with people "
				+ "throwing snow at some weird yellow duck in my park.");
		player("Why were they doing that?");
		npc("No idea, I didn't stop to ask as an angry mob was closing in on them pretty quick.");
		player("Sounds awful.");
		npc("Anyways, keep Molin'!");
	}

}
