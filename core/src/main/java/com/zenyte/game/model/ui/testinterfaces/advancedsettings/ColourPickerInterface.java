package com.zenyte.game.model.ui.testinterfaces.advancedsettings;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Kris | 10/06/2022
 */
public class ColourPickerInterface extends Interface {
    @Override
    protected void attach() {

    }

    @Override
    protected void build() {

    }

    @Override
    public void close(Player player, Optional<GameInterface> replacement) {
        player.getPacketDispatcher().sendClientScript(101, 0);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.COLOUR_PICKER;
    }
}
