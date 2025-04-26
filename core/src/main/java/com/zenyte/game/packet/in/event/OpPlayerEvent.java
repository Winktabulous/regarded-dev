package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.PlayerHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 21:53
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpPlayerEvent implements ClientProtEvent {
    private final int index;
    private final int option;
    private final boolean run;

    @Override
    public void log(@NotNull final Player player) {
        final Player target = World.getPlayers().get(index);
        if (target == null) {
            log(player, "Index: " + index + ", option: " + option + ", run: " + run);
            return;
        }
        final Location tile = target.getLocation();
        log(player, "Index: " + index + ", option: " + option + ", run: " + run + "; name: " + target.getUsername() + ", location: x" + tile.getX() + ", y" + tile.getY() + ", z: " + tile.getPlane());
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        final Player target = World.getPlayers().get(index);
        if (target == null || target == player || target.isFinished() || !target.isInitialized()) {
            return;
        }
        PlayerHandler.handle(player, target, run, option);
    }

    public OpPlayerEvent(int index, int option, boolean run) {
        this.index = index;
        this.option = option;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
