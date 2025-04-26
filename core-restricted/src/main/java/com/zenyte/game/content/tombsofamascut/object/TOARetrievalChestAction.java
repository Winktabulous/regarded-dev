package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Savions.
 */
public class TOARetrievalChestAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.TOMBS_OF_AMASCUT || player.getRetrievalService().getContainer().isEmpty()) {
			player.getDialogueManager().start(new PlainChat(player, "There is nothing to collect."));
			return;
		}
		GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
	}

	@Override public Object[] getObjects() {
		return new Object[] {46078};
	}
}
