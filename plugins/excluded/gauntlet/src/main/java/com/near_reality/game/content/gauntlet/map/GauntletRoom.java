package com.near_reality.game.content.gauntlet.map;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * Represents a single room in the Gauntlet minigame.
 *
 * @author Andys1814.
 * @since 1/26/2022.
 */
public final class GauntletRoom {

    private final GauntletRoomType type;

    private final int gridX;
    private final int gridY;

    private final int chunkX;
    private final int chunkY;

    private final int rotation;

    /* We only need the y coordinate of the static chunk because the x is the same for each respective type, only offset by the same y. */
    private final int staticChunkY;

    private final List<Location> singleTileResourceSpawnLocations = new ObjectArrayList<>();
    private final List<Location> twoTileResourceSpawnLocations = new ObjectArrayList<>();

    private final Map<Direction, Pair<Location, Location>> lightLocations = new HashMap<>();
    private final Map<Direction, Pair<Location, Location>> symbolLocations = new HashMap<>();

    public GauntletRoom(GauntletRoomType type, int gridX, int gridY, int chunkX, int chunkY, int rotation) {
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.rotation = rotation;
        this.staticChunkY = type.getRandomStaticChunkY();

        /* Locations of the lights for each of the main cardinal directions. */
        lightLocations.put(Direction.NORTH, Pair.of(new Location(getBaseX() + 6, getBaseY() + 14, 1), new Location(getBaseX() + 9, getBaseY() + 14, 1)));
        lightLocations.put(Direction.SOUTH, Pair.of(new Location(getBaseX() + 6, getBaseY(), 1), new Location(getBaseX() + 9, getBaseY(), 1)));
        lightLocations.put(Direction.EAST, Pair.of(new Location(getBaseX() + 14, getBaseY() + 6, 1), new Location(getBaseX() + 14, getBaseY() + 9, 1)));
        lightLocations.put(Direction.WEST, Pair.of(new Location(getBaseX(), getBaseY() + 6, 1), new Location(getBaseX(), getBaseY() + 9, 1)));

        /* Locations of the symbols for each of the main cardinal directions. */
        symbolLocations.put(Direction.NORTH, Pair.of(new Location(getBaseX() + 2, getBaseY() + 14, 1), new Location(getBaseX() + 10, getBaseY() + 14, 1)));
        symbolLocations.put(Direction.SOUTH, Pair.of(new Location(getBaseX() + 2, getBaseY() + 1, 1), new Location(getBaseX() + 10, getBaseY() + 1, 1)));
        symbolLocations.put(Direction.EAST, Pair.of(new Location(getBaseX() + 14, getBaseY() + 2, 1), new Location(getBaseX() + 14, getBaseY() + 10, 1)));
        symbolLocations.put(Direction.WEST, Pair.of(new Location(getBaseX() + 1, getBaseY() + 2, 1), new Location(getBaseX() + 1, getBaseY() + 10, 1)));
    }

