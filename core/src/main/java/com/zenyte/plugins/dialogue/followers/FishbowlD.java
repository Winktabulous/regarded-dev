package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:11.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class FishbowlD extends Dialogue {

	public FishbowlD(Player player, int npcId) {
		super(player, npcId);
	}

	@Override
	public void buildDialogue() {
		player("Good fish. Just keep swimming... swimming... swimming...");
		plain("The fish swims. It is clearly an obedient fish.");
	}

}
