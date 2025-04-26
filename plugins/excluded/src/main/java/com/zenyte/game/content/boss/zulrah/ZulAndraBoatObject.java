package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 22. march 2018 : 1:18.04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public final class ZulAndraBoatObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final ItemRetrievalService retrievalService = player.getRetrievalService();
		if (retrievalService.getType() == ItemRetrievalService.RetrievalServiceType.ZUL_GWENWYNIG && !retrievalService.getContainer().isEmpty()) {
			player.getDialogueManager().start(new NPCChat(player, NpcId.PRIESTESS_ZULGWENWYNIG, "I've got some stuff you left at the shrine earlier. You should get it back from me before sacrificing yourself again."));
			return;
		}
		if (option.equalsIgnoreCase("Board")) {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					options("Return to Zulrah's shrine?", "Yes.", "No.").onOptionOne(() -> {
						finish();
						boardBoat(player);
					});
				}
			});
		} else {
			boardBoat(player);
		}
	}

	private static void boardBoat(Player player) {
		ZulrahInstance.launch(player);
		player.getDialogueManager().start(new PlainChat(player, "The priestess rows you to Zulrah's shrine,<br>then hurriedly paddles away.", false));
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {10068};
	}
}
