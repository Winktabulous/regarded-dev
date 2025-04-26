package com.zenyte.game.model.item.pluginextensions;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.ChargesManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;

import java.text.DecimalFormat;

/**
 * @author Kris | 25. aug 2018 : 17:05:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface ChargeExtension {
	DecimalFormat FORMATTER = ChargesManager.FORMATTER;

	void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount);

	default void checkCharges(final Player player, final Item item) {
		player.getChargesManager().notifyPlayerOfChargesLeft(item);
	}
}
