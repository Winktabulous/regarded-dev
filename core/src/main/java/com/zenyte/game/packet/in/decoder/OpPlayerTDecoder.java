package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpPlayerTEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:45:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpPlayerTDecoder implements ClientProtDecoder<OpPlayerTEvent> {
	@Override
	public OpPlayerTEvent decode(int opcode, RSBuffer buffer) {
		final boolean run = buffer.readByte128() == 1;
		final short index = buffer.readShortLE128();
		final int compressed = buffer.readInt();
		final int itemId = buffer.readShortLE128();
		final int componentIndex = buffer.readShort128();
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new OpPlayerTEvent(interfaceId, componentId, index, run, itemId, componentIndex);
	}
}
