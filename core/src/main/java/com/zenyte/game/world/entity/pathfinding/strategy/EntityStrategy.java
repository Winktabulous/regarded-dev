package com.zenyte.game.world.entity.pathfinding.strategy;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.pathfinding.RouteStrategy;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;

public class EntityStrategy extends RouteStrategy {
	/**
	 * The entity moving.
	 */
	protected final Entity entity;
	/**
	 * Entity size.
	 */
	protected final int size;
	/**
	 * Access block flag, see RouteStrategy static final values.
	 */
	protected final int accessBlockFlag;

	public EntityStrategy(final Entity entity) {
		this(entity, 0);
	}

	public EntityStrategy(final Entity entity, final int distance) {
		this(entity, distance, 0);
	}

	public EntityStrategy(final Entity entity, final int distance, final int accessBlockFlag) {
		this(entity, distance, entity.getSize(), accessBlockFlag);
	}

	public EntityStrategy(final Entity entity, final int distance, final int size, final int accessBlockFlag) {
		super(distance);
		this.entity = entity;
		this.size = size;
		this.accessBlockFlag = accessBlockFlag;
	}

	@Override
	public boolean canExit(final int currentX, final int currentY, final int sizeXY, final int[][] clip, final int clipBaseX, final int clipBaseY) {
		final int x = entity.getX();
		final int y = entity.getY();
		if (accessBlockFlag != 0) {
			final int xDistance = accessBlockFlag == RouteEvent.SOUTH_EXIT || accessBlockFlag == RouteEvent.NORTH_EXIT ? 0 : distance;
			final int yDistance = accessBlockFlag == RouteEvent.EAST_EXIT || accessBlockFlag == RouteEvent.WEST_EXIT ? 0 : distance;
			return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY + xDistance, sizeXY + yDistance, x - clipBaseX, y - clipBaseY, getApproxDestinationSizeX() + xDistance, getApproxDestinationSizeY() + yDistance, accessBlockFlag);
		}
		return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY + distance, sizeXY + distance, x - clipBaseX, y - clipBaseY, getApproxDestinationSizeX() + distance, getApproxDestinationSizeY() + distance, accessBlockFlag);
	}

	@Override
	public int getApproxDestinationX() {
		return entity.getX();
	}

	@Override
	public int getApproxDestinationY() {
		return entity.getY();
	}

	@Override
	public int getApproxDestinationSizeX() {
		return size;
	}

	@Override
	public int getApproxDestinationSizeY() {
		return size;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof EntityStrategy)) {
			return false;
		}
		final EntityStrategy strategy = (EntityStrategy) other;
		return entity.getX() == strategy.entity.getX() && entity.getY() == strategy.entity.getY() && size == strategy.size && accessBlockFlag == strategy.accessBlockFlag;
	}

	public Entity getEntity() {
		return entity;
	}
}
