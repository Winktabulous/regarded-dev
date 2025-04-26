package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.skills.slayer.dialogue.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;

public class SumonaNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Rewards", (player, npc) -> {
			player.getSlayer().openInterface();
			Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
		});
		bind("Talk-to", (Player player, NPC npc) -> {
			player.getDialogueManager().start(new SumonaD(player, npc));
			Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
		});
		bind("Assignment", (player, npc) -> {
			player.getDialogueManager().start(new SumonaAssignmentD(player, npc));
			Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[]{16064};
	}

}
