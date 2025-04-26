package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CommandEvent implements ClientProtEvent {

    private final String command;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Command: " + command);
    }

    @Override
    public void handle(Player player) {
        try {
            GameCommands.process(player, command);
        } catch (final Exception e) {
            player.getPacketDispatcher().sendGameMessage("Error processing command %s: %s (%s).", true, command, e.getClass(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public CommandEvent(String command) {
        this.command = command;
    }
}
