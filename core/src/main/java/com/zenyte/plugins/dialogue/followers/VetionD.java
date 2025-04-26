package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:21.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class VetionD extends Dialogue {

	public VetionD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Who is the true lord and king of the lands?");
		npc("The mighty heir and lord of the Wilderness.");
		player("Where is he? Why hasn't he lifted your burden?");
		npc("I have not fulfilled my purpose.");
		player("What is your purpose?");
		npc("Not what is, what was. A great war tore this land apart and, "
				+ "for my failings in protecting this land, I carry the burden of its waste.");
	}

}
