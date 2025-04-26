package com.zenyte.game.world.entity.masks;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.PrivateMask;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 6. nov 2017 : 14:30.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public class Graphics extends PrivateMask {
	public static final Graphics RESET = new Graphics(-1);
	private final int id;
	private final int delay;
	private final int height;

	public Graphics(int id) {
		this(id, 0, 0);
	}

	public Graphics(int id, int delay, int height) {
		this.id = id;
		this.delay = delay;
		this.height = height;
	}

	public void sendDelayed(@NotNull final Projectile projectile, @NotNull final Position startPosition, @NotNull final Position endPosition) {
		final int preciseDelay = projectile.getProjectileDuration(startPosition, endPosition);
		World.sendGraphics(new Graphics(id, preciseDelay, height), endPosition.getPosition());
	}

	public int getId() {
		return id;
	}

	public int getDelay() {
		return delay;
	}

	public int getHeight() {
		return height;
	}

}
