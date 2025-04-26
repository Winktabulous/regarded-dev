package com.zenyte.game.util;

import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.List;

public class LocationUtil {
    /**
     * Constructs an arraylist of tiles between two points. x1/y1 -> Starting
     * location. x2/y2 -> End location. Draws as straight of a line as possible
     * between the given locations
     */
    public static List<Location> calculateLine(int x1, int y1, final int x2, final int y2, final int plane) {
        final List<Location> tiles = new ArrayList<Location>();
        final int dx = Math.abs(x2 - x1);
        final int dy = Math.abs(y2 - y1);
        final int sx = (x1 < x2) ? 1 : -1;
        final int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        while (true) {
            tiles.add(new Location(x1, y1, plane));
            if (x1 == x2 && y1 == y2) {
                break;
            }
            final int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
        return tiles;
    }

    public static IntArrayList calculateLineHash(final IntArrayList tiles, int x1, int y1, final int x2, final int y2, final int plane) {
        if (!tiles.isEmpty()) {
            tiles.clear();
        }
        final int dx = Math.abs(x2 - x1);
        final int dy = Math.abs(y2 - y1);
        final int sx = (x1 < x2) ? 1 : -1;
        final int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        while (true) {
            tiles.add(Location.getHash(x1, y1, plane));
            if (x1 == x2 && y1 == y2) {
                break;
            }
            final int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
        return tiles;
    }

    public static int getAngle(final Location from, final Location target) {
        double angle = Math.toDegrees(Math.atan2(target.getY() - from.getY(), -(target.getX() - from.getX())));
        if (angle < 0) {
            angle += 360;
        }
        return (int) angle;
    }
}
