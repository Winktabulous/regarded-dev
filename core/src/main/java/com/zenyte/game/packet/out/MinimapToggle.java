package com.zenyte.game.packet.out;

import com.zenyte.game.model.MinimapState;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:12:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class MinimapToggle implements GamePacketEncoder {
	private final MinimapState minimapState;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "State: " + minimapState.name());
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.MINIMAP_TOGGLE.gamePacketOut();
		buffer.writeByte(minimapState.getState());
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

	public MinimapToggle(MinimapState minimapState) {
		this.minimapState = minimapState;
	}
}
