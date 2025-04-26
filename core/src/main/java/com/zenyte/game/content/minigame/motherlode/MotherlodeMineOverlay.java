package com.zenyte.game.content.minigame.motherlode;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 02/07/2019 23:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MotherlodeMineOverlay extends Interface {
    @Override
    protected void attach() {

    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getVarManager().sendBit(5558, player.getPaydirt().size());
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.MOTHERLODE_MINE;
    }
}
