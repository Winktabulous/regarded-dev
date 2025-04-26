package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpNpcEvent;
import com.zenyte.net.game.ClientProt;
import com.zenyte.net.io.RSBuffer;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Tommeh | 28 jul. 2018 | 19:56:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpNpcDecoder implements ClientProtDecoder<OpNpcEvent> {
	public static final int[] OPCODES = {ClientProt.OPNPC1.getOpcode(), ClientProt.OPNPC2.getOpcode(), ClientProt.OPNPC3.getOpcode(), ClientProt.OPNPC4.getOpcode(), ClientProt.OPNPC5.getOpcode()};

	/*ClientProt.OPNPC6.getOpcode()*/
	@Override
	public OpNpcEvent decode(int opcode, RSBuffer buffer) {
		boolean run = false;
		int index = -1;
		final int option = ArrayUtils.indexOf(OPCODES, opcode) + 1;
		if (opcode == OPCODES[0]) {
			run = buffer.read128Byte() == 1;
			index = buffer.readShort128();
		} else if (opcode == OPCODES[1]) {
			run = buffer.readByte() == 1;
			index = buffer.readShortLE128();
		} else if (opcode == OPCODES[2]) {
			run = buffer.readByte128() == 1;
			index = buffer.readShort();
		} else if (opcode == OPCODES[3]) {
			run = buffer.readByte() == 1;
			index = buffer.readShortLE();
		} else if (opcode == OPCODES[4]) {
			index = buffer.readShort128();
			run = buffer.readByteC() == 1;
		}
		return new OpNpcEvent(index, option, run);
	}
}
