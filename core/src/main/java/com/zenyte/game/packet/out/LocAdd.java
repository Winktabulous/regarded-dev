package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:28:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class LocAdd implements GamePacketEncoder {
	private final WorldObject object;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Id: " + object.getId() + ", type: " + object.getType() + ", rotation: " + object.getRotation() + ", x: " + object.getX() + ", y: " + object.getY() + ", z: " + object.getPlane());
	}

	public LocAdd(WorldObject object) {
		this.object = object;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.LOC_ADD_CHANGE.gamePacketOut();
		final int objectX = object.getX();
		final int objectY = object.getY();
		final int targetLocalX = objectX - ((objectX >> 3) << 3);
		final int targetLocalY = objectY - ((objectY >> 3) << 3);
		final int offsetHash = (targetLocalX & 7) << 4 | (targetLocalY & 7);
		buffer.writeByte128(31);//op flags
		buffer.writeShortLE(object.getId());
		buffer.writeByteC(offsetHash);
		buffer.writeByte((object.getType() << 2) | object.getRotation());
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}
}
