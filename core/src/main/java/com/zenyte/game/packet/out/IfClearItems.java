package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 31 mrt. 2018 : 22:14:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfClearItems implements GamePacketEncoder {
	private final int interfaceId;
	private final int componentId;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Interface: " + interfaceId + ", component: " + componentId);
	}

	public IfClearItems(int interfaceId, int componentId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
	}

	@Override
	public LogLevel level() {
		return LogLevel.HIGH_PACKET;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.UPDATE_INV_CLEAR.gamePacketOut();
		buffer.writeIntME(interfaceId << 16 | componentId);
		return buffer;
	}

}
