package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Corey
 * @since 10:03 - 22/07/2019
 */
public class WintertodtOverlay extends Interface {
    
    @Override
    protected void attach() {
    
    }
    
    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        Wintertodt.refreshOverlay(player, true);
    }
    
    @Override
    protected void build() {
    
    }
    
    @Override
    public GameInterface getInterface() {
        return GameInterface.WINTERTODT;
    }
    
}
