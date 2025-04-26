package com.zenyte.game.content.compcapes;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 18:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HansComp extends NPCPlugin {

	public static final int PRICE = 5_000_000;
	private static final Item COINS = new Item(995, PRICE);

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

			@Override
			public void buildDialogue() {
				player("Hello Hans, what is that cape that you're wearing?");
				npc("A good question. I discovered it recently, it turns out this cape might be the product, or at least involved with a Second Age mage by the name of Dahmaroc.");
				player("Dahmaroc?");
				npc("He was a powerful mage back in the Second Age. Very skill-focused too, so this cape was a particular find.");
				player("What do you mean by that?");
				npc("Well, generally, his magical abilities were focused away from combat - it seems this cape is under the most powerful enchantment we've ever seen.");
				player("This cape is enchanted?");
				npc("Yes, and more than we can grasp. It physically repels anyone who tries to wear it. I had quite a hassle getting it on.");
				player("So only you have worn this cape?");
				npc("Only I can! It's like it has a mind of its own, judging those who try as unworthy!");

				options(new DialogueOption("Can I try?", () -> setKey(100)),
						new DialogueOption("How interesting.", () -> setKey(30)));
				player(30, "How interesting.");
				npc("Thanks for wasting my time.");

				player(100, "Can I try?");
				npc("Sure, good luck!").executeAction(() -> {
					int tier = CompletionistCape.checkRequirements(player);
					if (tier == 0) {
						setKey(150);
						CompletionistCape.noRequirements(player);
					} else {
						setKey(200);
					}
				});

				npc(150, "Sorry, it doesn't look like you are worthy of this cape. At least not yet...");
				npc(200, "I think the cape is identifying its true owner.");
				player("You mean I can have it?");
				npc("Well, yes, but... I can't just let you take it from me. You may be the true owner, but it is one of the most treasured items I have.");
				npc("I suppose if I were compensated, perhaps I could let you take it... How does " + Utils.format(PRICE) + " coins sound?");
				options(new DialogueOption("That sounds fair.", () -> setKey(300)),
						new DialogueOption("It sounds like a joke!", () -> setKey(400)));

				player(300, "That sounds fair.").executeAction(() -> {
					if (!player.getInventory().hasFreeSlots()) {
						npc("You don't seem to have enough space in your inventory.");
					} else {
						if (!player.getInventory().containsItem(COINS)) {
							setKey(500);
						} else {
							int capeId = CompletionistCape.tierToCape(CompletionistCape.checkRequirements(player));
							if (capeId != -1 && !player.getInventory().deleteItem(COINS).isFailure()) {
								player.getInventory().addItem(new Item(capeId));
								setKey(600);
							} else {
								setKey(500);
							}
						}
					}
				});
				player(400, "It sounds like a joke!");
				npc("So be it, the cape stays where it is.");

				npc(500, "You don't seem to have enough coins with you.");
				npc(600, "There you go, wear it with pride.");
			}
		}));
	}

	@Override
	public int[] getNPCs() {
		return new int[]{16065};
	}

}
