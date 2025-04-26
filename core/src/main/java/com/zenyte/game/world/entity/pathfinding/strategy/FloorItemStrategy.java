package com.zenyte.game.world.entity.pathfinding.strategy;

import com.zenyte.game.world.entity.pathfinding.RouteStrategy;
import com.zenyte.game.world.flooritem.FloorItem;

public class FloorItemStrategy extends RouteStrategy {

	/**
	 * Entity position x.
	 */
	private final int x;
	/**
	 * Entity position y.
	 */
	private final int y;

	public FloorItemStrategy(final FloorItem entity) {
		super(0);
		x = entity.getLocation().getX();
		y = entity.getLocation().getY();
	}

	public FloorItemStrategy(final FloorItem entity, final int distance) {
		super(distance);
		x = entity.getLocation().getX();
		y = entity.getLocation().getY();
	}

	@Override
	public boolean canExit(final int currentX, final int currentY, final int sizeXY, final int[][] clip, final int clipBaseX, final int clipBaseY) {
		return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY + getDistance(), sizeXY + getDistance(), x - clipBaseX, y - clipBaseY, getApproxDestinationSizeX() + getDistance(), getApproxDestinationSizeY() + getDistance(), 0);
	}

	@Override
	public int getApproxDestinationX() {
		return x;
	}

	@Override
	public int getApproxDestinationY() {
		return y;
	}

    @Override
    public int getApproxDestinationSizeX() {
        return 1;
    }

    @Override
    public int getApproxDestinationSizeY() {
        return 1;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof FloorItemStrategy)) {
            return false;
        }
        final FloorItemStrategy strategy = (FloorItemStrategy) other;
        return x == strategy.x && y == strategy.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
