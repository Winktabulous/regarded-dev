package com.zenyte.game.content.minigame.puropuro;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Corey
 * @since 29/01/2020
 */
public class PuroPuroImplingOverlay extends Interface {
    
    public static final String PURO_PURO_OVERLAY_ATTRIBUTE = "puro_puro_overlay";
    
    @Override
    protected void attach() {
    
    }
    
    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }
    
    @Override
    protected void build() {
    
    }
    
    @Override
    public GameInterface getInterface() {
        return GameInterface.PURO_PURO_IMPLING_OVERLAY;
    }
}
