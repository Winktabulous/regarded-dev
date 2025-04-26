package com.zenyte.game.util;

import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 23. march 2018 : 21:43.00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Direction {
    SOUTH(0, 1, 6, 0, -1),
    SOUTH_WEST(256, 0, 5, -1, -1),
    WEST(512, 3, 3, -1, 0),
    NORTH_WEST(768, 5, 0, -1, 1),
    NORTH(1024, 6, 1, 0, 1),
    NORTH_EAST(1280, 7, 2, 1, 1),
    EAST(1536, 4, 4, 1, 0),
    SOUTH_EAST(1792, 2, 7, 1, -1);

    public static final Direction[] values = values();

    public static final Direction[] mainDirections = new Direction[]{SOUTH, WEST, NORTH, EAST};
    public static final Direction[] cardinalDirections = new Direction[]{WEST, EAST, SOUTH, NORTH};
    public static final Direction[] intercardinalDirections = new Direction[]{SOUTH_WEST, NORTH_WEST, NORTH_EAST, SOUTH_EAST};

    public static final Map<Integer, Direction> npcMap = new HashMap<>(values.length);
    /**
     * Collection of {@link Direction} Enum values in ascending order based on the {@link Direction#direction} integer.
     */
    public static final ObjectLists.UnmodifiableList<Direction> orderedByDirectionValue;
    private static final Map<Integer, Direction> map = new HashMap<>(values.length);

    static {
        for (final Direction dir : values) {
            map.put(dir.movementDirection, dir);
            npcMap.put(dir.NPCDirection, dir);
        }

        final ObjectList<Direction> orderedDirections = new ObjectArrayList<>(values.length);
        map.values().stream().sorted(Comparator.comparingInt(Direction::getDirection)).forEach(orderedDirections::add);
        orderedByDirectionValue = (ObjectLists.UnmodifiableList<Direction>) ObjectLists.unmodifiable(orderedDirections);
    }

    private final int direction;
    private final int movementDirection;
    private final int NPCDirection;

    public static Direction getClosestDirection(int dirValue) {
        Direction direction = null;
        int dirDistance = Integer.MAX_VALUE;
        for (Direction d : values) {
            final int distance = Math.abs(d.direction - dirValue);
            if (distance < dirDistance) {
                direction = d;
                dirDistance = distance;
            }
        }
        return direction;
    }

    public static Direction getOppositeDirection(Direction direction) {
        return getDirection(-direction.getOffsetX(), -direction.getOffsetY());
    }

    @NotNull
    public Direction getCounterClockwiseDirection(int cycles) {
        final int targetDirection = (direction - (cycles << 8)) & 2047;
        for (Direction direction : values) {
            if (direction.getDirection() == targetDirection) {
                return direction;
            }
        }
        throw new RuntimeException();
    }

    public static Direction getMovementDirection(final int value) {
        return map.get(value);
    }

    private final int offsetX;
    private final int offsetY;

    Direction(int direction, int movementDirection, int NPCDirection, int offsetX, int offsetY) {
        this.direction = direction;
        this.movementDirection = movementDirection;
        this.NPCDirection = NPCDirection;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public static Direction getNPCDirection(final int value) {
        return npcMap.get(value);
    }

    public int getDirection() {
        return direction;
    }

    public int getMovementDirection() {
        return movementDirection;
    }

    public int getNPCDirection() {
        return NPCDirection;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public static final Direction DEFAULT = SOUTH;

    public static Direction randomDirection() {
        return Utils.random(values);
    }

    public Pair<Integer, Integer> getCartesianTranslation(Direction direction) {
        return Pair.of(offsetX, offsetY);
    }

    public static Direction getDirection(Location location, Location l) {
        return getDirection(location, l, 0);
    }

    public static Direction getDirection(Location location, Location l, int tolerance) {
        return getDirection(l.getX() - location.getX(), l.getY() - location.getY(), tolerance);
    }

    public static Direction getDirection(int diffX, int diffY) {
        return getDirection(diffX, diffY, 0);
    }

    public static Direction getDirection(int diffX, int diffY, int tolerance) {
        if (diffX < tolerance) {
            if (diffY < tolerance) {
                return SOUTH_WEST;
            } else if (diffY > tolerance) {
                return NORTH_WEST;
            }
            return WEST;
        } else if (diffX > tolerance) {
            if (diffY < tolerance) {
                return SOUTH_EAST;
            } else if (diffY > tolerance) {
                return NORTH_EAST;
            }
            return EAST;
        }
        if (diffY < tolerance) {
            return SOUTH;
        }
        return NORTH;
    }

}
