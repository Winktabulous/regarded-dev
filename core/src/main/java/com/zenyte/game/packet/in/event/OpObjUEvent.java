package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnFloorItemHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpObjUEvent implements ClientProtEvent {
    private final int interfaceId;
    private final int componentId;
    private final int slotId;
    private final int itemId;
    private final int floorItemId;
    private final int x;
    private final int y;
    private final boolean run;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", slot: " + slotId + ", item: " + itemId + ", floor item: " + floorItemId + ", x: " + x + ", y: " + y + ", z: " + player.getPlane() + ", run: " + run);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (player.isNulled() || player.isFinished() || player.isDead() || player.isLocked()) {
            return;
        }
        final Location location = new Location(x, y, player.getPlane());
        final FloorItem floorItem = World.getRegion(location.getRegionId()).getFloorItem(floorItemId, location, player);
        if (floorItem == null) {
            return;
        }
        final Item item = player.getInventory().getItem(slotId);
        if (item == null) {
            return;
        }
        player.stopAll();
//        player.setRun(run);
        ItemOnFloorItemHandler.handleItemOnFloorItem(player, item, floorItem);
    }

    public OpObjUEvent(int interfaceId, int componentId, int slotId, int itemId, int floorItemId, int x, int y, boolean run) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.slotId = slotId;
        this.itemId = itemId;
        this.floorItemId = floorItemId;
        this.x = x;
        this.y = y;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
