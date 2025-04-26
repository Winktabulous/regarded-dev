package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.CountDialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePCountDialogEvent implements ClientProtEvent {
    private final int value;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Value: " + value);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        final Object input = player.getTemporaryAttributes().get("interfaceInput");
        if (input instanceof CountDialogue) {
            final CountDialogue dialogue = (CountDialogue) input;
            dialogue.execute(player, value);
        }
    }

    public ResumePCountDialogEvent(int value) {
        this.value = value;
    }
}
