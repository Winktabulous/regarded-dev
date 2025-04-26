package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

public interface IDropPlugin {

    boolean drop(final Player player, final Item item);
    boolean dropOnGround(final Player player, final Item item);

    int visibleTicks(final Player player, final Item item);
    int invisibleTicks(final Player player, final Item item);
}
