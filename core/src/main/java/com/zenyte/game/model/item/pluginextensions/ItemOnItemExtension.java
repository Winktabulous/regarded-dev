package com.zenyte.game.model.item.pluginextensions;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

public interface ItemOnItemExtension {

    void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot);

}
