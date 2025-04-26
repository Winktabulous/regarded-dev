package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/03/2019 23:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class IfSetScrollPos implements GamePacketEncoder {
    private final int interfaceId;
    private final int componentId;
    private final int height;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", height: " + height);
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.IF_SETSCROLLPOS.gamePacketOut();
        buffer.writeShortLE(height);
        buffer.writeIntIME(interfaceId << 16 | componentId);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    public IfSetScrollPos(int interfaceId, int componentId, int height) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.height = height;
    }
}
