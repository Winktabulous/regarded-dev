package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.info.WorldType;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/03/2019 23:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LogoutTransfer implements GamePacketEncoder {

    private final WorldType world;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Address: " + world.getAddress() + ", id: " + world.getWorldId() + ", flag: " + world.getFlag());
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.LOGOUT_TRANSFER.gamePacketOut();
        buffer.writeString(world.getAddress());
        buffer.writeShort(world.getWorldId());
        buffer.writeInt(world.getFlag());
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public LogoutTransfer(WorldType world) {
        this.world = world;
    }
}
