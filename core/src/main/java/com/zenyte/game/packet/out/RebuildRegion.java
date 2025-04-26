package com.zenyte.game.packet.out;

import com.near_reality.buffer.BitBuf;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.GamePacketEncoderMode;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicRegion;
import com.zenyte.game.world.region.Region;
import com.zenyte.game.world.region.XTEALoader;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 19:10:26
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class RebuildRegion implements GamePacketEncoder {

    private static final int BITBUFFER_LENGTH = (int) Math.ceil(13.0F * 13.0F * 4.0F * 27.0F / 8.0F);

    private final Player player;

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Tile: x: " + player.getX() + ", y: " + player.getY() + ", z: " + player.getPlane());
    }

    public RebuildRegion(Player player) {
        this.player = player;
    }

    @Override
    public GamePacketOut encode() {
        final int centerChunkX = player.getLocation().getChunkX();
        final int centerChunkY = player.getLocation().getChunkY();
        final boolean forceReloadMap = player.getForceReloadMap().getAndSet(false);

        final GamePacketOut buffer = ServerProt.REBUILD_REGION.gamePacketOut();
        buffer.writeShortLE(centerChunkY);
        buffer.writeByte(forceReloadMap ? 1 : 0);
        buffer.writeShort(centerChunkX);

        final ByteBuf xteasBuf = buffer.alloc().directBuffer();
        try {
            int regionCount = 0;
            try (final BitBuf bits = new BitBuf(buffer.alloc().directBuffer(255, BITBUFFER_LENGTH))) {
                for (int plane = 0; plane < 4; plane++) {
                    for (int chunkX = centerChunkX - 6; chunkX <= centerChunkX + 6; chunkX++) {
                        for (int chunkY = centerChunkY - 6; chunkY <= centerChunkY + 6; chunkY++) {
                            final int regionId = ((chunkX >> 3) << 8) + (chunkY >> 3);
                            final Region region = World.getRegions().get(regionId);
                            int displayedChunkX = chunkX;
                            int displayedChunkY = chunkY;
                            int displayedPlane = plane;
                            int rotation = 0;
                            if (DynamicRegion.isDynamicRegion(region)) {
                                final DynamicRegion dynamicRegion = (DynamicRegion) region;
                                final int hash = dynamicRegion.getLocationHash(chunkX - ((chunkX >> 3) << 3),
                                        chunkY - ((chunkY >> 3) << 3), plane);
                                displayedChunkX = hash & 2047;
                                displayedChunkY = hash >> 11 & 2047;
                                displayedPlane = hash >> 22 & 3;
                                rotation = hash >> 24 & 3;
                            }
                            if (displayedChunkX == 0 && displayedChunkY == 0) {
                                bits.writeBit(0);
                            } else {
                                bits.writeBit(1);
                                bits.writeBits(26, rotation << 1
                                        | displayedPlane << 24
                                        | displayedChunkX << 14
                                        | displayedChunkY << 3);

                                final int regionID = ((displayedChunkX >> 3) << 8) + (displayedChunkY >> 3);
                                for (int xtea : XTEALoader.getXTEAs(regionID)) {
                                    xteasBuf.writeInt(xtea);
                                }

                                regionCount++;
                            }
                        }
                    }
                }
                buffer.writeShort(regionCount);
                buffer.writeBits(bits);
            }
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
