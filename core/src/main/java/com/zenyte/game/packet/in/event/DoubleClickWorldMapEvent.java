package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DoubleClickWorldMapEvent implements ClientProtEvent {
    private final int compressed;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Tile hash: " + compressed);
    }

    @Override
    public void handle(Player player) {
        if (!player.eligibleForShiftTeleportation()) {
            return;
        }
        final Location location = new Location(compressed);
        player.setLocation(location);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public DoubleClickWorldMapEvent(int compressed) {
        this.compressed = compressed;
    }
}
