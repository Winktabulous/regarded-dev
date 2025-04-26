package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.pathfinding.events.player.FloorItemEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.FloorItemStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.plugins.Plugin;

/**
 * @author Kris | 11. mai 2018 : 00:45:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface ItemOnFloorItemAction extends Plugin {

	void handleItemOnFloorItemAction(final Player player, final Item item, final FloorItem floorItem);
	
	Object[] getItems();
	
	Object[] getFloorItems();
	
	default void handle(final Player player, final Item item, final FloorItem floorItem) {
		player.setRouteEvent(new FloorItemEvent(player, new FloorItemStrategy(floorItem), () -> {	
			player.stopAll();
			if (!player.getInventory().containsItem(item)) {
				return;
			}
			handleItemOnFloorItemAction(player, item, floorItem);
		}));
	}
	
}
