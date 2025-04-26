package com.zenyte.game.content.boss.phantommuspah;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class PhantomOrb extends NPC {

	static final Location[][] SPAWN_LOCATIONS = {
			{new Location(2852, 4256)},
			{new Location(2846, 4261), new Location(2846, 4262)},
			{new Location(2850, 4250), new Location(2848, 4248)},
			{new Location(2850, 4262), new Location(2851, 4261), new Location(2850, 4261)},
			{new Location(2839, 4262), new Location(2837, 4260), new Location(2841, 4256)},
			{new Location(2849, 4257), new Location(2850, 4257), new Location(2849, 4256)},
			{new Location(2845, 4260)}, {},
			{new Location(2852, 4256)},
			{new Location(2845, 4261), new Location(2846, 4261)},
			{new Location(2849, 4253), new Location(2848, 4248)},
			{new Location(2851, 4261), new Location(2850, 4261)},
			{new Location(2837, 4260), new Location(2841, 4262), new Location(2840, 4256)},
			{new Location(2850, 4257), new Location(2849, 4257)},
			{new Location(2845, 4260)}, {},
			{new Location(2852, 4256)},
			{new Location(2846, 4262), new Location(2845, 4261), new Location(2846, 4261)},
			{new Location(2843, 4249), new Location(2850, 4250)}
	};
	private static final Animation EXPLODE_ANIM = new Animation(9948);
	private static final SoundEffect EXPLODE_SOUND = new SoundEffect(6815);

	private static final int ORB_ID = 12083;
	private final PhantomInstance instance;
	private final Direction direction;
	private boolean finish;

	public PhantomOrb(Location tile, PhantomInstance instance, Direction direction) {
		super(ORB_ID, tile, true);
		this.instance = instance;
		this.direction = direction;
		setForceAggressive(false);
	}

	@Override public void processNPC() {
		if ((finish || instance.getPlayers().size() < 1) && !isFinished()) {
			finish();
			return;
		}
		if (instance.getPlayer() != null && instance.getPlayer().getLocation().equals(getLocation())) {
			setAnimation(EXPLODE_ANIM);
			instance.getPlayer().sendSound(EXPLODE_SOUND);
			instance.getPlayer().applyHit(new Hit(this, Utils.random(12, 20), HitType.DEFAULT));
			if (instance.getMuspah() != null) {
				instance.getMuspah().setTakenAvoidableDamage();
			}
			finish = true;
			return;
		}
		if (isLocked()) {
			return;
		}
		final Location nextLoc = getLocation().transform(direction.getOffsetX(), direction.getOffsetY());
		if (instance.isSpikeTileBlocked(nextLoc) || !addWalkSteps(nextLoc.getX(), nextLoc.getY())) {
			setAnimation(EXPLODE_ANIM);
			finish = true;
		}
	}

	@Override public void setRespawnTask() {

	}

	@Override public boolean canAttack(Player source) {
		return false;
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}
}