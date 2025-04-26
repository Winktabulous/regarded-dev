package com.zenyte.game.packet;

import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.PlayerLogger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 24/05/2019 00:20
 * @author Jire
 */
public interface LoggableEvent {

    default void log(@NotNull final Player player) {
        //Empty by default, need to delegate the packets to log.
    }

    default void logChecked(@NotNull final Player player) {
        if (level().getPriority() >= PlayerLogger.WRITE_LEVEL_PRIORITY) {
            log(player);
        }
    }

    LogLevel level();

}
