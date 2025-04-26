package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Glabay | Glabay-Studios
 * @project Near-Reality
 * @social Discord: Glabay
 * @since 2024-08-27
 */
public class DragonHunterItemCombinationAction implements ItemOnItemAction {
	public static Item DHCB = new Item(ItemId.DRAGON_HUNTER_CROSSBOW, 1);
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		var kbdHead = from.getId() == ItemId.KBD_HEADS;
		if (!kbdHead)
			kbdHead = to.getId() == ItemId.KBD_HEADS;

		final var message = String.format(
			"<col=FF0040>Warning!</col><br>You're about to add the %s to your crossbow. The combined item cannot be traded.",
			(kbdHead ? "King Black Dragon heads" : "Vorkath head"));

		final var head = kbdHead ? ItemId.KBD_HEADS : ItemId.VORKATHS_HEAD;
		final var crossbow = kbdHead ? ItemId.DRAGON_HUNTER_CROSSBOW_B : ItemId.DRAGON_HUNTER_CROSSBOW_T;

		player.getDialogueManager().start(new Dialogue(player) {

			@Override
			public void buildDialogue() {
				plain(message);
				options("Are you sure you wish to do this?", "Yes, combine the head.", "No, I'll keep my head.")
				.onOptionOne(() -> {
					if(player.getInventory().deleteItems(DHCB, new Item(head, 1)).getResult() == RequestResult.SUCCESS) {
						player.getInventory().addItem(crossbow, 1);
					}
				});
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { ItemId.KBD_HEADS, ItemId.VORKATHS_HEAD, ItemId.DRAGON_HUNTER_CROSSBOW };
	}

}
