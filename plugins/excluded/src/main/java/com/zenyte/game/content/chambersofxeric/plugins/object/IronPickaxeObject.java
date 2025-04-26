package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class IronPickaxeObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		player.getRaid().ifPresent(raid -> {
			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("You need some free inventory space to take the tools.");
				return;
			}
			Item item = new Item(ItemId.IRON_PICKAXE);
			if (player.getInventory().containsItem(item)) {
				player.sendMessage("You have nothing more to take from here.");
				return;
			}
			player.getInventory().addOrDrop(item);
		});
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { ObjectId.IRON_PICKAXE };
	}
}

