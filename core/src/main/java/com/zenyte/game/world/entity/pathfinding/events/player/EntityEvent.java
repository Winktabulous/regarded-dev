package com.zenyte.game.world.entity.pathfinding.events.player;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 14. juuli 2018 : 00:11:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class EntityEvent extends RouteEvent<Player, EntityStrategy> {
	public EntityEvent(final Player entity, final EntityStrategy strategy, final boolean interruptUponApproach) {
		super(entity, strategy, null, 0);
		this.interruptUponApproach = interruptUponApproach;
	}

	public EntityEvent(final Player entity, final EntityStrategy strategy, final Runnable event, final boolean interruptUponApproach) {
		super(entity, strategy, event, 0);
		this.interruptUponApproach = interruptUponApproach;
	}

	private Location lastTile;
	protected boolean interruptUponApproach;

	@Override
	public boolean process() {
		if (entity.isTeleported()) {
			return STOP;
		}
		final Entity target = strategy.getEntity();
		if (entity.getFaceEntity() < 0) {
			if (entity.getLocation().withinDistance(target, 15)) {
				entity.setFaceEntity(target);
			}
		} else {
			if (!entity.getLocation().withinDistance(target, 15)) {
				entity.setFaceEntity(null);
			}
		}
		if (!initiated || !strategy.getEntity().getLocation().matches(lastTile)) {
			lastTile = new Location(target.getLocation());
			entity.getPacketDispatcher().sendMapFlag(entity.getXInScene(lastTile), entity.getYInScene(lastTile));
			return initiate();
		}
		if (!entity.hasWalkSteps()) {
			final RouteResult steps = RouteFinder.findRoute(entity, entity.getSize(), strategy, true);
			if (interruptUponApproach && calculateBufferDistance(steps) <= 4) {
				if (target instanceof NPC && ((NPC) target).isPathfindingEventAffected()) {
					target.freeze(entity.isRun() ? 1 : 2);
				}
			}
			if (steps == RouteResult.ILLEGAL) {
				cancel();
				return STOP;
			}
			if (!steps.isAlternative() && steps.getSteps() <= 0) {
				if (delay > 0) {
					delay--;
					return CONTINUE;
				}
				resetFlag();
				execute();
				return STOP;
			}
			cancel();
			return STOP;
		}
		return CONTINUE;
	}

	@Override
	protected boolean initiate() {
		initiated = true;
		final RouteResult steps = RouteFinder.findRoute(entity, entity.getSize(), strategy, true);
		if (interruptUponApproach && calculateBufferDistance(steps) <= 4) {
			final Entity target = strategy.getEntity();
			if (target instanceof NPC && ((NPC) target).isPathfindingEventAffected()) {
				target.freeze(entity.isRun() ? 2 : 4);
			}
		}
		if (steps == RouteResult.ILLEGAL) {
			cancel();
			return STOP;
		}
		if (!steps.isAlternative() && steps.getSteps() == 0) {
			if (delay > 0) {
				delay--;
				return CONTINUE;
			}
			resetFlag();
			execute();
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

	private final int calculateBufferDistance(final RouteResult steps) {
		int x = entity.getX();
		int y = entity.getY();
		int dx = 0;
		int dy = 0;
		final int[] bufferX = steps.getXBuffer();
		final int[] bufferY = steps.getYBuffer();
		for (int step = steps.getSteps() - 1; step >= 0; step--) {
			final int $x = bufferX[step];
			final int $y = bufferY[step];
			dx += Math.abs($x - x);
			dy += Math.abs($y - y);
			x = $x;
			y = $y;
		}
		return Math.max(dx, dy);
	}

	@Override
	protected void resetFlag() {
		entity.getPacketDispatcher().resetMapFlag();
	}

	@Override
	protected void inform(final String message) {
		entity.sendMessage(message);
	}
}
