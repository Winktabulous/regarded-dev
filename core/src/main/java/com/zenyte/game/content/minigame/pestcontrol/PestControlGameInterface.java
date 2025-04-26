package com.zenyte.game.content.minigame.pestcontrol;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RegionArea;

/**
 * @author Kris | 13/12/2018 19:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PestControlGameInterface extends Interface {
    @Override
    protected void attach() {
        put(27, "Western shield");
        put(33, "South-Western shield");
        put(31, "South-Eastern shield");
        put(29, "Eastern shield");
        put(23, "Western health");
        put(26, "South-Western health");
        put(25, "South-Eastern health");
        put(24, "Eastern health");
        put(6, "Time remaining");
        put(7, "Void knight health");
        put(8, "Damage dealt");
    }

    @Override
    public void open(Player player) {
        final RegionArea area = player.getArea();
        if (!(area instanceof PestControlInstance)) {
            player.sendMessage("You cannot open the pest control overlay outside of pest control instances.");
            return;
        }
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PEST_CONTROL_GAME_OVERLAY;
    }
}
