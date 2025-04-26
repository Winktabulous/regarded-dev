package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ClanKickUserEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:52:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClanKickUserDecoder implements ClientProtDecoder<ClanKickUserEvent> {
	@Override
	public ClanKickUserEvent decode(int opcode, RSBuffer buffer) {
		final String name = buffer.readString();
		return new ClanKickUserEvent(name);
	}
}
