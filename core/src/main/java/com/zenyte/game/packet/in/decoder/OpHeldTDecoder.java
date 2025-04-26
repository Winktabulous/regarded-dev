package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpHeldTEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 16 dec. 2017 : 17:17:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpHeldTDecoder implements ClientProtDecoder<OpHeldTEvent> {
	@Override
	public OpHeldTEvent decode(int opcode, RSBuffer buffer) {
		final int fromCompressed = buffer.readIntME();
		final int toCompressed = buffer.readInt();
		final short toSlot = buffer.readShort();
		buffer.readShortLE();
		final short fromSlot = buffer.readShortLE();
		final int fromInterfaceId = fromCompressed >> 16;
		final int fromComponentId = fromCompressed & 65535;
		final int toInterfaceId = toCompressed >> 16;
		final int toComponentId = toCompressed & 65535;
		return new OpHeldTEvent(fromInterfaceId, fromComponentId, toInterfaceId, toComponentId, fromSlot, toSlot);
	}
}
