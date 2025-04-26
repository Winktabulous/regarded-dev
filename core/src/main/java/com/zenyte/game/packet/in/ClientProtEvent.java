package com.zenyte.game.packet.in;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.packet.LoggableEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ClientEvent;
import kotlinx.datetime.Instant;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface ClientProtEvent extends ClientEvent, LoggableEvent, AutoCloseable {

    void handle(final Player player);

    default String name() {
        return "[Client prot: " + getClass().getSimpleName() + "] ";
    }

    default void log(@NotNull final Player player, final String text) {
        player.log(level(), name() + text);
    }

    default QueueType getQueueType() {
        return null;
    }

    default void handleEvent(Player player) {
/*        QueueType queueType = getQueueType();
        if (queueType != null && !player.getQueueStack().overtakeQueues(queueType)) {
            return;
        }*/
        handle(player);
    }

    @Override
    default void close() throws Exception {
    }

}
