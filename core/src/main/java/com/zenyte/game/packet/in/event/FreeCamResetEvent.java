package com.zenyte.game.packet.in.event;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FreeCamResetEvent implements ClientProtEvent {
    @Override
    public void log(@NotNull final Player player) {
        log(player, "");
    }

    @Override
    public void handle(Player player) {
        if (player.getTemporaryAttributes().get("tournament_spectating") != null) {
            player.getInterfaceHandler().closeInterfaces();
            return;
        }
        final PaneType pane = player.getInterfaceHandler().getPane();
        final Object loc = player.getTemporaryAttributes().get("oculusStart");
        player.getInterfaceHandler().sendPane(pane, pane);
        player.getPacketDispatcher().sendClientScript(2221, 1);
        GameInterface.EMOTE_TAB.open(player);
        GameInterface.MUSIC_TAB.open(player);
        player.getInterfaceHandler().openJournal();
        player.getInterfaceHandler().getVisible().remove(PaneType.ORB_OF_OCULUS.getId() << 16);
        if (!(loc instanceof Location) || !player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            return;
        }
        player.setLocation((Location) loc);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }
}
