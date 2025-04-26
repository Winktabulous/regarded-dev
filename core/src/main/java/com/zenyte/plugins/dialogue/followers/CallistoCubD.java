package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:08.13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class CallistoCubD extends Dialogue {

	public CallistoCubD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Why the grizzly face?");
		npc("You're not funny...");
		player("You should get in the.... sun more.");
		npc("You're really not funny...");
		player("One second, let me take a picture of you with my.... kodiak camera.");
		npc(".....");
		player("Feeling.... blue.");
		npc("If you don't stop, I'm going to leave some... brown... at your feet, human.");
	}

}
