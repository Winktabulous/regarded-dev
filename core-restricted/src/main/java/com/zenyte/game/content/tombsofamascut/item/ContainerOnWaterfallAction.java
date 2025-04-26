package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class ContainerOnWaterfallAction implements ItemOnObjectAction {

	@Override public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		if (player.getArea() instanceof CrondisPuzzleEncounter crondisPuzzleEncounter) {
			crondisPuzzleEncounter.handleWaterfall(player, object);
		}
	}

	@Override public Object[] getItems() {
		return new Object[] {CrondisPuzzleEncounter.CONTAINER_ITEM_ID};
	}

	@Override public Object[] getObjects() {
		return new Object[] {CrondisPuzzleEncounter.WATERFALL_OBJECT_ID, CrondisPuzzleEncounter.WATERFALL_OBJECT_ID + 1};
	}
}
