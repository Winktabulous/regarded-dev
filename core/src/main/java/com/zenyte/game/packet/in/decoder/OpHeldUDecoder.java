package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpHeldUEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 31 mrt. 2018 : 16:58:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpHeldUDecoder implements ClientProtDecoder<OpHeldUEvent> {
	@Override
	public OpHeldUEvent decode(int opcode, RSBuffer buffer) {
		buffer.readIntME();
		buffer.readIntIME();
		final short fromSlotId = buffer.readShortLE128();
		final short fromItemId = buffer.readShortLE();
		final short toSlotId = buffer.readShortLE();
		final short toItemId = buffer.readShortLE128();
		return new OpHeldUEvent(fromSlotId, fromItemId, toSlotId, toItemId);
	}
}
