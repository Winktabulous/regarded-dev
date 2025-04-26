package com.zenyte.game.packet.out;

import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:01:53
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfOpenSub implements GamePacketEncoder {

    private final int interfaceId;
    private final int paneChildId;
    private final PaneType pane;
    private final boolean overlay;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", pane: " + pane.getId() + ", name: " + pane.name() + ", child: " + paneChildId + ", overlay: " + overlay);
    }

    public IfOpenSub(int interfaceId, int paneChildId, PaneType pane, boolean overlay) {
        this.interfaceId = interfaceId;
        this.paneChildId = paneChildId;
        this.pane = pane;
        this.overlay = overlay;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.IF_OPENSUB.gamePacketOut();
        buffer.writeShortLE(interfaceId);
        buffer.writeByte128(overlay ? 1 : 0);
        buffer.writeIntIME(pane.getId() << 16 | paneChildId);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
