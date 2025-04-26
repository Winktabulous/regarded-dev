package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpLocUEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:49:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpLocUDecoder implements ClientProtDecoder<OpLocUEvent> {
	@Override
	public OpLocUEvent decode(int opcode, RSBuffer buffer) {
		final int compressed = buffer.readIntME();
		final short x = buffer.readShort128();
		final boolean run = buffer.read128Byte() == 1;
		final int objectId = buffer.readShort128();
		final short slotId = buffer.readShort128();
		final short itemId = buffer.readShort128();
		final short y = buffer.readShortLE128();
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new OpLocUEvent(interfaceId, componentId, slotId, itemId, objectId, x, y, run);
	}
}
