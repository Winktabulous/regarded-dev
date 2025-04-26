package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 4. dets 2017 : 13:45.09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CamLookAt implements GamePacketEncoder {
	private final int viewLocalX;
	private final int viewLocalY;
	private final int cameraHeight;
	private final int speed;
	private final int acceleration;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "X: " + viewLocalX + ", y: " + viewLocalY + ", height: " + cameraHeight + ", speed: " + speed + ", acceleration: " + acceleration);
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.CAM_LOOKAT.gamePacketOut();
		buffer.writeByte(viewLocalX);
		buffer.writeByte(viewLocalY);
		buffer.writeShort(cameraHeight);
		buffer.writeByte(speed);
		buffer.writeByte(acceleration);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.HIGH_PACKET;
	}

	public CamLookAt(int viewLocalX, int viewLocalY, int cameraHeight, int speed, int acceleration) {
		this.viewLocalX = viewLocalX;
		this.viewLocalY = viewLocalY;
		this.cameraHeight = cameraHeight;
		this.speed = speed;
		this.acceleration = acceleration;
	}
}
