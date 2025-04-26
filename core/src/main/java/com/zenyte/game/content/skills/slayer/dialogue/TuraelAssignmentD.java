package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.content.skills.slayer.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 7. nov 2017 : 1:05.01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class TuraelAssignmentD extends Dialogue {
	public TuraelAssignmentD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		if (player.getSlayer().getAssignment() != null) {
			npc("You're still on an assignment. You need to finish that one first.");
			npc("You need to kill " + player.getSlayer().getAssignment().getAmount() + " " + player.getSlayer().getAssignment().getTask().toString() + ".");
			final RegularTask data = player.getSlayer().getAssignment().getTask() instanceof BossTask ? null : ((RegularTask) player.getSlayer().getAssignment().getTask());
			if (data == null || (player.getSlayer().getMaster() == SlayerMaster.KRYSTILIA || data.getCertainTaskSet(SlayerMaster.TURAEL) == null || data.getCertainTaskSet(SlayerMaster.TURAEL).getMaximumAmount() < player.getSlayer().getAssignment().getAmount())) {
				npc("You're still hunting " + player.getSlayer().getAssignment().getTask().toString() + ", you have " + player.getSlayer().getAssignment().getAmount() + " to go.");
				npc("Although it's not an assignment that I'd normally give... I guess I could  give you a new " +
                        "assignment, if you'd like.");
				npc("If you do get a new one, you will reset your standard task streak of " + player.getSlayer().getCurrentStreak() + ".");
				options(TITLE, "Yes please.", "No, thanks.").onOptionOne(() -> {
					final Assignment task = player.getSlayer().generateTask(SlayerMaster.TURAEL);
					player.getSlayer().setCurrentStreak(0);
					player.getSlayer().setAssignment(task);
					player.getDialogueManager().finish();
					player.getDialogueManager().start(new Dialogue(player, npc) {
						@Override
						public void buildDialogue() {
							npc("Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
						}
					});
					setKey(150);
				}).onOptionTwo(this::finish);
			}
			return;
		}
		final Slayer slayer = player.getSlayer();
		final Assignment task = slayer.generateTask(SlayerMaster.TURAEL);
		if (slayer.getMaster() != SlayerMaster.TURAEL) {
			slayer.setMaster(SlayerMaster.TURAEL);
		}
		player.getSlayer().setAssignment(task);
		npc("Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
		options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(100));
		npc(100, task.getTask().getTip());
	}
}
