package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.BugReportEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 1. apr 2018 : 22:57.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BugReportDecoder implements ClientProtDecoder<BugReportEvent> {
	@Override
	public BugReportEvent decode(int opcode, RSBuffer buffer) {
		final String instructions = buffer.readString();
		final String description = buffer.readString();
		final byte bit = buffer.readByte128();
		return new BugReportEvent(instructions, description, bit);
	}
}
