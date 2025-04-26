package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 14. apr 2018 : 15:03.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MidiJingle implements GamePacketEncoder {
	private final int trackId;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Song: " + trackId);
	}

	public MidiJingle(int trackId) {
		this.trackId = trackId;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.MIDI_JINGLE.gamePacketOut();
		buffer.writeMedium(0);
		buffer.writeShort128(trackId);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}
}
