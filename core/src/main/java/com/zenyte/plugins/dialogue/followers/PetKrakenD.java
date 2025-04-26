package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public final class PetKrakenD extends Dialogue {

	public PetKrakenD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("What's Kraken?");
		npc("Not heard that one before.");
		player("How are you actually walking on land?");
		npc("We have another leg, just below the center of our body that we use to move across solid surfaces.");
		player("That's.... interesting.");
	}

}
