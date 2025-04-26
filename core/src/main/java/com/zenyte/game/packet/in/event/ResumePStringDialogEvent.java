package com.zenyte.game.packet.in.event;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.testinterfaces.DropViewerInterface;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.StringDialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:31
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePStringDialogEvent implements ClientProtEvent {
    private final String string;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Value: " + string);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (player.getInterfaceHandler().isPresent(GameInterface.DROP_VIEWER)) {
            DropViewerInterface.search(player, string);
            return;
        }
        final Object input = player.getTemporaryAttributes().get("interfaceInput");
        if (input instanceof StringDialogue) {
            final StringDialogue dialogue = (StringDialogue) input;
            dialogue.execute(player, string);
        }
    }

    public ResumePStringDialogEvent(String string) {
        this.string = string;
    }
}
