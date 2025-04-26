package com.zenyte.game.world.entity.pathfinding.events.player;

import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.RouteStrategy;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.FloorItemStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 14. juuli 2018 : 03:01:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class FloorItemEvent extends RouteEvent<Player, FloorItemStrategy> {
	public FloorItemEvent(final Player entity, final FloorItemStrategy strategy, final Runnable event) {
		this(entity, strategy, event, 0);
	}

	public FloorItemEvent(final Player entity, final FloorItemStrategy strategy, final Runnable event, final int delay) {
		super(entity, strategy, event, delay);
		strategies = new RouteStrategy[] {new TileStrategy(strategy.getX(), strategy.getY()), strategy};
	}

	private final RouteStrategy[] strategies;

	@Override
	protected void resetFlag() {
		entity.getPacketDispatcher().resetMapFlag();
	}

	@Override
	protected void inform(final String message) {
		entity.sendMessage(message);
	}

	private boolean withinDistance() {
		final int x = strategy.getX();
		final int y = strategy.getY();
		final int distanceX = entity.getX() - x;
		final int distanceY = entity.getY() - y;
		return !(distanceX > 1 || distanceY > 1 || distanceX < -1 || distanceY < -1);
	}

	@Override
	public boolean process() {
		if (entity.isTeleported()) {
			return STOP;
		}
		if (!initiated) {
			return initiate();
		}
		if (!entity.hasWalkSteps()) {
			for (final RouteStrategy strategy : strategies) {
				final RouteResult steps = RouteFinder.findRoute(entity, entity.getSize(), strategy, true);
				if (steps == RouteResult.ILLEGAL) {
					continue;
				}
				if (!steps.isAlternative() && steps.getSteps() <= 0) {
					resetFlag();
					if (delay > 0) {
						delay--;
						return CONTINUE;
					}
					if (withinDistance()) execute();
					 else cancel();
					return STOP;
				}
			}
			cancel();
			return STOP;
		}
		return CONTINUE;
	}

	@Override
	protected boolean initiate() {
		initiated = true;
		for (final RouteStrategy strategy : strategies) {
			final RouteResult steps = RouteFinder.findRoute(entity, entity.getSize(), strategy, true);
			if (steps == RouteResult.ILLEGAL) {
				continue;
			}
			if (steps.getSteps() == 0) {
				resetFlag();
				if (delay > 0) {
					delay--;
					return CONTINUE;
				}
				if (withinDistance()) execute();
				 else cancel();
				return STOP;
			}
			final int[] bufferX = steps.getXBuffer();
			final int[] bufferY = steps.getYBuffer();
			entity.resetWalkSteps();
			if (entity.isFrozen() || entity.isStunned()) {
				return CONTINUE;
			}
			for (int step = steps.getSteps() - 1; step >= 0; step--) {
				if (!entity.addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
					cancel();
					return STOP;
				}
			}
			return CONTINUE;
		}
		cancel();
		return STOP;
	}
}
