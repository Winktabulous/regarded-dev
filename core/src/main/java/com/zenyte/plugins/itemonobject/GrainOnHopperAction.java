package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class GrainOnHopperAction implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		if(player.getBooleanAttribute("hopper"))
			player.sendMessage("There is already grain in the hopper.");
		else {
			player.getInventory().deleteItem(item);
			player.setAnimation(new Animation(3572));
			player.sendMessage("You put the grain in the hopper.");
			player.toggleBooleanAttribute("hopper");
		}
	}

	@Override
	public Object[] getItems() {
		return new Object[] { "grain" };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "hopper" };
	}

}
