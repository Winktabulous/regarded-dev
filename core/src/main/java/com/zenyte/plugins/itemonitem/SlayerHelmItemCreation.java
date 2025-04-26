package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Tommeh | 19 mei 2018 | 16:04:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SlayerHelmItemCreation implements ItemOnItemAction {
	public static final Item[] REQUIRED_ITEMS = {new Item(4166), new Item(4164), new Item(4168), new Item(4155), new Item(4551)};
	public static final Item SLAYER_HELM = new Item(11864);

	private static final int[] BLACK_MASKS = new int[] {ItemId.BLACK_MASK, ItemId.BLACK_MASK_1, ItemId.BLACK_MASK_2, ItemId.BLACK_MASK_3, ItemId.BLACK_MASK_4, ItemId.BLACK_MASK_5, ItemId.BLACK_MASK_6, ItemId.BLACK_MASK_7, ItemId.BLACK_MASK_8, ItemId.BLACK_MASK_9, ItemId.BLACK_MASK_10};
	private static final int[] BLACK_MASKS_I = new int[] {ItemId.BLACK_MASK_I, ItemId.BLACK_MASK_1_I, ItemId.BLACK_MASK_2_I, ItemId.BLACK_MASK_3_I, ItemId.BLACK_MASK_4_I, ItemId.BLACK_MASK_5_I, ItemId.BLACK_MASK_6_I, ItemId.BLACK_MASK_7_I, ItemId.BLACK_MASK_8_I, ItemId.BLACK_MASK_9_I, ItemId.BLACK_MASK_10_I, };

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		boolean hasBlackMask = player.getInventory().containsAnyOf(BLACK_MASKS)
				|| player.getInventory().containsAnyOf(BLACK_MASKS_I);
		boolean usedImbued = from.getName().contains("Black mask") && from.getName().contains("(i)") || to.getName().contains("Black mask") && to.getName().contains("(i)");
		boolean imbued = hasBlackMask && usedImbued;

		int maskId = -1;

		if(imbued)
			for(int i = 11784; i >= 11744; i--) {
				if (from.getId() == i || to.getId() == i || player.getInventory().containsItem(i, 1)) {
					maskId = i;
					break;
				}
			}
		else
			for (int i = 8921; i >= 8901; i--) {
				if (from.getId() == i || to.getId() == i || player.getInventory().containsItem(i, 1)) {
					maskId = i;
					break;
				}
			}


		if (player.getSkills().getLevel(SkillConstants.CRAFTING) < 55) {
			player.sendMessage("You need a Crafting level of at least 55 to assemble a slayer helmet.");
			return;
		}

		if (!player.getSlayer().isUnlocked("Malevolent masquerade")) {
			player.sendMessage("You must unlock the slayer reward Malevolent masquerade in order to assemble a slayer helmet");
			return;
		}

		final StringBuilder builder = new StringBuilder();
		for (final Item item : REQUIRED_ITEMS) {
			if (!player.getInventory().containsItem(item)) {
				builder.append(item.getName().toLowerCase()).append(", ");
			}
		}
		if(!hasBlackMask || maskId == -1) {
			builder.append("black mask").append(", ");
		}

		if (builder.length() > 0) {
			final String message = "You are still missing the following items in order to assemble a slayer helmet;<br>" + builder.toString().replaceAll(", $", ".");
			final StringBuilder bldr = new StringBuilder(message);
			final int index = message.lastIndexOf(", ");
			if (index >= 0) {
				bldr.replace(message.lastIndexOf(", "), message.lastIndexOf(", ") + 1, " and");
			}
			player.sendMessage(bldr.toString());
			return;
		}

		for (final Item item : REQUIRED_ITEMS) {
			player.getInventory().deleteItem(item);
		}
		player.getInventory().deleteItem(maskId, 1);
		player.getInventory().addItem(imbued ? new Item(11865, 1) : SLAYER_HELM);
		player.sendMessage("You successfully assembled a slayer helmet.");
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final Item item : REQUIRED_ITEMS) {
			list.add(item.getId());
		}
		for (int blackMask : BLACK_MASKS) {
			list.add(blackMask);
		}
		for (int i : BLACK_MASKS_I) {
			list.add(i);
		}
		return list.toArray(new int[0]);
	}
}
