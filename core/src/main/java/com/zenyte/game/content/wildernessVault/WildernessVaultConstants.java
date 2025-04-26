package com.zenyte.game.content.wildernessVault;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.utils.TimeUnit;

import java.util.Arrays;
import java.util.List;

public class WildernessVaultConstants {

    public static final int WILDERNESS_VAULT_ENTRANCE = 50073;
    public static final int WILDERNESS_VAULT_ENTRANCE_SEALED = 50074;
    public static final int WILDERNESS_VAULT_LADDER = 42208;
    public static final int WILDERNESS_VAULT_LADDER_UP = 42207;

    public static final int CHEST = 50075;
    public static final int CHEST_OPEN = 50076;

    public static final int RARE_CHEST = 50077;
    public static final int RARE_CHEST_OPEN = 50078;

    public static final int LEAVE_BARRIER = 12351;
    public static final Location POSITION_ENTRANCE = new Location(1955, 7022);
    public static final Location CHEST_ENTRANCE = new Location(2131, 7002);
    public static final Location CHEST_SPAWN = new Location(2130, 6991);
    public static final Location CHEST_LADDER = new Location(1955, 6985);

    public static final Location GOBLET_LOCATION = new Location(1955, 6994);
    public static int SPAWN_DELAY = (int) TimeUnit.HOURS.toTicks(1);
    public static final int EVENT_DURATION = (int) TimeUnit.MINUTES.toTicks(10);
    public static final int LOOT_ROOM_TIME = (int) TimeUnit.MINUTES.toTicks(2);
    public static int LOCK_DURATION = (int) TimeUnit.MINUTES.toTicks(5);
    public static final int BOSS_ID = 16041;
    public static final int BOSS_ID_SOULSPLIT = 16044;
    public static final int MINION_ID = 16042;
    public static final int MINION2_ID = 16043;
    public static final int FIRE_OBJECT = 50079;

    public static final RSPolygon FIRE_SPAWN_AREA = new RSPolygon(new int [][]{ {1964, 7017}, {1964, 6998}, {1946, 6998}, {1946, 7017}});
    public static final List<Location> MINION_SPAWNS = Arrays.asList(
            new Location(1965, 7019), new Location(1965, 7012), new Location(1965, 7005), new Location(1965, 6998),
            new Location(1945, 7019), new Location(1945, 7012), new Location(1945, 7005), new Location(1945, 6998)
    );

}
