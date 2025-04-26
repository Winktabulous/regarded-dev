package com.zenyte.utils;

public class MathUtils {

    public static final byte[] DIRECTION_DELTA_X = new byte[]{-1, 0, 1, -1, 1, -1, 0, 1};
    public static final byte[] DIRECTION_DELTA_Y = new byte[]{1, 1, 1, 0, 0, -1, -1, -1};

    private static final long INIT_MILLIS = System.currentTimeMillis();
    private static final long INIT_NANOS = System.nanoTime();

    private static long millisSinceClassInit() {
        return (System.nanoTime() - INIT_NANOS) / 1_000_000;
    }

    public static long currentTimeMillis() {
        return INIT_MILLIS + millisSinceClassInit();
    }

    public static final int getFaceDirection(final int xOffset, final int yOffset) {
        return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
    }

    /**
     * Distance formula used to calculate the distance betwen two entities.
     *
     * @param sx
     * @param sy
     * @param dx
     * @param dy
     * @return
     */
    public static int distance(final int sx, final int sy, final int dx, final int dy) {
        final int deltaX = sx - dx;
        final int deltaY = sy - dy;
        return Math.abs(deltaX) + Math.abs(deltaY);
    }

    public static int getDaysFromMillis(final long time) {
        final int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
        final int minutes = seconds / 60;
        final int hours = minutes / 60;
        return (hours / 24);
    }

    public static int getHoursFromMillis(final long time) {
        final int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
        final int minutes = seconds / 60;
        return (minutes / 60);
    }

    public int getHours(final long ms) {
        return (int) ((ms / (1000 * 60 * 60)) % 24);
    }

    public int getMinutes(final long ms) {
        return (int) ((ms / (1000 * 60)) % 60);
    }

    public int getSeconds(final long ms) {
        return (int) ((ms / 1000) % 60);
    }

    public static String getMinutesDisplay(final int timer) {
        String display = "";
        final int minutes = (timer / 60);
        final int seconds = (timer - minutes * 60);
        if (minutes > 0) {
            display += minutes + " minutes ";
        } else {
            display += seconds + "s";
        }
        return display;
    }

    public static int direction(final int dx, final int dy) {
        if (dx < 0) {
            if (dy < 0) {
                return 5;
            } else if (dy > 0) {
                return 0;
            } else {
                return 3;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                return 7;
            } else if (dy > 0) {
                return 2;
            } else {
                return 4;
            }
        } else {
            if (dy < 0) {
                return 2;
            } else if (dy > 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public static final int getMoveDirection(final int xOffset, final int yOffset) {
        if (xOffset < 0) {
            if (yOffset < 0)
                return 5;
            else if (yOffset > 0)
                return 0;
            else
                return 3;
        } else if (xOffset > 0) {
            if (yOffset < 0)
                return 7;
            else if (yOffset > 0)
                return 2;
            else
                return 4;
        } else {
            if (yOffset < 0)
                return 6;
            else if (yOffset > 0)
                return 1;
            else
                return -1;
        }
    }
}
