package com.zenyte.game.world.entity.masks;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;

/**
 * @author Tom
 */
public class ForceMovement {

    public static final int SOUTH = 0, WEST = 0x200, NORTH = 0x400, EAST = 0x600;

    private Location toFirstTile, toSecondTile;
    private int firstTileTicketDelay, secondTileTicketDelay, direction;

    public ForceMovement(final Location toFirstTile, final int firstTileTicketDelay, final int direction) {
        this(toFirstTile, firstTileTicketDelay, null, 0, direction);
    }

    public ForceMovement(final Location toFirstTile, final int firstTileTicketDelay, final Location toSecondTile, final int secondTileTicketDelay, final int direction) {
        this.toFirstTile = toFirstTile;
        this.firstTileTicketDelay = firstTileTicketDelay;
        this.toSecondTile = toSecondTile;
        this.secondTileTicketDelay = secondTileTicketDelay;
        this.direction = direction;
    }

    public Location getToFirstTile() {
        return toFirstTile;
    }

    public void setToFirstTile(Location toFirstTile) {
        this.toFirstTile = toFirstTile;
    }

    public Location getToSecondTile() {
        return toSecondTile;
    }

    public void setToSecondTile(Location toSecondTile) {
        this.toSecondTile = toSecondTile;
    }

    public int getFirstTileTicketDelay() {
        return firstTileTicketDelay;
    }

    public void setFirstTileTicketDelay(int firstTileTicketDelay) {
        this.firstTileTicketDelay = firstTileTicketDelay;
    }

    public int getSecondTileTicketDelay() {
        return secondTileTicketDelay;
    }

    public void setSecondTileTicketDelay(int secondTileTicketDelay) {
        this.secondTileTicketDelay = secondTileTicketDelay;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public static Direction direction(Location s, Location d) {
        Location delta = Location.getDelta(s, d);
        int x = Math.abs(delta.getX());
        int y = Math.abs(delta.getY());
        if (x > y) {
            return Direction.getDirection(delta.getX(), 0);
        }
        return Direction.getDirection(0, delta.getY());
    }

}