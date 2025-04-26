package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class EventMouseMoveEvent implements ClientProtEvent {

    @Override
    public void handle(Player player) {
    }

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "");
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

}
