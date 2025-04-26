package com.zenyte.game.content.pyramidplunder;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class PyramidPlunderOverlay extends Interface {
    @Override
    public void attach() {
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    public void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PYRAMID_PLUNDER;
    }
}
