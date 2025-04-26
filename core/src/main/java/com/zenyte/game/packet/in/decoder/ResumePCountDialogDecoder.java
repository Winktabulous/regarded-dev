package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ResumePCountDialogEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:40:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePCountDialogDecoder implements ClientProtDecoder<ResumePCountDialogEvent> {
	@Override
	public ResumePCountDialogEvent decode(int opcode, RSBuffer buffer) {
		final int value = buffer.readInt();
		return new ResumePCountDialogEvent(value);
	}
}
