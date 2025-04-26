package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpHeldDEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 20:14:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpHeldDDecoder implements ClientProtDecoder<OpHeldDEvent> {
	@Override
	public OpHeldDEvent decode(int opcode, RSBuffer buffer) {
		final short toSlotId = buffer.readShort();
		final short fromSlotId = buffer.readShortLE();
		final int compressed = buffer.readInt();
		buffer.readByteC(); // Unknown field; seems to only register as @code { true } if component's type is 206, which is only true on an invisible logout tab component.
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new OpHeldDEvent(interfaceId, componentId, fromSlotId, toSlotId);
	}
}
