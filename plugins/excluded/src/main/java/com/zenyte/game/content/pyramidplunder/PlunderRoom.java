package com.zenyte.game.content.pyramidplunder;

import com.zenyte.game.content.pyramidplunder.area.SpearTrap;
import com.zenyte.game.content.pyramidplunder.object.GrandGoldChest;
import com.zenyte.game.content.pyramidplunder.object.PlunderDoor;
import com.zenyte.game.content.pyramidplunder.object.Sarcophagus;
import com.zenyte.game.content.pyramidplunder.object.Urn;
import com.zenyte.game.util.IntListUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import it.unimi.dsi.fastutil.ints.IntLists;

import java.util.EnumMap;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public enum PlunderRoom {
    FIRST_ROOM(1, 21, 60, 40, 20, 40, 10, new ImmutableLocation(1927, 4477, 0), new RSPolygon(new int[][] {{1920, 4480}, {1920, 4463}, {1934, 4463}, {1934, 4480}})), SECOND_ROOM(2, 31, 90, 60, 30, 60, 10, new ImmutableLocation(1954, 4477, 0), new RSPolygon(new int[][] {{1947, 4480}, {1947, 4463}, {1961, 4463}, {1961, 4480}})), THIRD_ROOM(3, 41, 150, 100, 50, 100, 10, new ImmutableLocation(1977, 4471, 0), new RSPolygon(new int[][] {{1968, 4473}, {1968, 4453}, {1982, 4453}, {1982, 4473}})), FOURTH_ROOM(4, 51, 215, 140, 70, 140, 10, new ImmutableLocation(1927, 4453, 0), new RSPolygon(new int[][] {{1924, 4460}, {1924, 4448}, {1943, 4448}, {1943, 4460}})), FIFTH_ROOM(5, 61, 300, 200, 100, 200, 10, new ImmutableLocation(1965, 4444, 0), new RSPolygon(new int[][] {{1951, 4456}, {1951, 4442}, {1967, 4442}, {1967, 4456}})), SIXTH_ROOM(6, 71, 450, 300, 150, 300, 10, new ImmutableLocation(1927, 4424, 0), new RSPolygon(new int[][] {{1921, 4441}, {1921, 4422}, {1933, 4422}, {1933, 4441}})), SEVENTH_ROOM(7, 81, 675, 450, 225, 450, 10, new ImmutableLocation(1943, 4421, 0), new RSPolygon(new int[][] {{1940, 4434}, {1940, 4420}, {1956, 4420}, {1956, 4434}})), EIGHT_ROOM(8, 91, 825, 550, 275, 0, 10, new ImmutableLocation(1974, 4420, 0), new RSPolygon(new int[][] {{1967, 4438}, {1967, 4419}, {1980, 4419}, {1980, 4438}}));
    public static final IntLists.UnmodifiableList doors = IntListUtils.range(26618, 26621);
    public static final PlunderRoom[] rooms = PlunderRoom.values();
    public static final EnumMap<PlunderRoom, Integer> currentDoors = new EnumMap<>(PlunderRoom.class);

    static {
        shuffle();
    }

    private final int roomId;
    private final int level;
    private final double urnExp;
    private final double chestExp;
    private final double sarcophagusExp;
    private final double doorExp;
    private final double trapExp;
    private final ImmutableLocation start;
    private final RSPolygon polygon;

    public static PlunderRoom get(int roomId) {
        for (PlunderRoom room : rooms) {
            if (room.getRoomId() == roomId) {
                return room;
            }
        }
        throw new IllegalArgumentException("No pyramid plunder room defined for id: " + roomId);
    }

    public static void shuffle() {
        for (PlunderRoom room : rooms) {
            final int randomDoor = doors.getInt(Utils.random(doors.size() - 1));
            currentDoors.put(room, randomDoor);
        }
    }

    public static void reset(final Player player) {
        SpearTrap.reset(player);
        GrandGoldChest.reset(player);
        Urn.reset(player);
        PlunderDoor.reset(player);
        Sarcophagus.reset(player);
    }

    public int getCurrentDoor() {
        return currentDoors.get(this);
    }

    public boolean hasNext() {
        return rooms[rooms.length - 1].getRoomId() > roomId;
    }

    public PlunderRoom next() {
        for (PlunderRoom room : rooms) {
            if (room.getRoomId() == roomId + 1) {
                return room;
            }
        }
        throw new IllegalStateException("Tried to access next plunder room when there are none left after: " + roomId);
    }

    PlunderRoom(int roomId, int level, double urnExp, double chestExp, double sarcophagusExp, double doorExp, double trapExp, ImmutableLocation start, RSPolygon polygon) {
        this.roomId = roomId;
        this.level = level;
        this.urnExp = urnExp;
        this.chestExp = chestExp;
        this.sarcophagusExp = sarcophagusExp;
        this.doorExp = doorExp;
        this.trapExp = trapExp;
        this.start = start;
        this.polygon = polygon;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getLevel() {
        return level;
    }

    public double getUrnExp() {
        return urnExp;
    }

    public double getChestExp() {
        return chestExp;
    }

    public double getSarcophagusExp() {
        return sarcophagusExp;
    }

    public double getDoorExp() {
        return doorExp;
    }

    public double getTrapExp() {
        return trapExp;
    }

    public ImmutableLocation getStart() {
        return start;
    }

    public RSPolygon getPolygon() {
        return polygon;
    }
}
