package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.WindowStatusEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jan. 2018 : 21:27:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class WindowStatusDecoder implements ClientProtDecoder<WindowStatusEvent> {
	@Override
	public WindowStatusEvent decode(final int opcode, final RSBuffer buffer) {
		final short mode = buffer.readUnsignedByte();
		final short width = buffer.readShort();
		final short height = buffer.readShort();
		return new WindowStatusEvent(mode, width, height);
	}
}
