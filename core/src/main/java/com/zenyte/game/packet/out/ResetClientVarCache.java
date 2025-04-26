package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/03/2019 23:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ResetClientVarCache implements GamePacketEncoder {

    @Override
    public void log(@NotNull final Player player) {
        log(player, "");
    }

    @Override
    public GamePacketOut encode() {
        return ServerProt.RESET_CLIENT_VARCACHE.gamePacketOut();
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
