package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class EventAppletFocusEvent implements ClientProtEvent {
    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Active: " + active);
    }

    private final boolean active;

    @Override
    public void handle(Player player) {
        if (active) {
            player.getTemporaryAttributes().remove("User deemed inactive");
        }
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    public EventAppletFocusEvent(boolean active) {
        this.active = active;
    }
}
