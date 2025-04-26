package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpHeldEvent;
import com.zenyte.net.game.ClientProt;
import com.zenyte.net.io.RSBuffer;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Tommeh | 28 jul. 2018 | 19:46:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpHeldDecoder implements ClientProtDecoder<OpHeldEvent> {
	public static final int[] OPCODES = {ClientProt.OPHELD1.getOpcode(), ClientProt.OPHELD2.getOpcode(), ClientProt.OPHELD3.getOpcode(), ClientProt.OPHELD4.getOpcode(), ClientProt.OPHELD5.getOpcode()};

	@Override
	public OpHeldEvent decode(int opcode, RSBuffer buffer) {
		int slotId = -1;
		int itemId = -1;
		final int option = ArrayUtils.indexOf(OPCODES, opcode) + 1;
		if (opcode == OPCODES[0]) {
			itemId = buffer.readShortLE() & 65535;
			buffer.readIntME();
			slotId = buffer.readShort();
		} else if (opcode == OPCODES[1]) {
			itemId = buffer.readShort128() & 65535;
			buffer.readIntIME();
			slotId = buffer.readShortLE128();
		} else if (opcode == OPCODES[2]) {
			buffer.readIntLE();
			slotId = buffer.readShortLE128();
			itemId = buffer.readShortLE128() & 65535;
		} else if (opcode == OPCODES[3]) {
			itemId = buffer.readShortLE128() & 65535;
			buffer.readInt();
			slotId = buffer.readShortLE128();
		} else if (opcode == OPCODES[4]) {
			buffer.readInt();
			slotId = buffer.readShort();
			itemId = buffer.readShort() & 65535;
		}
		if (itemId == 65535) {
			itemId = -1;
		}
		return new OpHeldEvent(slotId, itemId, option);
	}
}
