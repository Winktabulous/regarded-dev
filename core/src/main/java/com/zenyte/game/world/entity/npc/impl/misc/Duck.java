package com.zenyte.game.world.entity.npc.impl.misc;

import com.zenyte.ContentConstants;
import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.WalkStep;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 12. dets 2017 : 22:55.10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Duck extends NPC implements Spawnable {
	private static final ForceTalk[] MESSAGES = new ForceTalk[] {new ForceTalk("Quack!"), new ForceTalk("Qwack!")};

	public Duck(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	private long chatDelay;

	@Override
	public void processNPC() {
		if (radius <= 0 || ContentConstants.SPAWN_MODE) {
			return;
		}
		if (!combat.process() && getRadius() > 0 && getForceWalk() == null && getWalkSteps().isEmpty() && Utils.random(5) == 0) {
			final int moveX = Math.round((float) Utils.randomDouble() * Utils.random(-radius, radius));
			final int moveY = Math.round((float) Utils.randomDouble() * Utils.random(-radius, radius));
			addWalkStepsInteract(getRespawnTile().getX() + moveX, getRespawnTile().getY() + moveY, getRadius(), getSize(), true);
		} else if (getForceWalk() != null) {
			if (getLocation().getPositionHash() == getForceWalk().getPositionHash()) {
				setForceWalk(null);
			} else if (getWalkSteps().size() == 0) {
				this.addWalkSteps(getForceWalk().getX(), getForceWalk().getY(), getSize(), true);
			}
		}
		if (chatDelay < Utils.currentTimeMillis() && Utils.random(100) == 0) {
			chatDelay = Utils.currentTimeMillis() + 5000;
			setForceTalk(MESSAGES[Utils.random(MESSAGES.length - 1)]);
		}
	}

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		final int dir = DirectionUtil.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}
		final int offsetX = Utils.DIRECTION_DELTA_X[dir];
		final int offsetY = Utils.DIRECTION_DELTA_Y[dir];
		if (check && !canMove(getX() + offsetX, getY() + offsetY)) {
			return false;
		}
		getWalkSteps().enqueue(WalkStep.getHash(dir, nextX, nextY, check));
		return true;
	}

	private final boolean canMove(final int x, final int y) {
		final int mask = World.getMask(getPlane(), x, y);
		return mask == 262144;
	}

	@Override
	public void processMovement() {
		if (getFaceEntity() >= 0) {
			final Entity target = getFaceEntity() >= 32768 ? World.getPlayers().get(getFaceEntity() - 32768) : World.getNPCs().get(getFaceEntity());
			if (target != null) {
				setDirection(DirectionUtil.getFaceDirection(target.getLocation().getCoordFaceX(target.getSize()) - getX(), target.getLocation().getCoordFaceY(target.getSize()) - getY()));
			}
		}
		if (getNextLocation() != null) {
			if (lastLocation == null) {
				lastLocation = new Location(location);
			} else {
				lastLocation.setLocation(location);
			}
			forceLocation(getNextLocation());
			setLocation(null);
			setTeleported(true);
			World.updateEntityChunk(this, false);
			resetWalkSteps();
			return;
		}
		setRunDirection(-1);
		setWalkDirection(-1);
		if (getWalkSteps().isEmpty() || isLocked() && getEntityType() == EntityType.NPC) {
			return;
		}
		if (lastLocation == null) {
			lastLocation = new Location(location);
		} else {
			lastLocation.setLocation(location);
		}
		setTeleported(false);
		final int steps = isRun() ? 2 : 1;
		for (int stepCount = 0; stepCount < steps; stepCount++) {
			final int nextStep = getNextWalkStep();
			if (nextStep == 0) {
				break;
			}
			final int dir = WalkStep.getDirection(nextStep);
			final int offsetX = Utils.DIRECTION_DELTA_X[dir];
			final int offsetY = Utils.DIRECTION_DELTA_Y[dir];
			if ((WalkStep.check(nextStep) && !canMove(getX() + offsetX, getY() + offsetY))) {
				resetWalkSteps();
				break;
			}
			if (stepCount == 0) {
				setWalkDirection(dir);
			} else {
				setRunDirection(dir);
			}
			getUpdateFlags().flag(UpdateFlag.MOVEMENT_TYPE);
			getLocation().moveLocation(Utils.DIRECTION_DELTA_X[dir], Utils.DIRECTION_DELTA_Y[dir], 0);
		}
		World.updateEntityChunk(this, false);
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			player.getAchievementDiaries().update(FaladorDiary.KILL_A_DUCK);
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return (name.equals("duck") || name.equals("drake")) && id != NpcId.DRAKE_8612 && id != NpcId.DRAKE_8613;
	}
}
