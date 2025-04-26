package com.zenyte.plugins.item;

import com.zenyte.game.content.multicannon.DwarfMultiCannonType;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 25. aug 2018 : 22:18:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public class CannonBase extends ItemPlugin {

	@Override
	public void handle() {
		bind("Set-up", (player, item, slotId) -> player.getDwarfMulticannon().setupCannon(DwarfMultiCannonType.list(item)));
		bind("Dismantle", Dismantleable::dismantle);
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.CANNON_BASE, 26520};
	}

}
