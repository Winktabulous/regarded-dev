package com.zenyte.game.packet.out;

import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 15:14:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class IfOpenTop implements GamePacketEncoder {

	private final PaneType pane;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Pane: " + pane.getId() + ", name: " + pane.name());
	}

	public IfOpenTop(PaneType pane) {
		this.pane = pane;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.IF_OPENTOP.gamePacketOut();
		buffer.writeShortLE128(pane.getId());
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.HIGH_PACKET;
	}

}
