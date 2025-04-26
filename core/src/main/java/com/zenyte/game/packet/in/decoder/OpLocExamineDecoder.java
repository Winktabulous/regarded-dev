package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpLocExamineEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 25-1-2019 | 21:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpLocExamineDecoder implements ClientProtDecoder<OpLocExamineEvent> {
    @Override
    public OpLocExamineEvent decode(int opcode, RSBuffer buffer) {
        final int id = buffer.readShortLE128() & 65535;
        return new OpLocExamineEvent(id);
    }
}
