package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:23:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfSetNpcHead implements GamePacketEncoder {
	private final int interfaceId;
	private final int componentId;
	private final int npcId;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Interface: " + interfaceId + ", component: " + componentId + ", npc: " + npcId);
	}

	public IfSetNpcHead(int interfaceId, int componentId, int npcId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.npcId = npcId;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.IF_SETNPCHEAD.gamePacketOut();
		buffer.writeInt(interfaceId << 16 | componentId);
		buffer.writeShortLE128(npcId);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}
}
