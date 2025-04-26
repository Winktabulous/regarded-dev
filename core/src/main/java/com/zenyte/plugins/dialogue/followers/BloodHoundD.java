package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:04.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class BloodHoundD extends Dialogue {

	public BloodHoundD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int random = Utils.random(4);
		switch(random) {
		case 0:
			player("How come I can talk to you without an amulet?");
			npc ("*Woof woof bark!* Elementary, it's due to the influence of the -SQUIRREL-!");
			break;
		case 1:
			player("Walkies!");
			npc("...");
			break;
		case 2:
			player("Can you help me with this clue?");
			npc("*Woof! Bark yip woof!* Sure! Eliminate the impossible first.");
			player("And then?");
			npc("*Bark! Woof bark bark.* Whatever is left, however improbable, must be the answer.");
			player("So helpful.");
			break;
		case 3:
			player("I wonder if I could sell you to a vampire to track down dinner.");
			npc("*Woof bark bark woof* I have teeth too you know, that joke was not funny.");
			break;
			default:
				player("Hey boy, what's up?");
				npc("*Woof! Bark bark woof!* You smell funny.");
				player("Err... funny strange or funny ha ha?");
				npc("*Bark bark woof!* You aren't funny.");
				break;
		}
	}

}
