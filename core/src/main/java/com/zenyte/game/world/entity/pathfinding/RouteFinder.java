package com.zenyte.game.world.entity.pathfinding;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;

/**
 * Route finder, designed for single-threaded usage.
 * 
 * @author Mangis
 */
public class RouteFinder {
	/**
	 * Find's route using given strategy. Returns amount of steps found. If
	 * steps > 0, route exists. If steps = 0, route exists, but no need to move.
	 * If steps < 0, route does not exist.
	 */
	public static RouteResult findRoute(final int srcX, final int srcY, final int srcZ, final int srcSizeXY, final RouteStrategy strategy, final boolean findAlternative) {
		return WalkRouteFinder.findRoute(srcX, srcY, srcZ, srcSizeXY, strategy, findAlternative);
	}

	/**
	 * Find's route using given strategy. Returns amount of steps found. If
	 * steps > 0, route exists. If steps = 0, route exists, but no need to move.
	 * If steps < 0, route does not exist.
	 */
	public static RouteResult findRoute(final Position position, final int srcSizeXY, final RouteStrategy strategy, final boolean findAlternative) {
		final Location tile = position.getPosition();
		return WalkRouteFinder.findRoute(tile.getX(), tile.getY(), tile.getPlane(), srcSizeXY, strategy, findAlternative);
	}

	public static RouteResult findConditionalRoute(final Position position, final int srcSizeXY, final RouteStrategy strategy, final boolean findAlternative) {
		final Location tile = position.getPosition();
		return ConditionalWalkRouteFinder.findRoute(tile.getX(), tile.getY(), tile.getPlane(), srcSizeXY, strategy, findAlternative);
	}
}
