package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:20.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class VenenatisSpiderlingD extends Dialogue {

	public VenenatisSpiderlingD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("It's a damn good thing I don't have arachnophobia.");
		npc("We're misunderstood. Without us in your house, you'd be infested with flies and other REAL nasties.");
		player("Thanks for that enlightening fact.");
		npc("Everybody gets one.");
	}

}
