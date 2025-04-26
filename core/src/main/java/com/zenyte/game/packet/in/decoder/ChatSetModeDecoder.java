package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ChatSetModeEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:27:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChatSetModeDecoder implements ClientProtDecoder<ChatSetModeEvent> {
	@Override
	public ChatSetModeEvent decode(final int opcode, final RSBuffer buffer) {
		final byte publicFilter = buffer.readByte();
		final byte privateFilter = buffer.readByte();
		final byte tradeFilter = buffer.readByte();
		return new ChatSetModeEvent(publicFilter, privateFilter, tradeFilter);
	}
}
