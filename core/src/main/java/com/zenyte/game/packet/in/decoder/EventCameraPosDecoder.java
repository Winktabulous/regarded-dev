package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.EventCameraPosEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 30. march 2018 : 3:20.31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class EventCameraPosDecoder implements ClientProtDecoder<EventCameraPosEvent> {
	@Override
	public EventCameraPosEvent decode(int opcode, RSBuffer buffer) {
		final short x = buffer.readShortLE128();
		final short y = buffer.readShort();
		return new EventCameraPosEvent(x, y);
	}
}
