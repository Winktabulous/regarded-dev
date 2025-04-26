package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

@SuppressWarnings("unused")
public class JewelsPlugin extends ItemPlugin {

	@Override
	public void handle() {
		bind("Attach", (player, item, container, slotId) -> {
			for (JewelsOnKerisPartisanPlugin.JewelData data : JewelsOnKerisPartisanPlugin.JewelData.values) {
				if (data.getItem().getId() == item.getId()) {
					JewelsOnKerisPartisanPlugin.attach(player, data);
					return;
				}
			}
		});
		bind("Inspect", (player, item, container, slotId) -> {
			for (JewelsOnKerisPartisanPlugin.JewelData data : JewelsOnKerisPartisanPlugin.JewelData.values) {
				if (data.getItem().getId() == item.getId()) {
					player.getDialogueManager().item(item, data.getInspectMessage());
					return;
				}
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {ItemId.EYE_OF_THE_CORRUPTOR, ItemId.BREACH_OF_THE_SCARAB, ItemId.JEWEL_OF_THE_SUN};
	}

}
