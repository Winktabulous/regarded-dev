package com.zenyte.game.world.entity.masks;

import mgi.types.config.AnimationDefinitions;

/**
 * @author Kris | 6. nov 2017 : 14:25.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Animation {
	private final int id;
	private final int delay;
	public static final Animation STOP = new Animation(-1);
	public static final Animation JUMP = new Animation(741);

	public static final Animation STOMP = new Animation(4278);
	public static final Animation LEAP = new Animation(2586);
	public static final Animation LAND = new Animation(2588);
	public static final Animation CUT_WATERMELON = new Animation(2269);
	public static final Animation SMITH = new Animation(3243);
	public static final Animation SCORCHING_BOW_FLETCH = new Animation(6689);
	public static final Animation LADDER_UP = new Animation(828);
	public static final Animation LADDER_DOWN = new Animation(827);
	public static final Animation GRAB = new Animation(832);
	public static final Animation KNOCKBACK = new Animation(1157);
	public static final Animation CRAWL = new Animation(2796);

	public Animation(final int id) {
		this(id, 0);
	}

	public Animation(final int id, final int delay) {
		this.id = id;
		this.delay = delay;
	}

	public int getId() {
		return id;
	}

	public int getDelay() {
		return delay;
	}

	public final int getDuration() {
		final AnimationDefinitions defs = AnimationDefinitions.get(id);
		if (defs == null) {
			return 0;
		}
		return defs.getDuration() + (20 * delay);
	}

	public final int getCeiledDuration() {
		final AnimationDefinitions defs = AnimationDefinitions.get(id);
		if (defs == null) {
			return 0;
		}
		final int duration = defs.getDuration() + (20 * delay);
		final float remainder = duration % 600;
		if (remainder > 0) {
			return (int) (duration + (600 - remainder));
		}
		return duration;
	}

	@Override
	public String toString() {
		return "Animation(id=" + this.getId() + ", delay=" + this.getDelay() + ")";
	}

}
