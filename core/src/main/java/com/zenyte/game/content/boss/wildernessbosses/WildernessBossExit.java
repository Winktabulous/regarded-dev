package com.zenyte.game.content.boss.wildernessbosses;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.List;

/**
 * @author Cresinkel
 */
public final class WildernessBossExit implements ObjectAction {

    public final List<Location> EXIT_LOCATIONS = List.of(
            new Location(3360, 10295, 0),
            new Location(3341, 10255, 0),
            new Location(3376, 10255, 0)
    );

    public enum MinorCaves {
        SPINDEL_DUNGEON_EXIT(new Location(1630, 11527, 2), new Location(3182, 3746, 0)),
        ARTIO_DUNGEON_EXIT(new Location(1758, 11531, 0), new Location(3115, 3675, 0)),
        CALVARION_DUNGEON_EXIT(new Location(1886, 11534, 1), new Location(3180, 3682, 0));

        private final Location object;
        private final Location exitLocation;

        MinorCaves(final Location object, final Location exitLocation) {
            this.object = object;
            this.exitLocation = exitLocation;
        }

        public Location getExitLocation() {
            return this.exitLocation;
        }

        public Location getObject() {
            return this.object;
        }

        public static final MinorCaves[] VALUES = values();
        public static final Int2ObjectMap<MinorCaves> entries = new Int2ObjectOpenHashMap<>(VALUES.length);

        static {
            for(final MinorCaves entry : VALUES) {
                entries.put(entry.getObject().getPositionHash(), entry);
            }
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (MinorCaves.entries.containsKey(object.getPositionHash())) {
            player.lock();
            player.faceObject(object);
            WorldTasksManager.schedule(() -> {
                player.setLocation(MinorCaves.entries.get(object.getPositionHash()).getExitLocation());
                player.unlock();
            }, 0);
            return;
        }

        Location lastLocation = EXIT_LOCATIONS.get(Utils.random(2));
        player.lock();
        player.faceObject(object);
        WorldTasksManager.schedule(() -> {
            player.setLocation(lastLocation);
            player.unlock();
        }, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{47000, 46925, 47122};
    }
}
