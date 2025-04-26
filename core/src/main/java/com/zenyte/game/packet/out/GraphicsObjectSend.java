package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.GraphicsObjectRS;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;

public class GraphicsObjectSend implements GamePacketEncoder {
    private final GraphicsObjectRS gfx;
    private final Player player;

    public GraphicsObjectSend(Player player, GraphicsObjectRS object) {
        this.gfx = object;
        this.player = player;
    }
    private static int getLocal(int abs, int chunk) {
        return abs - 8 * (chunk - 6);
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.GRAPHICSOBJECT_SPAWN.gamePacketOut();
        final Location lastTile = player.getLastLoadedMapRegionTile();
        final Location spawnLoc = gfx.getLocation();
        final int chunkX = player.getLastLoadedMapRegionTile().getChunkX();
        final int chunkY = player.getLastLoadedMapRegionTile().getChunkY();
        final int srcHash = chunkX << 16 | chunkY << 8 & 255 | spawnLoc.getLocalHash(lastTile);
        buffer.writeUnsignedShort(gfx.getDelay());
        buffer.writeByte(0);
        buffer.writeUnsignedShortAdd(gfx.getId());
        buffer.write3UShort(srcHash);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
