package mgi.types.worldmap;

import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 4-12-2018 | 19:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
final class MapElement {
    /**
     * The boolean 'serverOnly' is only true for Fossil Island underground, specifically link icons in there.
     * MapElement(serverOnly=true, id=14, location=Tile: 3732, 10281, 1, region[15008, 58, 160], chunk[466, 1285],
     * hash [329590825])
     * MapElement(serverOnly=true, id=14, location=Tile: 3596, 10292, 1, region[14496, 56, 160], chunk[449, 1286],
     * hash [327362612])
     * MapElement(serverOnly=true, id=14, location=Tile: 3604, 10231, 0, region[14495, 56, 159], chunk[450, 1278],
     * hash [59058167])
     * MapElement(serverOnly=true, id=14, location=Tile: 3904, 10225, 0, region[15775, 61, 159], chunk[488, 1278],
     * hash [63973361])
     */
    private final boolean serverOnly;
    private final int id;
    private final Location location;

    public MapElement(boolean serverOnly, int id, Location location) {
        this.serverOnly = serverOnly;
        this.id = id;
        this.location = location;
    }

    public boolean isServerOnly() {
        return serverOnly;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "MapElement(serverOnly=" + this.isServerOnly() + ", id=" + this.getId() + ", location=" + this.getLocation() + ")";
    }

}
