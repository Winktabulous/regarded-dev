package com.zenyte.game.world.entity;

/**
 * @author Kris | 19. apr 2018 : 20:06.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ImmutableLocation extends Location {

	public ImmutableLocation(final Location tile) {
		super(tile);
	}
	
	public ImmutableLocation(final int x, final int y, final int z) {
		super(x, y, z);
	}
	
	@Override
	public final Location moveLocation(final int xOffset, final int yOffset, final int planeOffset) {
		throw new RuntimeException("An immutable tile object cannot be modified.");
	}

	@Override
	public final void setLocation(final int x, final int y, final int plane) {
		throw new RuntimeException("An immutable tile object cannot be modified.");
	}

	@Override
	public final void setLocation(final Location tile) {
		throw new RuntimeException("An immutable tile object cannot be modified.");
	}

}
