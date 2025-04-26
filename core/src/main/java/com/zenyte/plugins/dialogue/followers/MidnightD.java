package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:26.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public final class MidnightD extends Dialogue {

	public MidnightD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int option = Utils.random(2);
		switch (option) {
		case 0:
			player("Hello little other one.");
			npc("Other?");
			player("Yes, don't you have a sister?");
			npc("I don't want to chalk about it.");
			break;
		case 1:
			player("Sometimes I'm worried you'll attack me whilst my back is turned.");
			npc("Are you petrified of my tuffness?");
			player("Not really, but your puns are awful.");
			npc("I thought they were clastic.");
			break;
			default:
				player("I feel like our relationship is slowly eroding away.");
				npc("Geode willing.");
				break;
		}
	}

}
