package com.zenyte.game.util;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;

public class DirectionUtil {

    public static Direction getClosestFaceDirection(final Entity entity, final Entity other) {
        final int faceDirection = getFaceDirection(other.getLocation().getCoordFaceX(other.getSize()) - entity.getMiddleLocation().getX(),
                other.getLocation().getCoordFaceY(other.getSize()) - entity.getMiddleLocation().getY());
        int diff = Integer.MAX_VALUE;
        Direction direction = Direction.WEST;
        for (Direction d : Direction.values) {
            final int dDiff = Math.abs(d.getDirection() - faceDirection);
            if (dDiff < diff) {
                diff = dDiff;
                direction = d;
            }
        }
        return direction;
    }

    public static int getFaceDirection(final Location destination, final Location location) {
        return ((int) ((Math.atan2(-(destination.getX() - location.getX()), -(destination.getY() - location.getY())) * 325.949)) & 2047);
    }

    public static int getFaceDirection(final double xOffset, final double yOffset) {
        return ((int) ((Math.atan2(-xOffset, -yOffset) * 325.949)) & 2047);
    }

    public static int getMoveDirection(final int xOffset, final int yOffset) {
        if (xOffset < 0) {
            if (yOffset < 0) {
                return 0;
            } else if (yOffset > 0) {
                return 5;
            } else {
                return 3;
            }
        } else if (xOffset > 0) {
            if (yOffset < 0) {
                return 2;
            } else if (yOffset > 0) {
                return 7;
            } else {
                return 4;
            }
        } else {
            if (yOffset < 0) {
                return 1;
            } else if (yOffset > 0) {
                return 6;
            } else {
                return -1;
            }
        }
    }

    /**
     * Gets the direction.
     *
     * @param location The start location.
     * @param l        The end location.
     * @return The direction.
     */
    public static Direction getDirection(Location location, Location l) {
        return getDirection(location, l, 0);
    }

    public static Direction getDirection(Location location, Location l, int tolerance) {
        return getDirection(l.getX() - location.getX(), l.getY() - location.getY(), tolerance);
    }

    public static Direction getDirection(int diffX, int diffY, int tolerance) {
        if (diffX < tolerance) {
            if (diffY < tolerance) {
                return Direction.SOUTH_WEST;
            } else if (diffY > tolerance) {
                return Direction.NORTH_WEST;
            }
            return Direction.WEST;
        } else if (diffX > tolerance) {
            if (diffY < tolerance) {
                return Direction.SOUTH_EAST;
            } else if (diffY > tolerance) {
                return Direction.NORTH_EAST;
            }
            return Direction.EAST;
        }
        if (diffY < tolerance) {
            return Direction.SOUTH;
        }
        return Direction.NORTH;
    }

}
