package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:40:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ObjAdd implements GamePacketEncoder {
    private final FloorItem floorItem;

    @Override
    public void log(@NotNull final Player player) {
        final Location tile = floorItem.getLocation();
        this.log(player, "Item: " + floorItem.getId() + ", amount: " + floorItem.getAmount() + ", x: " + tile.getX() + ", y: " + tile.getY() + ", z: " + tile.getPlane());
    }

    public ObjAdd(FloorItem floorItem) {
        this.floorItem = floorItem;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.OBJ_ADD.gamePacketOut();
        final int targetLocalX = floorItem.getLocation().getX() - ((floorItem.getLocation().getX() >> 3) << 3);
        final int targetLocalY = floorItem.getLocation().getY() - ((floorItem.getLocation().getY() >> 3) << 3);
        final int offsetHash = (targetLocalX & 7) << 4 | (targetLocalY & 7);
        buffer.writeByte(offsetHash);
        buffer.writeByte128(31);//op flags
        buffer.writeByte(0);
        buffer.writeShort(0);
        buffer.writeShort128(floorItem.getId());
        buffer.writeInt(floorItem.getAmount());
        buffer.writeByte(0);
        buffer.writeShort(0);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
