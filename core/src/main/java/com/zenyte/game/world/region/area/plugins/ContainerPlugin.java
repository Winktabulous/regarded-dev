package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;

/**
 * @author Kris | 27. aug 2018 : 02:17:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface ContainerPlugin {

	void onContainerModification(final Player player, final Container container, final Item previousItem, final Item currentItem);
	
}
