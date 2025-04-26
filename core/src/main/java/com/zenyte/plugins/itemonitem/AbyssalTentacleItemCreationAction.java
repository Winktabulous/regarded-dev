package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 24 apr. 2018 | 00:00:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class AbyssalTentacleItemCreationAction implements ItemOnItemAction {

	private static final Item ABYSSAL_WHIP = new Item(4151);
	public static final Item KRAKEN_TENTACLE = new Item(12004);
	public static final Item ABYSSAL_TENTACLE = new Item(12006, 1, 10_000);

	//TODO animation
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain("<col=FF0040>Warning!</col><br>The tentacle will gradually consume your whip and destroy it. You won't be able to get the whip out again. The combined item is not tradeable.");
				options("Are you sure you wish to do this?", "Yes, let the tentacle consume the whip.", "No, I'll keep my whip.")
				.onOptionOne(() -> {
					player.getInventory().deleteItem(ABYSSAL_WHIP);
					player.getInventory().deleteItem(KRAKEN_TENTACLE);
					player.getInventory().addItem(ABYSSAL_TENTACLE);
				});
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 4151, 12004 };
	}

}
