package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.CloseModalEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:27:53
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CloseModalDecoder implements ClientProtDecoder<CloseModalEvent> {

	@Override
	public CloseModalEvent decode(int opcode, RSBuffer buffer) {
		return new CloseModalEvent();
	}
}
