package com.zenyte.game.world.entity.player.var;

import com.zenyte.game.world.entity.player.Player;

@FunctionalInterface
public interface VarMultivalueFunction extends VarFunction {

    default int getValue(final Player player) {
        return 0;
    }

    @Override
    int getValue(final Player player, final int idx);
}
