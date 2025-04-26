package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClickWorldMapEvent implements ClientProtEvent {
    private final int x;
    private final int y;
    private final int z;

    @Override
    public void handle(Player player) {
        if (!player.eligibleForShiftTeleportation()) {
            return;
        }
        final Location location = new Location(x, y, z);
        player.setLocation(location);
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "X: " + x + ", Y: " + y + ", Z: " + z);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public ClickWorldMapEvent(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
