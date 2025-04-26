package com.zenyte.game.world.object;

/**
 * @author Kris | 1. apr 2018 : 4:02.52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class AttachedObject {

    private final WorldObject object;
    private final int startTime, endTime, minX, maxX, minY, maxY;

    public AttachedObject(WorldObject object, int startTime, int endTime, int minX, int maxX, int minY, int maxY) {
        this.object = object;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public WorldObject getObject() {
        return object;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

}