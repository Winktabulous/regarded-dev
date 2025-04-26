package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author Tommeh | 30 sep. 2018 | 22:36:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>
 */
public enum SpiritTree {
    TREE_GNOME_VILLAGE("Tree Gnome Village", new Location(2542, 3170, 0)),
    GNOME_STRONGHOLD("Gnome Stronghold", new Location(2461, 3444, 0)),
    BATTLEFIELD_OF_KHAZARD("Battlefield of Khazard", new Location(2555, 3259, 0)),
    GRAND_EXCHANGE("Grand Exchange", new Location(3185, 3508, 0)),
    EDGEVILLE("Edgeville", new Location(3075, 3500, 0)),
    FELDIP_HILLS("Feldip Hills", new Location(2488, 2850, 0)),
    PORT_SARIM("Port Sarim", new Location(3059, 3261, 0)),
    ETCETERIA("Etceteria", new Location(2610, 3857, 0)),
    BRIMHAVEN("Brimhaven", new Location(2799, 3204, 0)),
    HOSIDIUS("Hosidius", new Location(1690, 3541, 0)),
    FARMING_GUILD("Farming Guild", new Location(1250, 3749, 0)),
    //YOUR_HOUSE("Your house", null)
    ;

    private final Location location;
    private final String locationName;

    SpiritTree(String locationName, final Location location) {
        this.location = location;
        this.locationName = locationName;
    }

    public Location getLocation() {
        return location;
    }

    private static final SpiritTree[] VALUES = values();

    public static String[] getAvailableOptions() {
        final ObjectArrayList<String> list = new ObjectArrayList<>(VALUES.length);
        for (final SpiritTree value : VALUES) {
            list.add(value.locationName);
        }
        return list.toArray(new String[0]);
    }

    public static SpiritTree get(final int slot) {
        return VALUES[slot];
    }

    public static SpiritTree getTree(final Player player) {
        final Location location = player.getLocation();
        for (final SpiritTree tree : VALUES) {
            final Location treeLocation = tree.getLocation();
            if (treeLocation == null) continue;
            if (treeLocation.withinDistance(location, 10)) {
                return tree;
            }
        }
        throw new NullPointerException("No spirit tree found near " + location);
    }

}
