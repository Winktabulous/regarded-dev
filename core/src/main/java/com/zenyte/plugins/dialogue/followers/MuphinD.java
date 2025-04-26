package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Savions.
 */
public class MuphinD extends Dialogue {

	private static final Object[][] TRANSFORM_OPTIONS = {{NpcId.MUPHIN, "Metamorphosis (ranged)"}, {NpcId.MUPHIN_12006, "Metamorphosis (melee)"}, {NpcId.MUPHIN_12007, "Metamorphosis (shield)"}};

	public MuphinD(Player player, final NPC npc) {
		super(player, npc);
	}

	@Override public void buildDialogue() {
		if (player.getNumericAttribute("Muspah charged ice").intValue() < 1) {
			final DialogueOption[] options = new DialogueOption[3];
			options[0] = new DialogueOption("Muphin", this::muphin);
			int indx = 1;
			for (int i = 0; i < 3 && indx < options.length; i++) {
				final int npcId = (int) TRANSFORM_OPTIONS[i][0];
				if (npcId != npc.getId()) {
					options[indx++] = new DialogueOption((String) TRANSFORM_OPTIONS[i][1], () -> transform(player, npc, npcId));
				}
			}
			options("Select an option", options);
		} else {
			muphin();
		}
	}

	private void transform(Player player, NPC npc, final int npcId) {
		if(player.getFollower().getId() == npc.getId()) {
			player.getFollower().setTransformation(npcId);
			player.setPetId(npcId);
		} else {
			player.sendMessage("You cannot do this to someone else's pet");
		}
	}

	private void muphin() {
		setKey(4);
		final int random = Utils.random(3);
		switch (random) {
			case 0 -> {
				player(4, "Who's the cutest little nightmare-spawn in all of Gielinor?");
				npc(3, "...");
				player(4, "Is it you?!");
				npc(5, "...");
				player(6, "Yes, it's you my little Muphin!");
				npc(7, "Stop this at once.");
			}
			case 1 -> {
				player(2, "Can you teach me how to do that thing where you teleport around really quickly?");
				npc(3, "That requires moving between realms... Your simple form would not withstand it.");
				player(4, "You'd be surprised.");
			}
			case 2 -> {
				player(2, "You know... if you were bite-sized, I reckon you'd be quite nutritious.");
				npc(3, "Do NOT eat me, human.");
			}
		}
	}
}
