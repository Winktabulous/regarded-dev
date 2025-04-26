package com.zenyte.game.world.entity.player.cutscene.actions;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 4. dets 2017 : 1:47.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CameraPositionAction implements Runnable {

	public CameraPositionAction(final Player player, final Location tile, final int cameraHeight, final int speed, final int acceleration) {
		this.player = player;
		this.tile = tile;
		this.cameraHeight = cameraHeight;
		this.speed = speed;
		this.acceleration = acceleration;
	}
	
	private final Player player;
	private final Location tile;
	private final int cameraHeight;
	private final int speed, acceleration;
	
	@Override
	public void run() {
		final Location lastLoaded = player.getLastLoadedMapRegionTile();
		player.getPacketDispatcher().sendCameraPosition(tile.getLocalX(lastLoaded), tile.getLocalY(lastLoaded), cameraHeight, speed, acceleration);
	}
}
