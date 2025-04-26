package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpHeldUEvent implements ClientProtEvent {
    private final int fromSlotId;
    private final int fromItemId;
    private final int toSlotId;
    private final int toItemId;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Item: " + fromItemId + " -> " + toItemId + ", slot: " + fromSlotId + " -> " + toSlotId);
    }

    @Override
    public void handle(Player player) {
        final Item from = player.getInventory().getItem(fromSlotId);
        final Item to = player.getInventory().getItem(toSlotId);
        if (from == null || to == null || player.isLocked()) {
            return;
        }
        ItemOnItemHandler.handleItemOnItem(player, from, to, fromSlotId, toSlotId);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public OpHeldUEvent(int fromSlotId, int fromItemId, int toSlotId, int toItemId) {
        this.fromSlotId = fromSlotId;
        this.fromItemId = fromItemId;
        this.toSlotId = toSlotId;
        this.toItemId = toItemId;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
