package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.CommandEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:28:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class CommandDecoder implements ClientProtDecoder<CommandEvent> {
	@Override
	public CommandEvent decode(final int opcode, final RSBuffer buffer) {
		final String command = buffer.readString();
		return new CommandEvent(command.toLowerCase());
	}
}
