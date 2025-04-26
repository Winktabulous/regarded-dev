package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemDialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 22:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePObjDialogEvent implements ClientProtEvent {
    private final int itemId;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Value: " + itemId);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (itemId == -1) {
            return;
        }
        final Object input = player.getTemporaryAttributes().get("interfaceInput");
        if (input instanceof ItemDialogue) {
            final ItemDialogue dialogue = (ItemDialogue) input;
            dialogue.execute(player, itemId);
        }
    }

    public ResumePObjDialogEvent(int itemId) {
        this.itemId = itemId;
    }
}