    public void findPossibleResourceLocations() {
        int baseX = chunkX << 3;
        int baseY = chunkY << 3;

        BiPredicate<Integer, Integer> containsObject = (x, y) -> !World.isFloorFree(1, x, y);

        /* Predicate that filters out tiles near an entrance or adjacent to a room lighting object. */
        BiPredicate<Integer, Integer> ignoredTile = (x, y) -> {
            int xOffset = x - (baseX + 2);
            int yOffset = y - (baseY + 2);

            if (xOffset == 0) {
                return yOffset >= 4 && yOffset <= 7;
            }

            if (xOffset == 11) {
                return yOffset >= 4 && yOffset <= 7;
            }

            if (yOffset == 0) {
                return xOffset >= 4 && xOffset <= 7;
            }

            if (yOffset == 11) {
                return xOffset >= 4 && xOffset <= 7;
            }

            return false;
        };

        // Nested for-loop that iterates over each walkable tile in this 2x2 chunk room.
        for (int x = baseX + 2; x < baseX + 11; x++) {
            for (int y = baseY + 2; y < baseY + 11; y++) {
                if (ignoredTile.test(x, y)) {
                    continue;
                }

                if (containsObject.test(x, y + 1)
                        || containsObject.test(x, y - 1)
                        || containsObject.test(x - 1, y)
                        || containsObject.test(x + 1, y)
                        || containsObject.test(x, y + 2)
                        || containsObject.test(x, y - 2)
                        || containsObject.test(x + 2, y)
                        || containsObject.test(x - 2, y)) {
                    if (World.isFloorFree(1, x, y)) {
                        singleTileResourceSpawnLocations.add(new Location(x, y, 1));
                    }

                    if (World.isFloorFree(1, x, y)
                            && World.isFloorFree(1, x + 1, y)
                            && World.isFloorFree(1, x, y + 1)
                            && World.isFloorFree(1, x + 1, y + 1)) {
                        twoTileResourceSpawnLocations.add(new Location(x, y, 1));
                    }
                }
            }
        }

    }

//    public Location getLocation(final int x, final int y) {
//        final int offsetX = x - (type.getStaticChunkX() * 8);
//        final int offsetY = y - (staticChunkY * 8);
//
//        final double radians = Math.toRadians((rotation == 1 ? 3 : rotation == 3 ? 1 : rotation) * 90);
//
//        // 7.5 IS GOOD! 7 buggy
//        final int transformedX = (int) Math.round(7.5 + ((offsetX - 7.5) * Math.cos(radians)) - ((offsetY - 7.5) * Math.sin(radians)));
//        final int transformedY = (int) Math.round(7.5 + ((offsetX - 7.5) * Math.sin(radians)) + ((offsetY - 7.5) * Math.cos(radians)));
//
//        return new Location((chunkX * 8) + transformedX, (chunkY * 8) + transformedY, 1);
//    }
//
//    public Location getRotated(final int x, final int y) {
//        final int offsetX = x - (type.getStaticChunkX() * 8);
//        final int offsetY = y - (staticChunkY * 8);
//
//        final double radians = Math.toRadians((rotation == 1 ? 3 : rotation == 3 ? 1 : rotation) * 90);
//
//        // 7.5 IS GOOD! 7 buggy
//        final int transformedX = (int) Math.round(7.5 + ((offsetX - 7.5) * Math.cos(radians)) - ((offsetY - 7.5) * Math.sin(radians)));
//        final int transformedY = (int) Math.round(7.5 + ((offsetX - 7.5) * Math.sin(radians)) + ((offsetY - 7.5) * Math.cos(radians)));
//
//        return new Location((type.getStaticChunkX() * 8) + transformedX, (staticChunkY * 8) + transformedY, 1);
//    }

    public boolean isDemiBossRoom() {
        return isDemiBossRoom(gridX, gridY);
    }

    /* Demi bosses only spawn in the middle three rooms on each of the four edges. */
    public static boolean isDemiBossRoom(int gridX, int gridY) {
        if (gridX == 0 || gridX == 6) {
            return gridY == 2 || gridY == 3 || gridY == 4;
        }

        if (gridY == 0 || gridY == 6) {
            return gridX == 2 || gridX == 3 || gridX == 4;
        }

        return false;
    }

    public Map<Direction, Pair<Location, Location>> getLightLocations() {
        return lightLocations;
    }
//
    public Map<Direction, Pair<Location, Location>> getSymbolLocations() {
        return symbolLocations;
    }

    public GauntletRoomType getType() {
        return type;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getRotation() {
        return rotation;
    }

    public int getBaseX() {
        return chunkX << 3;
    }

    public int getBaseY() {
        return chunkY << 3;
    }

    public int getStaticChunkY() {
        return staticChunkY;
    }

    public List<Location> getSingleTileResourceSpawnLocations() {
        return singleTileResourceSpawnLocations;
    }

    public List<Location> getTwoTileResourceSpawnLocations() {
        return twoTileResourceSpawnLocations;
    }

    public Location getBaseLocation() {
        return new Location(getBaseX(), getBaseY(), 1);
    }

    public boolean isOuterRing() {
        return gridX == 0 || gridX == 6 || gridY == 0 || gridY == 6;
    }
}
