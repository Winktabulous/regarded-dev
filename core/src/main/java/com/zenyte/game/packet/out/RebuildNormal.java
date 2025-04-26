package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.GamePacketEncoderMode;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.XTEALoader;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 13:48:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class RebuildNormal implements GamePacketEncoder {

    private final Player player;
    private final boolean init;

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Tile: x: " + player.getX() + ", y: " + player.getY() + ", z: " + player.getPlane());
    }

    public RebuildNormal(Player player, boolean init) {
        this.player = player;
        this.init = init;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.REBUILD_NORMAL.gamePacketOut();
        player.getForceReloadMap().set(false);
        if (init) {
            player.getPlayerViewport().init(buffer);
        }
        final int chunkX = player.getLocation().getChunkX();
        final int chunkY = player.getLocation().getChunkY();
        buffer.writeShortLE(chunkY);
        buffer.writeShortLE128(chunkX);
        final ByteBuf xteasBuf = buffer.alloc().buffer();
        try {
            int regionCount = 0;
            for (int xCalc = (chunkX - 6) / 8; xCalc <= (chunkX + 6) / 8; xCalc++) {
                for (int yCalc = (chunkY - 6) / 8; yCalc <= (chunkY + 6) / 8; yCalc++) {
                    final int regionID = yCalc + (xCalc << 8);
                    for (int xtea : XTEALoader.getXTEAs(regionID)) {
                        xteasBuf.writeInt(xtea);
                    }
                    regionCount++;
                }
            }
            buffer.writeShort(regionCount);
            buffer.writeBytes(xteasBuf);
        } finally {
            xteasBuf.release();
        }
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
