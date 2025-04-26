package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.content.skills.slayer.Slayer;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class SumonaD extends Dialogue {

	public SumonaD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final Slayer slayer = player.getSlayer();
		npc("'Ello, and what are you after then?");
		options(TITLE, "I need another assignment.", "Have you any rewards for me, or anything to trade?", "Let's talk" +
				" about the difficulty of my assignments.", "Let's talk about the Wilderness tasks.", "Cancel my Task (150k GP)").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10)).onOptionThree(() -> setKey(15)).onOptionFour(() -> setKey(200)).onOptionFive(() -> setKey(999));

		player(5, "I need another assignment.").executeAction(() -> {
			finish();
			player.getDialogueManager().start(new SumonaAssignmentD(player, npc));
		});

		player(10, "Have you any rewards for me, or anything to trade?");
		npc("I have quite a few rewards you can earn, and a wide<br><br>variety of Slayer equipment for sale.");
		options(TITLE, "Look at rewards.", "Look at shop.", "Cancel.").onOptionOne(slayer::openInterface).onOptionTwo(() -> player.openShop("Slayer Equipment"));

		player(15, "Let's talk about the difficulty of my assignments.");
		if (player.getSlayer().isCheckingCombat()) {
			npc("The Slayer Masters will take your combat l\tevel into account when choosing tasks for you, so you " +
					"shouldn't get anything too hard.");
			options(TITLE, "That's fine, I don't want anything too tough.", "Stop checking my combat level - I can take anything!").onOptionOne(() -> setKey(25)).onOptionTwo(() -> setKey(30));
			player(25, "That's fine, I don't want anything too tough.");
			npc("Okay, we'll keep checking your combat level.");
			player(30, "Stop checking my combat level - I can take anything!").executeAction(() -> player.getSlayer().setCheckingCombat(false));
			npc("Okay, from now on, all the Slayer Masters will assign you anything from their lists, regardless of your combat level.");
		} else {
			npc("The Slayer Masters may currently assign you any task in our lists, regardless of your combat level.");
			options(TITLE, "That's fine - I can handle any task.", "In future, please don't give anything too tough.").onOptionOne(() -> setKey(25)).onOptionTwo(() -> setKey(30));
			player(25, "That's fine - I can handle any task.");
			npc("That's the spirit.");
			player(30, "In future, please don't give anything too tough.").executeAction(() -> player.getSlayer().setCheckingCombat(true));
			npc("Okay, from now on, all the Slayer Masters will take your combat level into account when choosing " +
					"tasks for you, so you shouldn't get anything too hard.");
		}

		player(20, "Er... Nothing...");

		player(200, "Let's talk about the Wilderness tasks.");
		if (player.getSlayer().sumonaAssignWildernessTasks()) {
			npc("I will assign you Wilderness tasks that are in my list.");
			options(TITLE, "That's fine.", "Avoid the Wilderness at any cost!").onOptionOne(() -> setKey(250)).onOptionTwo(() -> setKey(300));
			player(250, "That's fine.");
			npc("Okay, let me know if anything.");
			player(300, "Avoid the Wilderness at any cost!").executeAction(() -> player.getSlayer().setSumonaAssignWildernessTasks(false));
			npc("Okay, from now on, you will not get any Wilderness tasks.");
		} else {
			npc("I will not assign you Wilderness tasks that are in my list.");
			options(TITLE, "That's fine.", "In future, give me Wilderness tasks.").onOptionOne(() -> setKey(250)).onOptionTwo(() -> setKey(300));
			player(250, "That's fine.");
			npc("That's the spirit.");
			player(300, "In future, give me Wilderness tasks.").executeAction(() -> player.getSlayer().setSumonaAssignWildernessTasks(true));
			npc("Okay, from now on, I will assign you tasks that are in the Wilderness");
		}
		player(999, "Cancel my Task (150k GP)").executeAction(() -> {
			finish();
			if(!player.getInventory().containsItem(995, 150_000)
					|| player.getInventory().deleteItem(995, 150_000).isFailure()) {
				player.sendMessage("You do not have the required funds to do that.");
			} else {
				player.getSlayer().removeTask();
			}
		});
	}

}
