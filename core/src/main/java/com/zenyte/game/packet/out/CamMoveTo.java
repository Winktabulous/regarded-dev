package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

public class CamMoveTo implements GamePacketEncoder {
	private final int localX;
	private final int localY;
	private final int cameraHeight;
	private final int speed;
	private final int acceleration;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "X: " + localX + ", y: " + localY + ", height: " + cameraHeight + ", speed: " + speed + ", acceleration: " + acceleration);
	}

	public CamMoveTo(int localX, int localY, int cameraHeight, int speed, int acceleration) {
		this.localX = localX;
		this.localY = localY;
		this.cameraHeight = cameraHeight;
		this.speed = speed;
		this.acceleration = acceleration;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.CAM_MOVETO.gamePacketOut();
		buffer.writeByte(localX);
		buffer.writeByte(localY);
		buffer.writeShort(cameraHeight);
		buffer.writeByte(speed);
		buffer.writeByte(acceleration);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.HIGH_PACKET;
	}
}
