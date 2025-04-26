package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.Objects;

public class SrarachaBossPetNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Metamorphosis", (player, npc) -> {
			if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
				player.sendMessage("This is not your pet.");
				return;
			}
			if (player.getNumericAttribute("sraracha_blue").intValue() <= 0 && player.getNumericAttribute("sraracha_orange").intValue() <= 0) {
				player.sendMessage("You haven't unlocked the metamorphosis ability yet.");
				return;
			}

			player.getDialogueManager().start(new Dialogue(player, npc) {
				@Override
				public void buildDialogue() {
					if (npc.getId() == BossPet.SRARACHA.getPetId()) {
						if (player.getNumericAttribute("sraracha_blue").intValue() > 0 && player.getNumericAttribute("sraracha_orange").intValue() > 0) {
							options(new DialogueOption("Blue", () -> transform(player, npc, BossPet.SRARACHA_BLUE)), new DialogueOption("Orange", () -> transform(player, npc, BossPet.SRARACHA_ORANGE)));
						} else if (player.getNumericAttribute("sraracha_blue").intValue() > 0) {
							transform(player, npc, BossPet.SRARACHA_BLUE);
						} else if (player.getNumericAttribute("sraracha_orange").intValue() > 0) {
							transform(player, npc, BossPet.SRARACHA_ORANGE);
						}
					} else if (npc.getId() == BossPet.SRARACHA_ORANGE.getPetId()) {
						if (player.getNumericAttribute("sraracha_blue").intValue() > 0) {
							options(new DialogueOption("Blue", () -> transform(player, npc, BossPet.SRARACHA_BLUE)), new DialogueOption("Red", () -> transform(player, npc, BossPet.SRARACHA)));
						} else {
							transform(player, npc, BossPet.SRARACHA);
						}
					} else {
						if (player.getNumericAttribute("sraracha_orange").intValue() > 0) {
							options(new DialogueOption("Orange", () -> transform(player, npc, BossPet.SRARACHA_ORANGE)), new DialogueOption("Red", () -> transform(player, npc, BossPet.SRARACHA)));
						} else {
							transform(player, npc, BossPet.SRARACHA);
						}
					}
				}
			});
		});
	}

	private static void transform(Player player, NPC npc, BossPet bossPet) {
		int id = bossPet.getPetId();
		npc.setTransformation(id);
		player.setPetId(id);
	}

	@Override
	public int[] getNPCs() {
		return new int[] { NpcId.SRARACHA_2144, NpcId.SRARACHA_11159, NpcId.SRARACHA_11160 };
	}

}
