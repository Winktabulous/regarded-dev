package com.zenyte.game.util;

import com.zenyte.game.world.entity.Location;

public class CollisionUtil {
    public static boolean collides(final Location first, final int firstSize, final Location second, final int secondSize, final int extraDistance) {
        final int distanceX = first.getX() - second.getX();
        final int distanceY = first.getY() - second.getY();
        final int size1 = firstSize + extraDistance;
        final int size2 = secondSize + extraDistance;
        return distanceX < (size2) && distanceX > (-size1) && distanceY < (size2) && distanceY > (-size1);
    }

    public static boolean collides(final int x1, final int y1, final int size1, final int x2, final int y2, final int size2) {
        final int distanceX = x1 - x2;
        final int distanceY = y1 - y2;
        return distanceX < size2 && distanceX > -size1 && distanceY < size2 && distanceY > -size1;
    }
}
