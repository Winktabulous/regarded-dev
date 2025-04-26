package com.zenyte.game.content.magiccarpet;

import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;

/**
 * @author Kris | 21. aug 2018 : 12:34:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class MagicCarpet {
	protected final RenderAnimation render = new RenderAnimation(6936, 6936, 6936, 6936, 6936, 6936, 6936);
	protected final Animation onKnees = new Animation(2266);
	protected final Animation fly = new Animation(2262);
	protected final Animation rightTurn = new Animation(2264);
	protected final Animation leftTurn = new Animation(2265);
	protected final Animation land = new Animation(2263);
	protected final Player player;
	protected final boolean forward;

	public abstract CarpetMovement[] getForwardMovement();

	public abstract CarpetMovement[] getBackwardsMovement();

	public abstract Location getStartLocation();

	public abstract Location getStartFaceLocation();

	public abstract Location getStartNextToCarpetLocation();

	public abstract Location getEndNextToCarpetLocation();

	public abstract CameraPositionAction getStartCameraPositionAction();

	public abstract CameraPositionAction getEndCameraPositionAction();

	public abstract CameraLookAction getStartCameraLookAction();

	public abstract CameraLookAction getEndCameraLookAction();

	public void travel() {
		player.lock();
		getStartCameraPositionAction().run();
		getStartCameraLookAction().run();
		final TileEvent event = new TileEvent(player, new TileStrategy(getStartNextToCarpetLocation()), () -> {
			final Location dest = getStartLocation();
			player.addWalkSteps(dest.getX(), dest.getY(), 1, false);
			WorldTasksManager.schedule(new TickTask() {
				@Override
				public void run() {
					if (ticks == 0) {
						player.setFaceLocation(getStartFaceLocation());
						player.setAnimation(onKnees);
					} else if (ticks == 1) {
						player.getVarManager().sendBit(1773, true);
						player.setAnimation(fly);
						player.getAppearance().setRenderAnimation(render);
						player.getAppearance().forceAppearance(3, 5614);
					} else if (ticks == 4) {
						execute();
					} else if (ticks == 8) {
						player.getPacketDispatcher().resetCamera();
						stop();
						return;
					}
					ticks++;
				}
			}, 1, 0);
		});
		event.setOnFailure(() -> {
			player.unlock();
			player.sendMessage("Unable to use carpet transportation from here!");
			player.getPacketDispatcher().resetCamera();
		});
		player.setRouteEvent(event);
	}

	private void execute() {
		final CarpetMovement[] movements = forward ? getForwardMovement() : getBackwardsMovement();
		WorldTasksManager.schedule(new WorldTask() {
			private int index;
			private int skipCount;
			@Override
			public void run() {
				if (skipCount > 0) {
					skipCount--;
					return;
				}
				if (index >= movements.length) {
					stop();
					finish();
					return;
				}
				// TODO: Turning animation on the last movement.
				if (movements[index].getSteps() != null) {
					int tempIndex = index;
					while (true) {
						if (tempIndex >= movements.length) {
							break;
						}
						final Location step = movements[tempIndex++].getSteps();
						if (step != null) {
							player.addWalkSteps(step.getX(), step.getY());
							skipCount++;
							index++;
							continue;
						}
						skipCount--;
						return;
					}
				}
				final ForceMovement fm = movements[index].getForceMovement();
				if (fm != null) {
					final Location currentTile = new Location(player.getLocation());
					player.setLocation(fm.getToFirstTile());
					final ForceMovement forceMovement = new ForceMovement(currentTile, 0, fm.getToFirstTile(), fm.getFirstTileTicketDelay(), fm.getDirection());
					player.setForceMovement(forceMovement);
				}
				if (index == movements.length - 2) {
					getEndCameraPositionAction().run();
					getEndCameraLookAction().run();
				}
				index++;
			}
		}, 0, 0);
	}

	public MagicCarpet(Player player, boolean forward) {
		this.player = player;
		this.forward = forward;
	}

	private void finish() {
		WorldTasksManager.schedule(new TickTask() {
			@Override
			public void run() {
				if (ticks == 0) {
					player.setAnimation(land);
				} else if (ticks == 1) {
					player.getPacketDispatcher().resetCamera();
					player.getVarManager().sendBit(1773, false);
					player.getAppearance().resetRenderAnimation();
					player.getAppearance().clearForcedAppearance();
				} else if (ticks == 3) {
					player.setAnimation(Animation.STOP);
					final Location offCarpet = getEndNextToCarpetLocation();
					player.addWalkSteps(offCarpet.getX(), offCarpet.getY(), 1, false);
					player.lock(1);
					stop();
					return;
				}
				ticks++;
			}
		}, 2, 0);
	}
}
