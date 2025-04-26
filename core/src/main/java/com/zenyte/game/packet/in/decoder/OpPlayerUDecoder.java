package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpPlayerUEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:50:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class OpPlayerUDecoder implements ClientProtDecoder<OpPlayerUEvent> {
	@Override
	public OpPlayerUEvent decode(int opcode, RSBuffer buffer) {
		final int targetIndex = buffer.readShortLE128();
		final int itemId = buffer.readShort();
		final int interfaceId = buffer.readIntLE();
		final byte run = buffer.readByteC();
		final short slotId = buffer.readShort128();
		return new OpPlayerUEvent(targetIndex, slotId, itemId, interfaceId, run);
	}
}
