package com.zenyte.game.packet.in.event;

import com.near_reality.tools.BotPrevention;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:08
 * @author Jire
 */
public final class EventMouseClickEvent implements ClientProtEvent {

    private final int lastButton;
    private final int lastPressedMillis;
    private final int lastPressedX;
    private final int lastPressedY;

    public EventMouseClickEvent(int lastButton, int lastPressedMillis,
                                int lastPressedX, int lastPressedY) {
        this.lastButton = lastButton;
        this.lastPressedMillis = lastPressedMillis;
        this.lastPressedX = lastPressedX;
        this.lastPressedY = lastPressedY;
    }

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "");
    }

    @Override
    public void handle(Player player) {
        player.incrementNumericTemporaryAttribute(BotPrevention.MOUSE_CLICKS_ATTRIBUTE_KEY, 1);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    public int getLastButton() {
        return lastButton;
    }

    public int getLastPressedMillis() {
        return lastPressedMillis;
    }

    public int getLastPressedX() {
        return lastPressedX;
    }

    public int getLastPressedY() {
        return lastPressedY;
    }

}
