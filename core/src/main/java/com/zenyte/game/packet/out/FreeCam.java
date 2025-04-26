package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 26. veebr 2018 : 2:06.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FreeCam implements GamePacketEncoder {

    private final boolean freeRoam;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Freeroaming: " + freeRoam);
    }

    public FreeCam(boolean freeRoam) {
        this.freeRoam = freeRoam;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.ENTER_FREECAM.gamePacketOut();
        buffer.writeByte(freeRoam ? 1 : 0);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

}
