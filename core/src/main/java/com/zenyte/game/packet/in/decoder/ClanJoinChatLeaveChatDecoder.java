package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ClanJoinChatLeaveChatEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 2 dec. 2017 : 18:53:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClanJoinChatLeaveChatDecoder implements ClientProtDecoder<ClanJoinChatLeaveChatEvent> {
	@Override
	public ClanJoinChatLeaveChatEvent decode(int opcode, RSBuffer buffer) {
		final String name = buffer.readString();
		return new ClanJoinChatLeaveChatEvent(name);
	}
}
