package com.zenyte.game.content.boss.nightmare.npc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class ShuraNPCPlugin extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> {
			if (player.getVarManager().getBitValue(10137) == 0) {
				player.getDialogueManager().start(new Dialogue(player, npc) {
					@Override
					public void buildDialogue() {
						player("What's going on here?");
						npc("Nothing good. This dungeon is tainted and has become the new home of the Nightmare.");
						player("The Nightmare?");
						npc("A being of pure darkness. No living creature is safe from her hunger. She feasts upon her victims while they sleep, draining their energy without even needing to touch them.");
						npc("Those that she feeds upon experience horrifying nightmares, unlike anything you could possibly imagine. Eventually, they become too weak to even wake. Death claims them soon after.");
						player("Nightmares? Hence the name?");
						npc("Indeed. Our kind are not always the most creative.");
						player("So how did she end up here?");
						npc("For generations, she terrorised the island of Ashihama, my home. I lost a lot of good friends to her. Sometimes, I even thought I would lose myself. For some, the nightmares become too much to bear.");
						npc("Some of us refused to give in though. Everything has a weakness, including the Nightmare, and eventually we found it. We drove her off the island, but she managed to endure.");
						npc("The people here are ill, suffering from a deep sleep from which few wake. They call it the Sleeper Plague. The illness made these people easy prey for the Nightmare, so she made this place her new home.");
						player("And you followed her here?");
						npc("While she still exists, my home will never be truly safe. Thanks to the Sleeper Plague, her power has grown faster than it ever did on Ashihama. If she is allowed to grow too strong, there won't be any way to stop her.");
						npc("I've managed to contain her within this section of the dungeon. She has no desire to leave for the moment. She has no need to, after all.");
						npc("But make no mistake, I won't be able to stop her if she decides to move on.");
						player("She doesn't look too dangerous at the moment.");
						npc("Looks can be deceiving. The battle with the Nightmare is a mental one, not a physical one. If you try to take her on, she will pull you into her dream - one where she makes the rules.");
						player("But if it's a dream, how can it be dangerous?");
						npc("It's her dream, not yours. Dying in her dream will destroy your mind, killing you in this world as well.");
						player("Sounds lovely. So how can we stop her?");
						npc("If you're willing to help, enter her dream and fight to keep her at bay. Our only hope is to prevent her from growing too strong.");
						npc("You can go in alone, but I wouldn't recommend it. The more support you have, the better your chances.");
						player("How do I keep her at bay?");
						npc("There are four totems in the area that can be used to harm her. First, you'll need to take down her shield for the totems to have an effect. Once her shield is down, use your energy to charge up the totems.");
						npc("She won't sit about while you do this though, so you'll need to take care if you want to survive.");
						player("And what's in it for me if I do survive?");
						npc("You mean apart from saving the world? She's claimed many victims over the years. I'm sure she's claimed many items as well. Maybe some of them will be of value to you.");
						player("Works for me. I'd better get started then.");
						npc("Good luck. Return to me if you need more information.").executeAction(() -> player.getVarManager().sendBit(10137, 1));
					}
				});
			} else {
				player.getDialogueManager().start(new Dialogue(player, npc) {

					@Override
					public void buildDialogue() {
						npc("Human.");
						player("I'd better get going.");
						npc("Good luck.");
					}
				});
			}
		});
		bind("Collect", (player, npc) -> {
			if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.NIGHTMARE || player.getRetrievalService().getContainer().isEmpty()) {
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
					npc("I do. Try to be more careful with them next time.");
					setOnCloseRunnable(() -> GameInterface.ITEM_RETRIEVAL_SERVICE.open(player));
				}
			});
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[]{9459};
	}

}
