package com.zenyte.game.world.entity.player.cutscene.actions;

import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 4. dets 2017 : 1:48.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CameraShakeAction implements Runnable {
	
	public CameraShakeAction(final Player player, final CameraShakeType type, final byte shakeIntensity, final byte movementIntensity, final byte speed) {
		this.player = player;
		this.type = type;
		this.shakeIntensity = shakeIntensity;
		this.movementIntensity = movementIntensity;
		this.speed = speed;

	}
	
	private final Player player;
	
	/**
	 * The type of the shaking.
	 */
	private final CameraShakeType type;
	
	/**
	 * The intensity of the low-range shaking, 
	 * basically intensity in general.
	 */
	private final byte shakeIntensity;
	
	/**
	 * The intensity of the movement of the camera,
	 * basically how fast the camera itself should move in the specified
	 * directions (based on the shake type).
	 */
	private final byte movementIntensity;
	
	/**
	 * The speed of the whole camera movement in general.
	 */
	private final byte speed;
	
	@Override
	public void run() {
		player.getPacketDispatcher().sendCameraShake(type, shakeIntensity, movementIntensity, speed);
	}

}
