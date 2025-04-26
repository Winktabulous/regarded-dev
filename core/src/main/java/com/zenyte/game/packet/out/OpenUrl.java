package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 2-12-2018 | 17:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class OpenUrl implements GamePacketEncoder {

    private final String url;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "URL: " + url);
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.OPEN_URL.gamePacketOut(true);
        buffer.writeString(url);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public OpenUrl(String url) {
        this.url = url;
    }

}
