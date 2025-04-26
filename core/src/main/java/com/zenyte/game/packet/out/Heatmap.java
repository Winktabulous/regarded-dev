package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 04/03/2019 00:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Heatmap implements GamePacketEncoder {
    @Override
    public void log(@NotNull final Player player) {
        log(player, "Enabled: " + enabled);
    }

    private final boolean enabled;

    public Heatmap(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.HEAT_MAP.gamePacketOut();
        buffer.writeByte(enabled ? 1 : 0);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

}
