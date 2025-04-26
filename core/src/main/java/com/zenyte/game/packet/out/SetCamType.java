package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/03/2019 23:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class SetCamType implements GamePacketEncoder {

    private final int type;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Type: " + type);
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.TOGGLE_OCULUS_ORB.gamePacketOut();
        buffer.writeInt(type);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public SetCamType(int type) {
        this.type = type;
    }
}
