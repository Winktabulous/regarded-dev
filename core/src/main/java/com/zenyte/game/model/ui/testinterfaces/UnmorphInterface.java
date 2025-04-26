package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Corey
 * @since 17/11/18
 */
public class UnmorphInterface extends Interface {
    
    @Override
    protected void attach() {
        put(0, "Unmorph");
    }
    
    @Override
    public void open(Player player) {
    
    }
    
    @Override
    protected void build() {
        bind("Unmorph", this::close);
    }
    
    @Override
    public GameInterface getInterface() {
        return GameInterface.UNMORPH_TAB;
    }
    
    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (player.getAppearance().isTransformedIntoNpc()) {
            player.getAppearance().transform(-1);
            player.getAppearance().resetRenderAnimation();
        }
    }
}
