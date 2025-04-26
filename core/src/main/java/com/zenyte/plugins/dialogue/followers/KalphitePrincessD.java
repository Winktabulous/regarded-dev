package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:24.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class KalphitePrincessD extends Dialogue {

	public KalphitePrincessD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("What is it with your kind and potato cactus?");
		npc("Truthfully?");
		player("Yeah, please.");
		npc("Soup. We make a fine soup with it.");
		player("Kalphites can cook?");
		npc("Nah, we just collect it and put it there because we know fools like yourself "
				+ "will come down looking for it then inevitably be killed by my mother.");
		player("Evidently not, that's how I got you!");
		npc("Touché.");
	}

}
