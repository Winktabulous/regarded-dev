package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.sailing.FremennikIslesSailing;
import com.zenyte.game.content.sailing.FremennikIslesSailing.SailingDestination;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 21 mrt. 2018 : 17:04:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class JarvaldD extends Dialogue {
	public JarvaldD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final boolean atWaterbirthIsland = player.inArea("Waterbirth Island");
		if (!atWaterbirthIsland) {
			npc("Greetings, " + player.getPlayerInformation().getDisplayname() + "! So what brings you to fair Rellekka? It has been too long since you have drunk in the long hall with us and sang of your battles!");
			npc("What would you like to ask about?");
			options(TITLE, "Travel to Waterbirth Island", "Cancel").onOptionOne(() -> setKey(5));
			player(5, "Travel to Waterbirth Island");
			npc("Well of course my friend but I must say it's a very dangerous island!").executeAction(() -> {
				FremennikIslesSailing.sail(player, SailingDestination.WATERBIRTH_ISLAND);
				finish();
			});
		} else {
			npc("You wish to leave for Waterbirth Island now?");
			options(TITLE, "YES", "NO").onOptionOne(() -> FremennikIslesSailing.sail(player, SailingDestination.WATERBIRTH_ISLAND));
		}
	}
}
