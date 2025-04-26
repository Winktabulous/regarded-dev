package com.zenyte.game.world.entity.pathfinding.events;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.RouteStrategy;

/**
 * @author Kris | 13. juuli 2018 : 23:16:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class RouteEvent<E extends Entity, S extends RouteStrategy> {
	public static final int BLOCK_FLAG_NORTH = 1;
	public static final int BLOCK_FLAG_EAST = 2;
	public static final int BLOCK_FLAG_SOUTH = 4;
	public static final int BLOCK_FLAG_WEST = 8;
	public static final int NORTH_EXIT = BLOCK_FLAG_EAST | BLOCK_FLAG_SOUTH | BLOCK_FLAG_WEST;
	public static final int EAST_EXIT = BLOCK_FLAG_NORTH | BLOCK_FLAG_SOUTH | BLOCK_FLAG_WEST;
	public static final int SOUTH_EXIT = BLOCK_FLAG_NORTH | BLOCK_FLAG_EAST | BLOCK_FLAG_WEST;
	public static final int WEST_EXIT = BLOCK_FLAG_NORTH | BLOCK_FLAG_EAST | BLOCK_FLAG_SOUTH;
	protected static final boolean CONTINUE = false;
	protected static final boolean STOP = true;

	/**
	 * A protected constructor for event to prevent direct instantiation.
	 * 
	 * @param entity
	 *            the entity who is executing the event.
	 * @param strategy
	 *            the strategy used to execute the event.
	 * @param event
	 *            the runnable associated to the event, executed upon successfully reaching the destination.
	 * @param delay
	 *            the delay in ticks until the event is executed after reaching the destination.
	 */
	protected RouteEvent(final E entity, final S strategy, final Runnable event, final int delay) {
		this.entity = entity;
		this.strategy = strategy;
		this.event = event;
		this.delay = delay;
	}

	/**
	 * Whether the event has begun already or about to start.
	 */
	protected boolean initiated;
	/**
	 * The delay in ticks until the {@link #event } is executed upon successfully reaching the destination.
	 */
	protected int delay;
	/**
	 * The strategy used in this event.
	 */
	protected S strategy;
	/**
	 * The entity executing this event.
	 */
	protected E entity;
	/**
	 * The event that's being executed upon successfully reaching the destination, after the {@link #delay } is up.
	 */
	protected Runnable event;
	/**
	 * The event that's being executed upon failure.
	 */
	protected Runnable onFailure;

	public RouteEvent setOnFailure(final Runnable runnable) {
		this.onFailure = runnable;
		return this;
	}

	/**
	 * The process method called every tick, used to execute the event.
	 * 
	 * @return whether to continue processing this event, or to stop it from executing any further.
	 */
	public boolean process() {
		if (entity.isTeleported()) {
			return STOP;
		}
		if (!initiated) {
			return initiate();
		}
		if (!entity.hasWalkSteps()) {
			final RouteResult steps = RouteFinder.findRoute(entity, entity.getSize(), strategy, true);
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

	/**
	 * The initiation method for this event, attempts to move the {@link #entity} to the destination.
	 * 
	 * @return whether to continue executing this event, or to finish it.
	 */
	protected boolean initiate() {
		initiated = true;
		final RouteResult steps = RouteFinder.findRoute(entity, entity.getSize(), strategy, true);
		if (steps == RouteResult.ILLEGAL) {
			cancel();
			return STOP;
		}
		if (!steps.isAlternative() && steps.getSteps() == 0) {
			resetFlag();
			execute();
			return STOP;
		}
		final int[] bufferX = steps.getXBuffer();
		final int[] bufferY = steps.getYBuffer();
		entity.resetWalkSteps();
		if (cannotMove(entity))
			return CONTINUE;

		for (int step = steps.getSteps() - 1; step >= 0; step--) {
			if (!entity.addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
				cancel();
				return STOP;
			}
		}
		return CONTINUE;
	}

	protected boolean cannotMove(Entity entity) {
		return entity.isFrozen() || entity.isStunned();
	}

	/**
	 * Upon failing to reach the destination, the character is informed of the failure, and the minimap flag is reset.
	 */
	protected void cancel() {
		inform("You can't reach that.");
		resetFlag();
		if (entity.getFaceEntity() >= 0) {
			entity.setFaceEntity(null);
		}
		if (onFailure != null) {
			onFailure.run();
		}
	}

	/**
	 * Upon successfully reaching the destination, the event, if exists, is executed.
	 */
	protected void execute() {
		if (event != null) {
			event.run();
		}
	}

	/**
	 * Resets the character's minimap flag.
	 */
	protected abstract void resetFlag();

	/**
	 * Informs the character about the event.
	 *
	 * @param message the message that's sent to the player.
	 */
	protected abstract void inform(final String message);

	public void setEvent(Runnable event) {
		this.event = event;
	}

	public Runnable getOnFailure() {
		return onFailure;
	}
}
