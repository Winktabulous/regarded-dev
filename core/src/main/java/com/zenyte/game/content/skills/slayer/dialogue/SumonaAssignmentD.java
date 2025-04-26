package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.content.skills.slayer.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;

public final class SumonaAssignmentD extends Dialogue {

	private static final int COST = 300_000;

	public SumonaAssignmentD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final SlayerMaster master = SlayerMaster.mappedMasters.get(npc.getId());
		if (master == null) {
			return;
		}

		final Slayer slayer = player.getSlayer();
		final Assignment currentTask = slayer.getAssignment();
		if (currentTask != null) {
			final Class<? extends RegionArea> clazz = currentTask.getArea();
			if (master.equals(SlayerMaster.KONAR_QUO_MATEN) && clazz != null) {
				final RegionArea area = GlobalAreaManager.getArea(clazz);
				npc("You're still bringing balance to " + currentTask.getTask().toString().toLowerCase() + " in the " + area.name() + ", with " + currentTask.getAmount() + " to go. Come back when you're finished.");
			} else {
				npc("You're still hunting " + currentTask.getTask().toString().toLowerCase() + "; you have " + currentTask.getAmount() + " to go. Come back when you've finished your task.");
			}
			return;
		}

		final Skills skills = player.getSkills();
		if (skills.getLevel(SkillConstants.SLAYER) < SlayerMaster.SUMONA.getSlayerRequirement() || skills.getCombatLevel() < SlayerMaster.SUMONA.getCombatRequirement()) {
			npc("Sorry, but you're not skilled enough to be taught by<br><br>me. Your best trainer would be " + player.getSlayer().getAdvisedMaster() + ".");
			return;
		}

		if (player.getInventory().getAmountOf(ItemId.COINS_995) < COST) {
			npc("Sorry, but for me to assign you a boss task will cost you 300,000 coins.");
			return;
		}

		player.getInventory().deleteItem(new Item(ItemId.COINS_995, COST));
		final Assignment task = slayer.generateSummonaTask();
		if (slayer.getMaster() != SlayerMaster.SUMONA) {
			slayer.setMaster(SlayerMaster.SUMONA);
		}
		player.getSlayer().setAssignment(task);
		npc("Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
		options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(100));
		npc(100, task.getTask().getTip());
	}
}
