package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 16:07:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfCloseSub implements GamePacketEncoder {

    private final int hash;

    public IfCloseSub(int hash) {
        this.hash = hash;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Id: " + (hash >> 16) + ", child: " + (hash & 65535));
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.IF_CLOSESUB.gamePacketOut();
        buffer.writeInt(hash);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
