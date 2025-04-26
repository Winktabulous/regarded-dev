package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpObjEvent;
import com.zenyte.net.game.ClientProt;
import com.zenyte.net.io.RSBuffer;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Tommeh | 28 jul. 2018 | 19:30:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpObjDecoder implements ClientProtDecoder<OpObjEvent> {
	public static final int[] OPCODES = {ClientProt.OPOBJ1.getOpcode(), ClientProt.OPOBJ2.getOpcode(), ClientProt.OPOBJ3.getOpcode(), ClientProt.OPOBJ4.getOpcode(), ClientProt.OPOBJ5.getOpcode(), ClientProt.OPOBJE.getOpcode()};

	@Override
	public OpObjEvent decode(int opcode, RSBuffer buffer) {
		int itemId = -1;
		int x = -1;
		int y = -1;
		boolean run = false;
		final int option = ArrayUtils.indexOf(OPCODES, opcode) + 1;
		if (opcode == OPCODES[0]) {
			y = buffer.readShort128();
			run = buffer.readByte128() == 1;
			x = buffer.readShortLE128();
			itemId = buffer.readShortLE() & 0xFFFF;
		} else if (opcode == OPCODES[1]) {
			itemId = buffer.readShortLE128() & 0xFFFF;
			y = buffer.readShort128();
			run = buffer.read128Byte() == 1;
			x = buffer.readShortLE();
		} else if (opcode == OPCODES[2]) {
			x = buffer.readShortLE();
			y = buffer.readShortLE128();
			run = buffer.readByte128() == 1;
			itemId = buffer.readShort128() & 0xFFFF;
		} else if (opcode == OPCODES[3]) {
			itemId = buffer.readShortLE128() & 0xFFFF;
			run = buffer.readByteC() == 1;
			x = buffer.readShort128();
			y = buffer.readShortLE128();
		} else if (opcode == OPCODES[4]) {
			y = buffer.readShortLE();
			run = buffer.readByteC() == 1;
			itemId = buffer.readShort() & 0xFFFF;
			x = buffer.readShortLE128();
		} else if (opcode == OPCODES[5]) {
			itemId = buffer.readShortLE() & 0xFFFF;
			x = buffer.readShort();
			y = buffer.readShortLE();
		}
		return new OpObjEvent(itemId, x, y, option, run);
	}
}
