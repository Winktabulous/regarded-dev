package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class StrangeOldMan extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> {
			if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.ROTS || player.getRetrievalService().getContainer().isEmpty()) {
				player.getDialogueManager().start(new Dialogue(player, npc) {

					@Override
					public void buildDialogue() {
						npc("There's nothing to collect at this time.");
					}
				});
				return;
			}
			GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[]{NpcId.STRANGE_OLD_MAN};
	}

}


