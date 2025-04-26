package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:20:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfSetAnim implements GamePacketEncoder {
	private final int interfaceId;
	private final int componentId;
	private final int animationId;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Interface: " + interfaceId + ", component: " + componentId + ", animation: " + animationId);
	}

	public IfSetAnim(int interfaceId, int componentId, int animationId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.animationId = animationId;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.IF_SETANIM.gamePacketOut();
		buffer.writeShortLE(animationId);
		buffer.writeIntME(interfaceId << 16 | componentId);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}
}
