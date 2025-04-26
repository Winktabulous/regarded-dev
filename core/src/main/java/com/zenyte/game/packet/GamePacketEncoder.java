package com.zenyte.game.packet;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerEvent;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 13:46:49
 * @author Jire
 */
public interface GamePacketEncoder extends ServerEvent, LoggableEvent {

    @NotNull
    default GamePacketEncoderMode encoderMode() {
        return GamePacketEncoderMode.QUEUE;
    }

    GamePacketOut encode();

    default String name() {
        return "[Server prot: " + getClass().getSimpleName() + "] ";
    }

    default void log(@NotNull final Player player, final String text) {
        player.log(level(), name() + text);
    }

}
