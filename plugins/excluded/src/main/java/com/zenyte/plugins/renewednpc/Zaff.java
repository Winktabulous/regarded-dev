package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.object.ZaffBarrelObject;

public class Zaff extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> {
			player.getDialogueManager().start(new Dialogue(player, npc) {
				@Override
				public void buildDialogue() {
					npc("Would you like to buy or sell some staffs? Or is there something else you need?");
					if (ZaffBarrelObject.canPurchase(player)) {
						options(new DialogueOption("Yes, please!", () -> player.openShop("Zaff's Superior Staffs!")),
								new DialogueOption("No, thank you.", () -> setKey(10)),
								new DialogueOption("Have you any extra stock of battlestaffs I can buy?", () -> setKey(20))
						);
					} else {
						options(new DialogueOption("Yes, please!", () -> player.openShop("Zaff's Superior Staffs!")), new DialogueOption("No, thank you.", () -> setKey(10)));
					}

					player(10, "No, thank you.");
					npc("Well, 'stick' your head in again if you change your mind.");
					player("Huh, terrible pun! You just can't get the 'staff' these days!");

					player(20, "Have you any extra stock of battlestaffs I can buy?");
					npc("For you, my friend, maybe. Take a look in the barrel in the corner.");
				}
			});
		});
		bind("Trade", (player, npc) -> player.openShop("Zaff's Superior Staffs!"));
	}

	@Override
	public int[] getNPCs() {
		return new int[]{NpcId.ZAFF};
	}
}
