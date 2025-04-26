package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpNpcUEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:48:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpNpcUDecoder implements ClientProtDecoder<OpNpcUEvent> {
	@Override
	public OpNpcUEvent decode(int opcode, RSBuffer buffer) {
		final short slotId = buffer.readShort128();
		final boolean run = buffer.read128Byte() == 1;
		final short itemId = buffer.readShort();
		final int compressed = buffer.readIntIME();
		final short index = buffer.readShortLE();
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new OpNpcUEvent(interfaceId, componentId, slotId, itemId, index, run);
	}
}
