package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 21:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpNpcUEvent implements ClientProtEvent {
    private final int interfaceId;
    private final int componentId;
    private final int slotId;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", slot: " + slotId);
    }

    private final int itemId;
    private final int index;
    private final boolean run;

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (player.isLocked()) {
            return;
        }
        final NPC npc = World.getNPCs().get(index);
        if (npc == null) {
            return;
        }
        final Item item = player.getInventory().getItem(slotId);
        if (item == null) {
            return;
        }
        if (run) {
            if (player.eligibleForShiftTeleportation()) {
                player.setLocation(new Location(npc.getLocation()));
                return;
            }
            player.setRun(true);
        }
        ItemOnNPCHandler.handleItemOnNPC(player, item, slotId, npc);
    }

    public OpNpcUEvent(int interfaceId, int componentId, int slotId, int itemId, int index, boolean run) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.slotId = slotId;
        this.itemId = itemId;
        this.index = index;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
