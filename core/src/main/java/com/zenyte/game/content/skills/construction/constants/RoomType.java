package com.zenyte.game.content.skills.construction.constants;

import com.zenyte.utils.TextUtils;

public enum RoomType {

	LAND(-1, -1, 233, 712, -1, - 1),
	PARLOUR(1, 1000, 232, 719, 1, 1433),
	GARDEN(1, 1000, 232, 713, 2, 1453),
	KITCHEN(5, 5000, 234, 719, 3, 1434),
	DINING_ROOM(10, 5000, 236, 719, 4, 1435),
	WORKSHOP(15, 10000, 232, 717, 12, 1444),
	BEDROOM(20, 10000, 238, 719, 5, 1436),
	SKILL_HALL(25, 15000, 233, 718, 7, 1439),
	GAMES_ROOM(30, 25000, 237, 716, 6, 1437),
	COMBAT_ROOM(32, 25000, 235, 716, 22, 1438),
	QUEST_HALL(35, 25000, 237, 718, 9, 1441),
	MENAGERIE_OUTDOORS(37, 30000, 239, 712, 25, 1457),
	MENAGERIE_INDOORS(37, 30000, 239, 714, 24, 1456),
	STUDY(40, 50000, 236, 717, 13, 1445),
	COSTUME_ROOM(42, 50000, 238, 713, 23, 1455),
	CHAPEL(45, 50000, 234, 717, 11, 1443),
	PORTAL_CHAMBER(50, 100000, 233, 716, 14, 1446),
	FORMAL_GARDEN(55, 75000, 234, 713, 21, 1454),
	THRONE_ROOM(60, 150000, 238, 717, 15, 1447),
	OUBLIETTE(65, 150000, 238, 715, 16, 1448),
	SUPERIOR_GARDEN(65, 75000, 237, 712, 26, 1458),
	DUNGEON_CORRIDOR(70, 7500, 232, 715, 17, 1450),
	DUNGEON_JUNCTION(70, 7500, 236, 715, 18, 1449),
	DUNGEON_STAIRS_ROOM(70, 7500, 234, 715, 19, 1451),
	TREASURE_ROOM(75, 250000, 239, 716, 20, 1452),
	ACHIEVEMENT_GALLERY(80, 250000, 233, 720, 27, 1459),
	DUNGEON_LAND(-1, -1, 235, 712, -1, -1),
	ROOFS_A(-1, -1, 233, 714, -1, -1),
	ROOFS_B(-1, -1, 241, 714, -1, -1),
	QUEST_HALL_DS(35, 25000, 239, 718, 10, 1441),
	SKILL_HALL_DS(25, 15000, 235, 718, 8, 1439);

	private final int level;
    private final int price;
    private final int chunkX;
    private final int chunkY;
    private final int interfaceSlot;
    private final int enumMap;
	
	public static final RoomType[] VALUES = values();

    RoomType(int level, int price, int chunkX, int chunkY, int interfaceSlot, int enumMap) {
        this.level = level;
        this.price = price;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.interfaceSlot = interfaceSlot;
        this.enumMap = enumMap;
    }

    public static RoomType getRoomBySlot(int slot) {
        for (RoomType room : VALUES) {
            if (slot == room.getInterfaceSlot())
                return room;
        }
        return null;
    }

    @Override
    public String toString() {
        return TextUtils.capitalize(name().replace("_", " "));
    }

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getInterfaceSlot() {
        return interfaceSlot;
    }

    public int getEnumMap() {
        return enumMap;
    }
}
