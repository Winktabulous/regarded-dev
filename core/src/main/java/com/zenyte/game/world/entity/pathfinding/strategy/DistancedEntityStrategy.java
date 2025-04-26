package com.zenyte.game.world.entity.pathfinding.strategy;

import com.zenyte.game.world.entity.Entity;

/**
 * @author Kris | 10/03/2019 23:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DistancedEntityStrategy extends EntityStrategy {
    private boolean checked;

    public DistancedEntityStrategy(final Entity entity, final int distance) {
        super(entity, distance, 0);
    }

    @Override
    public boolean canExit(final int currentX, final int currentY, final int sizeXY, final int[][] clip, final int clipBaseX, final int clipBaseY) {
        if (!checked) {
            checked = true;//TODO
            return onDistance(currentX, currentY, sizeXY, distance);
        }
        return onDistance(currentX, currentY, sizeXY, distance - 1);
    }

    private boolean onDistance(final int currentX, final int currentY, final int sizeXY, final int distance) {
        final int distanceX = currentX - entity.getX();
        final int distanceY = currentY - entity.getY();
        return distanceX <= size + distance && distanceX >= -sizeXY - distance && distanceY <= size + distance && distanceY >= -sizeXY - distance;
    }
}
