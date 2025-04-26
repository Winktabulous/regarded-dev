package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnPlayerHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 21:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpPlayerUEvent implements ClientProtEvent {
    private final int targetIndex;
    private final int slotId;
    private final int itemId;
    private final int interfaceId;
    private final int run;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", slot: " + slotId + ", itemId: " + itemId + ", run: " + run + ", target: " + targetIndex + " -> " + World.getPlayers().get(targetIndex));
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (player.isLocked()) {
            return;
        }
        final Player target = World.getPlayers().get(targetIndex);
        if (target == null) {
            return;
        }
        final Item item = player.getInventory().getItem(slotId);
        if (item == null) {
            return;
        }
        if (run == 1) {
            if (player.eligibleForShiftTeleportation()) {
                player.setLocation(new Location(target.getLocation()));
                return;
            }
            player.setRun(true);
        }
        ItemOnPlayerHandler.handleItemOnPlayer(player, item, slotId, target);
    }

    public OpPlayerUEvent(int targetIndex, int slotId, int itemId, int interfaceId, int run) {
        this.targetIndex = targetIndex;
        this.slotId = slotId;
        this.itemId = itemId;
        this.interfaceId = interfaceId;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
