package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 31 mrt. 2018 : 22:14:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class IfSetColour implements GamePacketEncoder {
    private final int interfaceId;
    private final int componentId;
    private final RSColour colour;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", 15-bit RGB: " + colour.getRGB());
    }

    public IfSetColour(int interfaceId, int componentId, RSColour colour) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.colour = colour;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.IF_SETCOLOR.gamePacketOut();
        buffer.writeIntIME(interfaceId << 16 | componentId);
        buffer.writeShortLE(colour.getRGB());
        return buffer;
    }

}
