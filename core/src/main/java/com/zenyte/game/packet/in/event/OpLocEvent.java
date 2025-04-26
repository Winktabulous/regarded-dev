package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 21:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpLocEvent implements ClientProtEvent {

    private final int id, x, y, option;
    private final boolean run;

    @Override
    public void handle(Player player) {
        ObjectHandler.handle(player, id, new Location(x, y, player.getPlane()), run, option);
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Id: " + id + ", x: " + x + ", y: " + y + ", z: " + player.getPlane() + ", option: " + option + ", run: " + run);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public OpLocEvent(int id, int x, int y, int option, boolean run) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.option = option;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
