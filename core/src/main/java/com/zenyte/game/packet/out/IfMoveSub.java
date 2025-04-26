package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 16:09:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfMoveSub implements GamePacketEncoder {

	private final int fromPaneId;
	private final int fromPaneChildId;
	private final int toPaneId;
	private final int toPaneChildId;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Pane: " + fromPaneId + " -> " + toPaneId + ", child: " + fromPaneChildId + " -> " + toPaneChildId);
	}

	public IfMoveSub(int fromPaneId, int fromPaneChildId, int toPaneId, int toPaneChildId) {
		this.fromPaneId = fromPaneId;
		this.fromPaneChildId = fromPaneChildId;
		this.toPaneId = toPaneId;
		this.toPaneChildId = toPaneChildId;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.IF_MOVESUB.gamePacketOut();
		buffer.writeIntIME(fromPaneId << 16 | fromPaneChildId);
		buffer.writeIntME(toPaneId << 16 | toPaneChildId);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

}
