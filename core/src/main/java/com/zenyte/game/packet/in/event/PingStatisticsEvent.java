package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 21:55
 * @author Jire
 */
public final class PingStatisticsEvent implements ClientProtEvent {

    private final int gc;
    private final int fps;
    private final long ns;

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "GC: " + gc + ", FPS: " + fps + ", MS: " + ns);
    }

    @Override
    public void handle(Player player) {
        long ms = TimeUnit.NANOSECONDS.toMillis(ns);
        long estimatedMS = TimeUnit.NANOSECONDS.toMillis(Math.max(0, ns - (1_000_000_000 / Math.max(1, fps))));
        player.sendMessage("Ping (estimated: " + estimatedMS + "ms, reported: " + ms + "ms)");
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    public PingStatisticsEvent(int gc, int fps, long ns) {
        this.gc = gc;
        this.fps = fps;
        this.ns = ns;
    }

}
