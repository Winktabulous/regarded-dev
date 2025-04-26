package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:34:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class MessageGame implements GamePacketEncoder {
	private final String message;
	private final String extension;
	private final MessageType type;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Type: " + type + ", message: " + message + (extension != null ? (", extension: " + extension) : ""));
	}

	public MessageGame(final String message, final MessageType type, final String extension) {
		this.message = message;
		this.type = type;
		this.extension = extension;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.MESSAGE_GAME.gamePacketOut();
		buffer.writeSmart(type.getType());
		buffer.writeByte(extension != null ? 1 : 0);
		if (extension != null) {
			buffer.writeString(extension);
		}
		buffer.writeString(message);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.HIGH_PACKET;
	}

}
