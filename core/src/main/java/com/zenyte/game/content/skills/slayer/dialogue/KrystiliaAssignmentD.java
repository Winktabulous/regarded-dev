package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 6. nov 2017 : 19:23.10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public class KrystiliaAssignmentD extends Dialogue {

	public KrystiliaAssignmentD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		if (player.getSlayer().getAssignment() != null) {
			npc("You're already on an assignment.");
			npc("Your assignment is to slay " + player.getSlayer().getAssignment().getAmount() + " "
					+ player.getSlayer().getAssignment().getTask().toString() + ".");
			return;
		} else if (player.getSlayer().getKrystiliaStreak() > 0) {
			player.getDialogueManager().start(new SlayerMasterAssignmentD(player, npc));
			return;
		}
		npc("Before I assign you anything, I want to make something clear." + " My tasks have to be done in the Wilderness. Only kills "
				+ "inside the Wilderness will count.");
		npc("I don't check combat levels when choosing tasks, either. "
				+ "So even if other masters won't assign you 'tough' monsters, I'll pick anything "
				+ "for you which you have the Slayer level, provided you can physically get to it.");
		npc("If you don't like my tasks, go and see Turael in Burthorpe, and ask him to assign you something else. He won't argue.");
		npc("You might get a few unusual loot drops while you're on my assignments, if you're lucky. "
				+ "So, do you want me to assign you anything?");
		options(TITLE, "Yes, I understand I must kill it in the Wilderness.", "No thanks, I don't want tasks from you.")
				.onOptionOne(() -> setKey(20)).onOptionTwo(() -> setKey(30));
		player(20, "Yes, I understand I must kill it in the Wilderness.").executeAction(() -> {
			finish();
			player.getDialogueManager().start(new SlayerMasterAssignmentD(player, npc));
		});
		player(30, "No thanks, I don't want tasks from you.");
	}

}
