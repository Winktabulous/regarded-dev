package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.If3ButtonEvent;
import com.zenyte.net.game.ClientProt;
import com.zenyte.net.io.RSBuffer;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Tommeh | 28 jul. 2018 | 16:04:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class If3ButtonActionDecoder implements ClientProtDecoder<If3ButtonEvent> {
    public static final int[] OPCODES = {ClientProt.IF_BUTTON1.getOpcode(), ClientProt.IF_BUTTON2.getOpcode(),
			ClientProt.IF_BUTTON3.getOpcode(), ClientProt.IF_BUTTON4.getOpcode(), ClientProt.IF_BUTTON5.getOpcode(),
			ClientProt.IF_BUTTON6.getOpcode(), ClientProt.IF_BUTTON7.getOpcode(),
			ClientProt.IF_BUTTON8.getOpcode(), ClientProt.IF_BUTTON9.getOpcode(),
			ClientProt.IF_BUTTON10.getOpcode()};

    @Override
    public If3ButtonEvent decode(int opcode, RSBuffer buffer) {
        final int compressed = buffer.readInt();
        int slotId = buffer.readShort() & 65535;
        int itemId = buffer.readShort() & 65535;
        final int interfaceId = (compressed >> 16);
        final int componentId = (compressed & 65535);
        final int option = ArrayUtils.indexOf(OPCODES, opcode) + 1;
        return new If3ButtonEvent(interfaceId, componentId, slotId, itemId, option);
    }
}
