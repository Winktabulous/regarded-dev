package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpModelEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 7 feb. 2018 : 18:11:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpModelDecoder implements ClientProtDecoder<OpModelEvent> {
	@Override
	public OpModelEvent decode(int opcode, RSBuffer buffer) {
		final int compressed = buffer.readInt();
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new OpModelEvent(interfaceId, componentId);
	}
}
