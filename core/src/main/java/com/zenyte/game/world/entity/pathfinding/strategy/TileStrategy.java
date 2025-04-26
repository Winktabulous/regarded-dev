package com.zenyte.game.world.entity.pathfinding.strategy;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.RouteStrategy;

public class TileStrategy extends RouteStrategy {

	protected final int x;
    protected final int y;
    protected final int accessBlockFlag;

	public TileStrategy(final Location location) {
		this(location.getX(), location.getY(), 0, 0);
	}
	
	public TileStrategy(final Location location, final int distance) {
		this(location.getX(), location.getY(), distance, 0);
	}

    public TileStrategy(final Location location, final int distance, final int accessBlockFlag) {
        this(location.getX(), location.getY(), distance, accessBlockFlag);
    }

	public TileStrategy(final int x, final int y) {
		this(x, y, 0, 0);
	}

	public TileStrategy(final int x, final int y, final int distance, final int accessBlockFlag) {
		super(distance);
		this.x = x;
		this.y = y;
		this.accessBlockFlag = accessBlockFlag;
	}

	@Override
	public boolean canExit(final int currentX, final int currentY, final int sizeXY, final int[][] clip, final int clipBaseX,
			final int clipBaseY) {
		if (distance == 0) {
			return currentX == x && currentY == y;
		}
		
		return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY,
                sizeXY + distance - 1,
				sizeXY + distance - 1, x - clipBaseX, y - clipBaseY, getApproxDestinationSizeX() + distance - 1,
				getApproxDestinationSizeY() + distance - 1, accessBlockFlag);
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
		if (!(other instanceof TileStrategy)) {
			return false;
		}
		final TileStrategy strategy = (TileStrategy) other;
		return x == strategy.x && y == strategy.y;
	}

}
