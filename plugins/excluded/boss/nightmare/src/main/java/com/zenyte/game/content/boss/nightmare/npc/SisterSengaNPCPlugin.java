package com.zenyte.game.content.boss.nightmare.npc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class SisterSengaNPCPlugin extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> {
			if (player.getVarManager().getBitValue(10239) == 0) {
				player.getDialogueManager().start(new Dialogue(player, npc) {
					@Override
					public void buildDialogue() {
						player("What are you doing up here?");
						npc("Keeping an eye on Mother.");
						player("Mother?");
						npc("Phosani, the founder of our sect and creator of this sanctuary. She is our mother, and always will be.");
						player("I see... So where is she?");
						npc("She lies in this coffin.");
						player("Oh... So she's dead?");
						npc("Dead? No. She sleeps the everlasting slumber.");
						player("Right. The coffin looks pretty secure to me. Why do you need to keep an eye on her.");
						npc("The creature down there, the one that Siren calls the Nightmare. It has been invading Mother's mind, feeding on her.");
						npc("Mother is strong, but not strong enough to fight it off alone. She needs help.");
						player("Needs help does she? Maybe I could be of assistance?").executeAction(() -> {
							if (player.getNotificationSettings().getKillcount("The Nightmare") == 0) {
								setKey(100);
							} else {
								setKey(200);
							}
						});

						npc(100, "The offer is appreciated. I fear you do not have the abilities to take on that sort of challenge though.");
						player("What abilities do I need?");
						npc("The horror that has manifested itself in Mother's mind is far stronger than the one fought in the Nightmare's dream. If you hope to have a chance, you'll want to take on the weaker form first. That Siren down there can help you.");
						player("Fair enough. I'll be back once I have more experience.");
						npc("Be safe, traveller.");

						npc(200, "That would be appreciated, traveller. You can use the pool here to enter Mother's mind and take on the Nightmare within.");
						npc("Be warned though. If you enter, you enter alone. No one else can help you with this one.");
						player("I see. I'll have to be careful then.");
						npc("Indeed. Good luck, traveller.").executeAction(() -> player.getVarManager().sendBit(10239, 1));
					}
				});
			} else {
				player.getDialogueManager().start(new Dialogue(player, npc) {
					@Override
					public void buildDialogue() {
						npc("Greetings, traveller.");
						player("I'd better get going.");
						npc("Be safe, traveller.");
					}
				});
			}
		});
		bind("Collect", (player, npc) -> {
			if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.PHOSANI_NIGHTMARE || player.getRetrievalService().getContainer().isEmpty()) {
				player.getDialogueManager().start(new Dialogue(player, npc) {

					@Override
					public void buildDialogue() {
						npc("Sorry, but I don't have anything for you. If I did have any of your items, but you died before collecting them, they'll now be lost.");
					}
				});
				return;
			}
			player.getDialogueManager().start(new Dialogue(player, npc) {
				@Override
				public void buildDialogue() {
					player("I see. I'll have to be careful then.");
					npc("I do, traveller. You should be more cautious with them.");
					setOnCloseRunnable(() -> GameInterface.ITEM_RETRIEVAL_SERVICE.open(player));
				}
			});
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[]{9473};
	}

}
