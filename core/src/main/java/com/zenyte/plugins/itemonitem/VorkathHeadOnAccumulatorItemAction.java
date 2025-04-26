package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 9 sep. 2018 | 23:05:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VorkathHeadOnAccumulatorItemAction implements ItemOnItemAction {
	
	private static final Item AVAS_ASSEMBLER = new Item(22109);

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final Item device = from.getId() == 10499 ? from : to;
		final Item head = from.getId() == 10499 ? to : from;
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				doubleItem(head, device, "Would you like to combine the ava's device and Vorkath's head into an ava's assembler?");
				options(TITLE, "Yes.", "No.")
					.onOptionOne(() -> {
						setKey(5);
						player.getInventory().deleteItem(head);
						player.getInventory().deleteItem(device);
						player.getInventory().addItem(AVAS_ASSEMBLER);
					});
				item(5, AVAS_ASSEMBLER, "You have successfully combined the ava's device and Vorkath's head into an ava's assembler.");
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 21907, 10499 };
	}

}
