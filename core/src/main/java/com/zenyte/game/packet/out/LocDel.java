package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:29:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class LocDel implements GamePacketEncoder {
    private final WorldObject object;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Id: " + object.getId() + ", type: " + object.getType() + ", rotation: " + object.getRotation() + ", x: " + object.getX() + ", y: " + object.getY() + ", z: " + object.getPlane());
    }

    public LocDel(WorldObject object) {
        this.object = object;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.LOC_DEL.gamePacketOut();
        final int targetLocalX = object.getX() - ((object.getX() >> 3) << 3);
        final int targetLocalY = object.getY() - ((object.getY() >> 3) << 3);
        final int offsetHash = (targetLocalX & 7) << 4 | (targetLocalY & 7);
        buffer.writeByte(offsetHash);
        buffer.write128Byte((object.getType() << 2) + (object.getRotation() & 3));
        return buffer;
    }
}
