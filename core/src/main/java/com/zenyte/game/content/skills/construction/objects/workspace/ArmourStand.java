package com.zenyte.game.content.skills.construction.objects.workspace;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.model.item.degradableitems.RepairableItem;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.RepairItemD;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom | 24. veebr 2018 : 21:46.58
 */
public final class ArmourStand implements ItemOnObjectAction {
	
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		player.getDialogueManager().start(new RepairItemD(player, item, true));
	}

	@Override
	public Object[] getItems() {
		final List<Object> list = new ArrayList<Object>();
		for (RepairableItem item : RepairableItem.VALUES) {
			for (int i = 0; i < item.getIds().length; i++)
				list.add(item.getIds()[i]);
		}
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Armour stand", "Armour repair stand" };
	}

}
