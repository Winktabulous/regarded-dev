package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.EventKeyboardEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:52:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class EventKeyboardDecoder implements ClientProtDecoder<EventKeyboardEvent> {
	@Override
	public EventKeyboardEvent decode(int opcode, RSBuffer buffer) {
		int key = -1;
		int msLastKeyStroke = -1;
		while (buffer.isReadable()) {
			msLastKeyStroke = buffer.readMedium();//Wrong type
			key = buffer.readByteC();
		}
		return new EventKeyboardEvent(key, msLastKeyStroke);
	}
}
