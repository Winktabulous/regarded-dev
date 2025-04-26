package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.FreeCamResetEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 1. apr 2018 : 20:38.49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class FreeCamResetDecoder implements ClientProtDecoder<FreeCamResetEvent> {

	@Override
	public FreeCamResetEvent decode(int opcode, RSBuffer buffer) {
		return new FreeCamResetEvent();
	}
}
