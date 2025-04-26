package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.FriendListAddEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 feb. 2018 : 16:00:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class FriendListAddDecoder implements ClientProtDecoder<FriendListAddEvent> {
	@Override
	public FriendListAddEvent decode(int opcode, RSBuffer buffer) {
		final String name = buffer.readString();
		return new FriendListAddEvent(name);
	}
}
