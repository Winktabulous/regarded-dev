package com.zenyte.game.content.wildernessVault;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

public class VaultOverlayInterface extends Interface {

    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendClientScript(5313, 255);
    }


    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WILDERNESS_VAULT_HUD;
    }

}
