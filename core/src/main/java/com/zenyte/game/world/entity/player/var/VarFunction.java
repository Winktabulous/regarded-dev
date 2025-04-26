package com.zenyte.game.world.entity.player.var;

import com.zenyte.game.world.entity.player.Player;

public interface VarFunction {
    int getValue(final Player player);
    int getValue(final Player player, final int idx);
}
