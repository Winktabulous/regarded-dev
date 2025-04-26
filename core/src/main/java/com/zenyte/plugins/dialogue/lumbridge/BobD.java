package com.zenyte.plugins.dialogue.lumbridge;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 30. apr 2018 : 21:56:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class BobD extends Dialogue {

	public BobD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		options(TITLE, new DialogueOption("Give me a quest!", () -> setKey(10)), new DialogueOption("Have you anything to sell?", () -> setKey(20)), new DialogueOption("Can you repair my items for me?", () -> setKey(30)));
		player(10, "Give me a quest!");
		npc("Get yer own!");
		player(20, "Have you anything to sell?");
		npc("Yes! I buy and sell axes! Take your pick (or axe)!").executeAction(() -> player.openShop("Bob's Brilliant Axes"));
		player(30, "Can you repair my items for me?");
		npc("Of course I'll repair it, though the materials may cost you. Just hand me the item and I'll have a look.");
	}

}
