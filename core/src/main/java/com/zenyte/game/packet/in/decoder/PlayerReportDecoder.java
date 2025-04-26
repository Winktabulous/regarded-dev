package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.PlayerReportEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 1. apr 2018 : 22:57.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PlayerReportDecoder implements ClientProtDecoder<PlayerReportEvent> {
	@Override
	public PlayerReportEvent decode(int opcode, RSBuffer buffer) {
		final String name = buffer.readString();
		final byte rule = buffer.readByte();
		final boolean mute = buffer.readByte() == 1;
		return new PlayerReportEvent(name, rule, mute);
	}
}
