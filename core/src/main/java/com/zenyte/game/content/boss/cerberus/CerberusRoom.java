package com.zenyte.game.content.boss.cerberus;

import com.zenyte.game.content.boss.cerberus.area.*;
import com.zenyte.game.world.entity.Location;

import java.util.List;

/**
 * @author Kris | 22. march 2018 : 16:17.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CerberusRoom {

	public static CerberusRoom WEST = new CerberusRoom(4883, WesternCerberusArea.class, new Location(1289, 1252, 0), new Location(1240, 1226, 0));
    public static CerberusRoom NORTH = new CerberusRoom(5140, NorthernCerberusArea.class, new Location(1310, 1273, 0), new Location(1304, 1290, 0));
    public static CerberusRoom EAST = new CerberusRoom(5395, EasternCerberusArea.class, new Location(1332, 1252, 0), new Location(1368, 1226, 0));
    public static CerberusRoom INSTANCED = new CerberusRoom(4883, WesternCerberusArea.class, new Location(1289, 1252, 0), new Location(1240, 1226, 0));
	
	private final int regionId;
	private final Class<? extends StaticCerberusLair> clazz;
    private static final List<CerberusRoom> values = List.of(WEST, NORTH, EAST);
    private final Location entrance, exit;

    CerberusRoom(int regionId, Class<? extends StaticCerberusLair> clazz, Location entrance, Location exit) {
        this.regionId = regionId;
        this.clazz = clazz;
        this.entrance = entrance;
        this.exit = exit;
    }

    public static List<CerberusRoom> getValues() {
        return values;
    }

    public Location getWesternFireLocation() {
        return exit.transform(-1, 16, 0);
    }

    public Location getWesternSoulLocation() {
        return exit.transform(-1, 39, 0);
    }

    public int getRegionId() {
        return regionId;
    }

    public Class<? extends StaticCerberusLair> getClazz() {
        return clazz;
    }

    public Location getEntrance() {
        return entrance;
    }

    public Location getExit() {
        return exit;
    }

}
