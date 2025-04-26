package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:24:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfSetObject implements GamePacketEncoder {
    private final int interfaceId;
    private final int componentId;
    private final int itemId;
    private final int zoom;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", item: " + itemId + ", zoom: " + zoom);
    }

    public IfSetObject(int interfaceId, int componentId, int itemId, int zoom) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.itemId = itemId;
        this.zoom = zoom;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.IF_SETOBJECT.gamePacketOut();
        buffer.writeShortLE128(itemId);
        buffer.writeIntIME(zoom);
        buffer.writeIntIME(interfaceId << 16 | componentId);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
