package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class IgnoreListAddEvent implements ClientProtEvent {
    @Override
    public void log(@NotNull final Player player) {
        log(player, "Name: " + name);
    }

    private String name;

    @Override
    public void handle(Player player) {
        if (name.length() > 12) {
            name = name.substring(0, 12);
        }
        name = name.replaceAll(" ", "_");
        player.getSocialManager().addIgnore(name);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public IgnoreListAddEvent(String name) {
        this.name = name;
    }
}
