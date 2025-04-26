package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Objects;

/**
 * @author Kris | 01/04/2019 20:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LunarStairsObject implements ObjectAction {

    private static final Int2ObjectMap<Location> destinations = new Int2ObjectOpenHashMap<>();

    static {
        destinations.put(Location.hash(2078, 3909, 1), new Location(2078, 3912, 0));
        destinations.put(Location.hash(2078, 3910, 0), new Location(2078, 3908, 1));
        destinations.put(Location.hash(2089, 3918, 0), new Location(2089, 3921, 1));
        destinations.put(Location.hash(2089, 3919, 1), new Location(2089, 3917, 0));
        destinations.put(Location.hash(2104, 3905, 0), new Location(2107, 3905, 1));
        destinations.put(Location.hash(2105, 3905, 1), new Location(2103, 3905, 0));
        destinations.put(Location.hash(2098, 3906, 0), new Location(2097, 3905, 1));
        destinations.put(Location.hash(2098, 3905, 1), new Location(2098, 3908, 0));
        destinations.put(Location.hash(2080, 3894, 0), new Location(2080, 3892, 1));
        destinations.put(Location.hash(2080, 3893, 1), new Location(2080, 3896, 0));
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(Objects.requireNonNull(destinations.get(object.getPositionHash())));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRS_16734, ObjectId.STAIRS_16733, ObjectId.STAIRS_16732 };
    }
}
