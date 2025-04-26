package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public final class RockyD extends Dialogue {

	public RockyD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(2);
		switch(option) {
		case 0:
			player("*Whistles*");
			plain("You slip your hand into Rocky's pocket.");
			npc("OY!! You're going to have to do better than that! Sheesh, what an amateur.");
			break;
		case 1:
			player("Is there much competition between you raccoons and the magpies?");
			npc("Magpies have nothing on us! They're just interested in shinies.");
			player("Us raccoons have a finer taste, we can see the value in anything, whether it shines or not.");
			break;
			default:
				player("Hey Rocky, do you want to commit a bank robbery with me?");
				npc("If that is the level you are at, I do not wish to participate in criminal acts with you " + player.getPlayerInformation().getDisplayname() + ".");
				player("Well what are you interested in stealing?");
				npc("The heart of a lovely raccoon called Rodney.");
				player("I cannot really help you there I'm afraid.");
				break;
		}
	}

}
