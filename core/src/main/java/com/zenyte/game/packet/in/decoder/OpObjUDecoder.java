package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpObjUEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 1. apr 2018 : 21:15.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OpObjUDecoder implements ClientProtDecoder<OpObjUEvent> {
	@Override
	public OpObjUEvent decode(int opcode, RSBuffer buffer) {
		final int floorItemId = buffer.readShortLE128() & 0xFFFF;
		final boolean run = buffer.readByte128() == 1;
		final short slotId = buffer.readShort128();
		final short x = buffer.readShort128();
		final int compressed = buffer.readIntME();
		final short y = buffer.readShortLE();
		final int itemId = buffer.readShortLE() & 0xFFFF;
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new OpObjUEvent(interfaceId, componentId, slotId, itemId, floorItemId, x, y, run);
	}
}
