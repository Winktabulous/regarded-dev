package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.GamePacketEncoderMode;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 1. apr 2018 : 22:02.56
 * @author Jire
 */
public final class PingStatisticsRequest implements GamePacketEncoder {

    @Override
    public void log(@NotNull final Player player) {
        log(player, "");
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.SEND_PING.gamePacketOut();
        final long time = System.nanoTime();
        buffer.writeLong(time);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    @NotNull
    @Override
    public GamePacketEncoderMode encoderMode() {
        return GamePacketEncoderMode.WRITE_FLUSH;
    }

}
