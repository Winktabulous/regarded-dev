package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.InterfaceOnFloorItemEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 1. apr 2018 : 21:15.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OpObjTDecoder implements ClientProtDecoder<InterfaceOnFloorItemEvent> {
	@Override
	public InterfaceOnFloorItemEvent decode(int opcode, RSBuffer buffer) {
		final short x = buffer.readShort();
		final int itemId = buffer.readShort();
		buffer.read128Byte();
		buffer.readShort128();
		buffer.readShortLE();
		final int compressed = buffer.readIntIME();
		final short y = buffer.readShortLE128();
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new InterfaceOnFloorItemEvent(interfaceId, componentId, itemId, x, y);
	}
}
