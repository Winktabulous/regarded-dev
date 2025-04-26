package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ResumePauseButtonEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:29:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePauseButtonDecoder implements ClientProtDecoder<ResumePauseButtonEvent> {
	@Override
	public ResumePauseButtonEvent decode(final int opcode, final RSBuffer buffer) {
		final short slotId = buffer.readShort();
		final int compressed = buffer.readInt();
		final int interfaceId = compressed >> 16;
		final int componentId = compressed & 65535;
		return new ResumePauseButtonEvent(interfaceId, componentId, slotId);
	}
}
