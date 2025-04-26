package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:21:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class IfSetHide implements GamePacketEncoder {
	private final int interfaceId;
	private final int componentId;
	private final boolean hidden;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Interface: " + interfaceId + ", component: " + componentId + ", hidden: " + hidden);
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.IF_SETHIDE.gamePacketOut();
		buffer.writeInt(interfaceId << 16 | componentId);
		buffer.writeByte128(hidden ? 1 : 0);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

	public IfSetHide(int interfaceId, int componentId, boolean hidden) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.hidden = hidden;
	}
}
