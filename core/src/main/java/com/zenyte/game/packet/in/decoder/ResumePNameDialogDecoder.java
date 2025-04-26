package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ResumePNameDialogEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:56:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePNameDialogDecoder implements ClientProtDecoder<ResumePNameDialogEvent> {
	@Override
	public ResumePNameDialogEvent decode(int opcode, RSBuffer buffer) {
		final String name = buffer.readString();
		return new ResumePNameDialogEvent(name);
	}
}
