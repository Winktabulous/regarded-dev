package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 0:11.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class ScorpiasOffspringD extends Dialogue {

	public ScorpiasOffspringD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("At night time, if I were to hold ultraviolet light over you, would you glow?");
		npc("Two things wrong there, human.");
		player("Oh?");
		npc("One, When has it ever been night time here?");
		npc("Two, When have you ever seen ultraviolet light around here?");
		player("Hm...");
		npc("In answer to your question though. Yes I, like every scorpion, would glow.");
	}

}
