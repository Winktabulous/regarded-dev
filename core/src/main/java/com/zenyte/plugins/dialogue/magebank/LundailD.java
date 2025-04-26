package com.zenyte.plugins.dialogue.magebank;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 29 mei 2018 | 19:05:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class LundailD extends Dialogue {

	public LundailD(Player player, NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Hello Sir.");
		npc("How can I help you, brave adventurer?");
		options(TITLE, "What are you selling?", "What's that big old building above us?").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10));
		player(5, "What are you selling?");
		npc("I sell rune stones. I've got some good stuff, some really<br><br>powerful little rocks. Take a look.").executeAction(() -> player.openShop("Lundail's Arena-side Rune Shop"));
		player(10, "What that big old building above us?");
		npc("That, my friend, is the mage battle arena. Top mages<br><br>come from all over " + GameConstants.SERVER_NAME + " to compete in the arena.");
		player("Wow.");
		npc("Few return, most get fried, hence the smell.");
		player("Hmmm.. I did notice.");
	}

}
