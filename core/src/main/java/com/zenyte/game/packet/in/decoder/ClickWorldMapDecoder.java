package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ClickWorldMapEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 31 mrt. 2018 : 15:07:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClickWorldMapDecoder implements ClientProtDecoder<ClickWorldMapEvent> {
	@Override
	public ClickWorldMapEvent decode(int opcode, RSBuffer buffer) {
		final byte z = buffer.readByteC();
		final short y = buffer.readShortLE128();
		final short x = buffer.readShort128();
		buffer.readIntME();
		return new ClickWorldMapEvent(x, y, z);
	}
}
