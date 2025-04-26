package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.DoubleClickWorldMapEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 31. march 2018 : 19:52.25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class DoubleClickWorldMapDecoder implements ClientProtDecoder<DoubleClickWorldMapEvent> {
	@Override
	public DoubleClickWorldMapEvent decode(int opcode, RSBuffer buffer) {
		final int compressed = buffer.readIntME();
		return new DoubleClickWorldMapEvent(compressed);
	}
}
