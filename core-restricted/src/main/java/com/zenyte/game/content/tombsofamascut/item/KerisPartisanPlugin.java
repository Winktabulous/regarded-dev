package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import mgi.types.config.items.ItemDefinitions;

@SuppressWarnings("unused")
public class KerisPartisanPlugin extends ItemPlugin {

	@Override
	public void handle() {
		bind("Dismantle", (player, item, container, slotId) -> {
			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("You need some free inventory space to dismantle this item.");
				return;
			}

			for (JewelsOnKerisPartisanPlugin.JewelData data : JewelsOnKerisPartisanPlugin.JewelData.values) {
				if (data.getAttached().getId() == item.getId()) {
					player.getInventory().deleteItem(slotId, item);
					player.getInventory().addItem(data.getItem());
					player.getInventory().addItem(JewelsOnKerisPartisanPlugin.KERIS_PARTISAN);
					player.sendMessage("You dismantle the " + ItemDefinitions.nameOf(data.getItem().getId()).toLowerCase() + " from the " + ItemDefinitions.nameOf(JewelsOnKerisPartisanPlugin.KERIS_PARTISAN.getId()).toLowerCase() + ".");
					return;
				}
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.KERIS_PARTISAN_OF_BREACHING, ItemId.KERIS_PARTISAN_OF_CORRUPTION, ItemId.KERIS_PARTISAN_OF_THE_SUN};
	}

}
