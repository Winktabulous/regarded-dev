package com.zenyte.game.world;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 14. okt 2017 : 10:35.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class Projectile {
	/**
	 * Right angle in degrees.
	 */
	private static final int RIGHT_ANGLE = 90;
	/**
	 * The right angle in client.
	 */
	private static final int CLIENT_RIGHT_ANGLE = 64;
	/**
	 * The ratio used to modify projectile angle for the client.
	 */
	private static final float ANGLE_RATIO = (float) RIGHT_ANGLE / CLIENT_RIGHT_ANGLE;
	/**
	 * The default values for projectiles.
	 */
	private static final int DEFAULT_SLOPE = 0;
	private static final int DEFAULT_OFFSET = 0;
	private static final int DEFAULT_SPEED = 10;
	private static final int DEFAULT_DELAY = 0;
	/**
	 * The id of the graphics that's going to be shot as a projectile.
	 */
	private final int graphicsId;
	/**
	 * The starting height of the projectile.
	 */
	private final int startHeight;
	/**
	 * The ending height of the projectile.
	 */
	private final int endHeight;
	/**
	 * The angle of the projectile, will be converted from actual degrees to units client reads. {value can be from 0-90, including both extremes.}
	 */
	private final int angle;
	/**
	 * The offset in distance. The projectile will be moved closer towards the end by input value. {value 64 is
	 * equal to one tile distance.}
	 */
	private final int distanceOffset;
	/**
	 * The multiplier used for adjusting projectile speed per tile distance basis.
	 */
	private final int multiplier;
	/**
	 * The delay before projectile starts flying, uses {@link GameConstants#CLIENT_CYCLE} as units.
	 */
	private int delay;
	/**
	 * The duration of the projectile that will be added or subtracted from the original flight duration of the projectile which is calculated from distance and delay.
	 */
	private final int duration;

	public Projectile(final int graphicsId, final int startHeight, final int endHeight, final int delay, final int angle, final int duration, final int distanceOffset, final int multiplier) {
		this.graphicsId = graphicsId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.delay = delay;
		this.angle = angle > 0 ? (Math.round(Math.min(RIGHT_ANGLE, angle) / ANGLE_RATIO)) : (Byte.MAX_VALUE - Math.round(-Math.min(RIGHT_ANGLE, angle) / ANGLE_RATIO));
		this.duration = duration;
		this.distanceOffset = distanceOffset;
		this.multiplier = multiplier;
	}

	public Projectile(final Projectile projectile) {
		this.graphicsId = projectile.graphicsId;
		this.startHeight = projectile.startHeight;
		this.endHeight = projectile.endHeight;
		this.delay = projectile.delay;
		this.angle = projectile.angle;
		this.duration = projectile.duration;
		this.distanceOffset = projectile.distanceOffset;
		this.multiplier = projectile.multiplier;
	}

	public Projectile(final Projectile projectile, final int duration) {
		this.graphicsId = projectile.graphicsId;
		this.startHeight = projectile.startHeight;
		this.endHeight = projectile.endHeight;
		this.delay = projectile.delay;
		this.angle = projectile.angle;
		this.duration = duration;
		this.distanceOffset = projectile.distanceOffset;
		this.multiplier = projectile.multiplier;
	}

	public Projectile(final int graphicsId, final int startHeight, final int endHeight, final int duration, final int delay) {
		this(graphicsId, startHeight, endHeight, delay, DEFAULT_SLOPE, duration, DEFAULT_OFFSET, 5);
	}

	public Projectile(final int graphicsId, final int startHeight, final int endHeight, final int delay) {
		this(graphicsId, startHeight, endHeight, delay, DEFAULT_SLOPE, DEFAULT_SPEED, DEFAULT_OFFSET, 5);
	}

	public Projectile(final int graphicsId, final int startHeight, final int endHeight) {
		this(graphicsId, startHeight, endHeight, DEFAULT_DELAY, DEFAULT_SLOPE, DEFAULT_SPEED, DEFAULT_OFFSET, 5);
	}

	/**
	 * Gets the duration of the projectile in ticks, rounded.
	 * 
	 * @param from
	 *            tile from where the projectile is sent.
	 * @param to
	 *            tile to which the projectile flies.
	 * @return 
	 * 			duration in ticks, rounded (>= .5 -> 1; < .5 -> 0)
	 */
	public int getTime(final Location from, final Location to) {
		float duration = getProjectileDuration(from, to) / GameConstants.CYCLES_PER_TICK;
		return Math.max(0, (int) duration - 1);
	}

	public int getTime(final Location from, final Entity to) {
		return getTime(from, to.getLocation());
	}

	public int getTime(final Entity from, final Location to) {
		return getTime(from.getMiddleLocation(), to);
	}

	public int getTime(final Entity from, final Entity to) {
		return getTime(from.getMiddleLocation(), to.getLocation());
	}

	/**
	 * Gets the flight duration of the projectile in client cycles.
	 * 
	 * @param from
	 *            where the projectile is sent.
	 * @param to
	 *            where the projectile flies.
	 * @return 
	 * 			the duration of the projectile in client cycles.
	 */
	public int getProjectileDuration(final Position from, final Position to) {
		final Location fromLocation = from.getPosition();
		final Location target = to instanceof Entity ? ((Entity) to).getMiddleLocation() : to.getPosition();
		final int flightDuration = Math.max(Math.abs(fromLocation.getX() - target.getX()), Math.abs(fromLocation.getY() - target.getY())) * multiplier;
		return delay + duration + flightDuration;
	}

	public int getTime(final int distance) {
		final int flightDuration = distance * multiplier;
		final int length = delay + duration + flightDuration;
		return length / 30;
	}

	/**
	 * Calculates the duration of the projectile from the input amount of ticks. 
	 * The value is unrestricted, so if the delay of the projectile ends up exceeding the new duration of the projectile, 
	 * projectile itself will never actually be visible.
	 *
	 * @param ticks
	 * 				the duration in ticks the projectile should last for.
	 * @return
	 *                the duration in client cycles the projectile will last for.
	 */
	public int getProjectileDurationByDistance(final int ticks) {
		return (int) (ticks * GameConstants.CYCLES_PER_TICK);
	}

	public int getGraphicsId() {
		return graphicsId;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getAngle() {
		return angle;
	}

	public int getDuration() {
		return duration;
	}

	public int getDistanceOffset() {
		return distanceOffset;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public int build(final Position shooter, final Position receiver) {
		return World.sendProjectile(shooter, receiver, this);
	}
}
