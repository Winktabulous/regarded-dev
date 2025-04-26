package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.SpinningData;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.skills.SpinningD;

import java.util.ArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:48.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public final class SpinningObjectAction implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		player.getDialogueManager().start(new SpinningD(player, object));
	}

	@Override
	public Object[] getItems() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (final CraftingDefinitions.SpinningData data : SpinningData.VALUES_ARR) {
			for (final Item material : data.getMaterials()) {
				list.add(material.getId());
			}
		}
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Spinning wheel"};
	}
}
