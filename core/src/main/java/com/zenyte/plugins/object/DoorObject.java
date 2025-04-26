package com.zenyte.plugins.object;

import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.DoorHandler;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;

/**
 * @author Kris | 10. nov 2017 : 22:01.53
 * @author Jire
 */
public final class DoorObject implements ObjectAction {

    private final Int2LongMap doorToClosedTimes = new Int2LongOpenHashMap(Short.MAX_VALUE);
    private final Int2LongMap doorToNextOpenTick = new Int2LongOpenHashMap(Short.MAX_VALUE);

    private static final int MAX_CLOSES_INTERVAL_TICKS = 100;
    private static final int MAX_CLOSES_PER_INTERVAL = 5;

    int[] ignore = new int[] {39251, 39253};
    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name,
                                   final int optionId, final String option) {
        final boolean close = "Close".equals(option);

        for (int i : ignore) {
            if(i == object.getId())
                return;
        }

        if (!close && !option.equals("Open")) return;

        if (close && disableClosing(object)) {
            String named = name;
            if (named == null) named = "door";
            else named = named.toLowerCase();

            player.sendMessage("The " + named + " appears to be stuck.");
            return;
        }
        DoorHandler.handleDoor(object);
    }

    private boolean disableClosing(final WorldObject o) {
        final long tick = WorldThread.getCurrentCycle();
        final int hashCode = o.hashCode();

        if (tick < doorToNextOpenTick.getOrDefault(hashCode, tick)) {
            return true;
        }

        long closedTimes = doorToClosedTimes.getOrDefault(hashCode, 0);
        if (closedTimes >= MAX_CLOSES_PER_INTERVAL) {
            doorToNextOpenTick.put(hashCode, tick + MAX_CLOSES_INTERVAL_TICKS);
            doorToClosedTimes.remove(hashCode);
            return true;
        }
        doorToClosedTimes.put(hashCode, closedTimes + 1);
        return false;
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{
                ObjectId.TOWER_DOOR,
                ObjectId.GATE_2623,
                ObjectId.GATE_21600,
                "Door",
                "Large door",
                ObjectId.GATE_3444,
                ObjectId.GATE_3445,
                "Bamboo Door",
                ObjectId.STRANGE_WALL_4545,
                ObjectId.STRANGE_WALL_4546,
                ObjectId.WALL_2606,
                ObjectId.GATE_9141,
                ObjectId.LARGE_DOOR_17089,
                ObjectId.LARGE_DOOR_1517,
                ObjectId.LARGE_DOOR_22435,
                ObjectId.LARGE_DOOR_22436,
                ObjectId.LARGE_DOOR_22437,
                ObjectId.LARGE_DOOR_22438,
        };
    }

}
