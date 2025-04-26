package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 22:54.49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class AbyssalOrphanD extends Dialogue {

	public AbyssalOrphanD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("You killed my father.");
		options(TITLE, "Yeah, don't take it personally.", "No, I am your father.")
		.onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10));
		player(5, "Yeah, don't take it personally.");
		npc("In his dying moment, my father poured his last ounce of "
				+ "strength into my creation. My being is formed from his remains.");
		npc("When your own body is confusmed to nourish the Nexus, and an army of scions "
				+ "arises from your corpse, I trust you will not take it personally either.");
		player(10, "No, I am your father.");
		if (player.getAppearance().isMale())
			npc("No you're not.");
		else
			npc ("Human biology may be unfamiliar to me, but nevertheless I doubt that very much");
	}

}
