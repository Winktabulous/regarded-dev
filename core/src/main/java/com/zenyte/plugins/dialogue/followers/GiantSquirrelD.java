package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:12.38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class GiantSquirrelD extends Dialogue {

	public GiantSquirrelD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(2);
		switch(option) {
		case 0:
			player("So how come you are so agile?");
			npc(" If you were so nutty about nuts, maybe you would understand the great lengths we go to!");
			break;
		case 1:
			player("What's up with all that squirrel fur? I guess fleas need a home too.");
			npc("You're pushing your luck! Stop it or you'll face my squirrely wrath.");
			break;
			default:
				player("Did you ever notice how big squirrels' teeth are?");
				npc("No...");
				player("You could land a gnome glider on those things!");
				npc("Watch it, I'll crush your nuts!");
				break;
		}
	}

}
