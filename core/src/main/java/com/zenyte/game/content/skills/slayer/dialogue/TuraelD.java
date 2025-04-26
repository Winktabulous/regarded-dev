package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26. okt 2017 : 22:00.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public class TuraelD extends Dialogue {
	public TuraelD(final Player player, final NPC npc) {
		super(player, npc);
	}

	private static final Item GEM = new Item(4155);

	@Override
	public void buildDialogue() {
		npc("'Ello, and what are you after then?");
		if (player.getSlayer().getCompletedTasks() == 0 && player.getSlayer().getAssignment() == null) {
			final Assignment task = player.getSlayer().generateTask(SlayerMaster.TURAEL);
			options(TITLE, "Who are you?", "Have you any rewards for me, or anything to trade?", "Let's talk about the" +
                    " difficulty of my assignments.", "Cancel my Task (150k GP)").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(20)).onOptionThree(() -> setKey(200)).onOptionFour(() -> setKey(999));
			player(10, "Who are you?");
			npc("I'm a Slayer Master. I train adventurers to learn the weaknesses of seemingly invulnerable monsters. " +
                    "To learn how, you need to kill specific monsters. I'll identify suitable targets and assign you " +
                    "a quota.");
			player("What's first?").executeAction(() -> player.getSlayer().setAssignment(task));
			npc("We'll start you off hunting " + task.getTask().toString() + ". You'll need to kill " + task.getAmount() + " of them.");
			npc("You'll also need this enchanted gem - it allows Slayer Masters like myself to contact you and update " +
                    "you on your progress. Don't worry if you lose it; you can buy another from any Slayer Master.").executeAction(() -> {
				if (player.getInventory().hasFreeSlots()) {
					player.getInventory().addItem(GEM);
				} else {
					World.spawnFloorItem(GEM, player);
				}
			});
			options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(100)).onOptionTwo(() -> finish());
			npc(100, task.getTask().getTip());
		} else {
			options(TITLE, "I need another assignment.", "Have you any rewards for me, or anything to trade?", "Let's " +
                    "talk about the difficulty of my assignments.", "Er... Nothing...").onOptionOne(() -> {
				if (player.getSlayer().getAssignment() == null) {
					final Assignment task = player.getSlayer().generateTask(SlayerMaster.TURAEL);
					player.getSlayer().setAssignment(task);
					player.getDialogueManager().finish();
					player.getDialogueManager().start(new Dialogue(player, npc) {
						@Override
						public void buildDialogue() {
							npc(40, "Your slayer task is to slay " + task.getTask().toString() + ". You'll need to " +
                                    "kill " + task.getAmount() + " of them. ");
							options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(100)).onOptionTwo(this::finish);
							npc(100, task.getTask().getTip());
						}
					});
				}
				setKey(40);
			}).onOptionTwo(() -> setKey(20)).onOptionThree(() -> setKey(200)).onOptionFour(() -> setKey(300));
			npc(40, "You're still on an assignment. You need to finish that one first.");
			npc("You need to kill " + player.getSlayer().getAssignment().getAmount() + " " + player.getSlayer().getAssignment().getTask().toString() + ".");
			for (final RegularTask data : RegularTask.VALUES) {
				if (player.getSlayer().getAssignment().getTask() == data) {
					if (data.getCertainTaskSet(SlayerMaster.TURAEL) == null || data.getCertainTaskSet(SlayerMaster.TURAEL).getMaximumAmount() < player.getSlayer().getAssignment().getAmount()) {
						npc(40, "You're still hunting " + player.getSlayer().getAssignment().getTask().toString() + ", you have " + player.getSlayer().getAssignment().getAmount() + " to go.");
						npc("Although it's not an assignment that I'd normally give... I guess I could  give you a new" +
                                " assignment, if you'd like.");
						npc("If you do get a new one, you will reset your standard task streak of " + player.getSlayer().getCurrentStreak() + ".");
						options(TITLE, "Yes please.", "No, thanks.").onOptionOne(() -> {
							final Assignment task = player.getSlayer().generateTask(SlayerMaster.TURAEL);
							player.getSlayer().setCurrentStreak(0);
							player.getSlayer().setAssignment(task);
							player.getDialogueManager().finish();
							player.getDialogueManager().start(new Dialogue(player, npc) {
								@Override
								public void buildDialogue() {
									npc("Your new assignment is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
								}
							});
						}).onOptionTwo(() -> finish());
					}
				}
			}
		}
		player(300, "Er... Nothing...");
		player(200, "Let's talk about the difficulty of my assignments.");
		if (player.getSlayer().isCheckingCombat()) {
			npc("The Slayer Masters will take your combat level into account when choosing tasks for you, so you " +
                    "shouldn't get anything too hard.");
			options(TITLE, "That's fine, I don't want anything too tough.", "Stop checking my combat level - I can take anything!").onOptionOne(() -> setKey(210)).onOptionTwo(() -> setKey(220));
			player(210, "That's fine, I don't want anything too tough.");
			npc("Okay, we'll keep checking your combat level.");
			player(220, "Stop checking my combat level - I can take anything!").executeAction(() -> player.getSlayer().setCheckingCombat(false));
			npc("Okay, from now on, all the Slayer Masters will assign you anything from their lists, regardless of your combat level.");
		} else {
			npc("The Slayer Masters may currently assign you any task in our lists, regardless of your combat level.");
			options(TITLE, "That's fine - I can handle any task.", "In future, please don't give anything too tough.").onOptionOne(() -> setKey(210)).onOptionTwo(() -> setKey(220));
			player(210, "That's fine - I can handle any task.");
			npc("That's the spirit.");
			player(220, "In future, please don't give anything too tough.").executeAction(() -> player.getSlayer().setCheckingCombat(true));
			npc("Okay, from now on, all the Slayer Masters will take your combat level into account when choosing " +
                    "tasks for you, so you shouldn't get anything too hard.");
		}
		player(20, "Have you any rewards for me, or anything to trade?");
		npc("I have quite a few rewards you can earn, and a wide variety of Slayer equipment for sale.");
		options(TITLE, "Look at rewards.", "Look at shop.", "Cancel.").onOptionOne(() -> {
			finish();
			player.getSlayer().openInterface();
		}).onOptionTwo(() -> player.openShop("Slayer Equipment")).onOptionThree(this::finish);
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
