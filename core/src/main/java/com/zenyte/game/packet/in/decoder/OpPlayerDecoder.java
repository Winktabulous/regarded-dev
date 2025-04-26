package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpPlayerEvent;
import com.zenyte.net.game.ClientProt;
import com.zenyte.net.io.RSBuffer;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Tommeh | 28 jul. 2018 | 20:05:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpPlayerDecoder implements ClientProtDecoder<OpPlayerEvent> {
	public static final int[] OPCODES = {ClientProt.OPPLAYER1.getOpcode(), ClientProt.OPPLAYER2.getOpcode(),
			ClientProt.OPPLAYER3.getOpcode(), ClientProt.OPPLAYER4.getOpcode(), ClientProt.OPPLAYER5.getOpcode(),
			ClientProt.OPPLAYER6.getOpcode(), ClientProt.OPPLAYER7.getOpcode(), ClientProt.OPPLAYER8.getOpcode()};

	@Override
	public OpPlayerEvent decode(int opcode, RSBuffer buffer) {
		int index = -1;
		boolean run = false;
		final int option = ArrayUtils.indexOf(OPCODES, opcode) + 1;
		if (opcode == OPCODES[0]) {
			index = buffer.readShortLE128();
			run = buffer.read128Byte() == 1;
		} else if (opcode == OPCODES[1]) {
			index = buffer.readShort128();
			run = buffer.readByte() == 1;
		} else if (opcode == OPCODES[2]) {
			run = buffer.readByte() == 1;
			index = buffer.readShort128();
		} else if (opcode == OPCODES[3]) {
			index = buffer.readShort();
			run = buffer.readByteC() == 1;
		} else if (opcode == OPCODES[4]) {
			run = buffer.readByte128() == 1;
			index = buffer.readShortLE();
		} else if (opcode == OPCODES[5]) {
			run = buffer.readByteC() == 1;
			index = buffer.readShort();
		} else if (opcode == OPCODES[6]) {
			index = buffer.readShortLE128();
			run = buffer.readByte128() == 1;
		} else if (opcode == OPCODES[7]) {
			run = buffer.readByte() == 1;
			index = buffer.readShort128();
		}
		return new OpPlayerEvent(index, option, run);
	}
}
