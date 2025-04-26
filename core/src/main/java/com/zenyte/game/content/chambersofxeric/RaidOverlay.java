package com.zenyte.game.content.chambersofxeric;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 07/07/2019 03:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidOverlay extends Interface {
    @Override
    protected void attach() {
        put(2, "Overlay component");
    }

    static final void setVisibility(@NotNull final Player player, final boolean hidden) {
        GameInterface.RAID_OVERLAY.getPlugin().ifPresent(plugin -> player.getPacketDispatcher().sendComponentVisibility(plugin.getInterface(), plugin.getComponent("Overlay component"), hidden));
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAID_OVERLAY;
    }
}
