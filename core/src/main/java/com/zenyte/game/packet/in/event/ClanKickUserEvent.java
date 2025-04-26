package com.zenyte.game.packet.in.event;

import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Tommeh | 25-1-2019 | 19:53
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClanKickUserEvent implements ClientProtEvent {
    private final String name;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Username: " + name);
    }

    @Override
    public void handle(Player player) {
        final Optional<Player> target = World.getPlayer(name);
        if (!target.isPresent()) {
            return;
        }
        ClanManager.kick(player, true, target.get(), false);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public ClanKickUserEvent(String name) {
        this.name = name;
    }
}
