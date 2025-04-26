package com.zenyte.game.content.skills.construction.constants;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TextUtils;

public enum House {

	RIMMINGTON(1, 5000, 15478, new Location(2953, 3223, 0)),
	TAVERLY(10, 5000, 15477, new Location(2893, 3464, 0)),
	POLLNIVNEACH(20, 7500, 15479, new Location(3341, 3003, 0)),
	KOUREND(25, 8750, 28822, new Location(1742, 3516, 0)),
	RELLEKKA(30, 10000, 15480, new Location(2671, 3631, 0)),
	BRIMHAVEN(40, 15000, 15481, new Location(2757, 3177, 0)),
	YANILLE(50, 25000, 15482, new Location(2543, 3096, 0));

	private final Location location;
	private final int level;
    private final int price;
    private final int objectId;

    public static final House[] VALUES = values();

    House(int level, int price, int objectId, Location location) {
        this.level = level;
        this.price = price;
        this.objectId = objectId;
        this.location = location;
    }

    public static House getHouseByObject(WorldObject object) {
        for (House house : House.VALUES) {
            if (object.getId() == house.getObjectId())
                return house;
        }
        return null;
    }

    @Override
    public String toString() {
        return TextUtils.capitalize(name().toLowerCase().replace("_", " "));
    }

    public Location getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public int getObjectId() {
        return objectId;
    }
}
