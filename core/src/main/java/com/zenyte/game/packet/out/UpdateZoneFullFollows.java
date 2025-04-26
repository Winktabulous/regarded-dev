package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.GamePacketEncoderMode;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:41:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class UpdateZoneFullFollows implements GamePacketEncoder {

	private final int chunkX;
	private final int chunkY;

	@Override
	public void log(@NotNull final Player player) {
		this.log(player, "X: " + chunkX + ", y: " + chunkY);
	}

	public UpdateZoneFullFollows(int chunkX, int chunkY) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.UPDATE_ZONE_FULL_FOLLOWS.gamePacketOut();
		buffer.writeByte128(chunkX);
		buffer.writeByte(chunkY);
		return buffer;
	}

	@Override
	public @NotNull GamePacketEncoderMode encoderMode() {
		return GamePacketEncoderMode.WRITE_FLUSH;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

}
