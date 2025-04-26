package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:47.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetDagannothSupremeD extends Dialogue {

	public PetDagannothSupremeD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Hey, so err... I kind of own you now.");
		npc("Tsssk. Next time you enter those caves, human, my father will be having words.");
		player("Maybe next time I'll enqueue your brothers to my collection.");
	}

}
