package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.OpNpcExamineEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 25-1-2019 | 19:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpNpcExamineDecoder implements ClientProtDecoder<OpNpcExamineEvent> {
    @Override
    public OpNpcExamineEvent decode(final int opcode, final RSBuffer buffer) {
        final short npcId = buffer.readShortLE();
        return new OpNpcExamineEvent(npcId);
    }
}
