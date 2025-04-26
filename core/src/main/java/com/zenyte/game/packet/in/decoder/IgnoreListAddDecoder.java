package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.IgnoreListAddEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 feb. 2018 : 16:01:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class IgnoreListAddDecoder implements ClientProtDecoder<IgnoreListAddEvent> {
	@Override
	public IgnoreListAddEvent decode(int opcode, RSBuffer buffer) {
		final String name = buffer.readString();
		return new IgnoreListAddEvent(name);
	}
}
