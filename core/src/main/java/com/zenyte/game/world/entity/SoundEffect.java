package com.zenyte.game.world.entity;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 26. veebr 2018 : 2:03.20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SoundEffect {
	public SoundEffect(final int id) {
		this(id, 1, 0);
	}

	public SoundEffect(final int id, final int radius) {
		this(id, radius, 0);
	}

	public SoundEffect(final int id, final int radius, final int delay) {
		this(id, radius, delay, 1);
	}

	public SoundEffect(final int id, final int radius, final int delay, final int repetitions) {
		this.id = id;
		this.delay = delay;
		this.radius = radius;
		this.repetitions = repetitions;
	}

	public SoundEffect withDelay(final int delay) {
		return new SoundEffect(id, radius, delay, repetitions);
	}

	private final int id;
	private final int delay;
	private final int radius;
	private final int repetitions;
	private static final Int2ObjectMap<SoundEffect> cachedLocalSounds = new Int2ObjectOpenHashMap<>(2000);

	public static final SoundEffect get(final int id) {
		SoundEffect cached = cachedLocalSounds.get(id);
		if (cached == null) {
			cached = new SoundEffect(id);
			cachedLocalSounds.put(id, cached);
		}
		return cached;
	}

	public void sendGlobal(@NotNull final Location location) {
		World.sendSoundEffect(location, this);
	}

	public void sendGlobal(@NotNull final Projectile projectile, @NotNull final Position startPosition, @NotNull final Position endPosition) {
		final int preciseDelay = projectile.getProjectileDuration(startPosition, endPosition);
		World.sendSoundEffect(endPosition, new SoundEffect(id, radius, preciseDelay, repetitions));
	}

	public void sendLocal(@NotNull final Entity target) {
		if (target instanceof Player) {
			((Player) target).sendSound(this);
		}
	}

	public void sendLocal(@NotNull final Entity target, @NotNull final Projectile projectile, @NotNull final Position startPosition, @NotNull final Position endPosition) {
		if (target instanceof Player) {
			final int preciseDelay = projectile.getProjectileDuration(startPosition, endPosition);
			((Player) target).sendSound(new SoundEffect(id, radius, preciseDelay, repetitions));
		}
	}

	public int getId() {
		return id;
	}

	public int getDelay() {
		return delay;
	}

	public int getRadius() {
		return radius;
	}

	public int getRepetitions() {
		return repetitions;
	}
}
