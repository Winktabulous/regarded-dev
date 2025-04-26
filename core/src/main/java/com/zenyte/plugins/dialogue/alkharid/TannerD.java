package com.zenyte.plugins.dialogue.alkharid;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.Leather;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.interfaces.TanningInterface;

/**
 * @author Tommeh | 15 mei 2018 | 20:51:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class TannerD extends Dialogue {
	public TannerD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Greetings friend. I am a manufacturer of leather.");
		if (hasHides()) {
			npc("I see you have brought me some hides.<br><br>Would you like me to tan them for you?");
			options("What would you like to say?", "Yes please.", "No thanks.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10));
			player(5, "Yes please.").executeAction(() -> TanningInterface.sendTanningInterface(player));
			player(10, "No thanks.");
			npc("Very well, sir, as you wish.");
		} else {
			options("What would you like to say?", "Can I buy some leather then?", "Leather is rather weak stuff.").onOptionOne(() -> setKey(15)).onOptionTwo(() -> setKey(20));
			player(15, "Can I buy some leather then?");
			npc("I make leather from animal hides. Bring me some cowhides and one gold coin per hide, and I'll tan " +
                    "them into soft leather for you.");
			player(20, "Leather is rather weak stuff.");
			npc("Normal leather may be quite weak, but it's very cheap - I make it from cowhides for only 1 gp per " +
                    "hide - and it's so easy to craft that anyone can work with it.");
			npc("Alternatively you could try hard leather. It's not so easy to craft, but I only charge 3 gp per " +
                    "cowhide to prepare it, and it makes much sturdier armour.");
			npc("I can also tan snake hides and dragonhides, suitable for crafting into the highest quality armour for rangers.");
			player("Thanks, I'll bear it in mind.");
		}
	}

	private boolean hasHides() {
		for (final CraftingDefinitions.Leather leather : Leather.VALUES) {
			if (player.getInventory().containsItem(leather.getBase())) {
				return true;
			}
		}
		return false;
	}
}
