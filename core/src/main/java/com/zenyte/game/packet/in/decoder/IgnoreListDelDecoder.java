package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.IgnoreListDelEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 20:10:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class IgnoreListDelDecoder implements ClientProtDecoder<IgnoreListDelEvent> {
	@Override
	public IgnoreListDelEvent decode(int opcode, RSBuffer buffer) {
		final String name = buffer.readString();
		return new IgnoreListDelEvent(name);
	}
}
