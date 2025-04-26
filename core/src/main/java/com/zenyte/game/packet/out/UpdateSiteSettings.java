package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/03/2019 23:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class UpdateSiteSettings implements GamePacketEncoder {

    private final String settings;

    public UpdateSiteSettings(String settings) {
        this.settings = settings;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Settings: " + settings);
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.UPDATE_SITESETTINGS.gamePacketOut();
        buffer.writeString(settings);
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
