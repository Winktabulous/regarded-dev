package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:50:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class SetMapFlag implements GamePacketEncoder {

	private final int x;
	private final int y;

	@Override
	public void log(@NotNull final Player player) {
		this.log(player, "X: " + x + ", y: " + y);
	}

	public SetMapFlag(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.SET_MAP_FLAG.gamePacketOut();
		buffer.writeByte(x);
		buffer.writeByte(y);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

}
