package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ResumePObjDialogEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 20:11:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePObjDialogDecoder implements ClientProtDecoder<ResumePObjDialogEvent> {
    @Override
    public ResumePObjDialogEvent decode(int opcode, RSBuffer buffer) {
        final short itemId = buffer.readShort();
        return new ResumePObjDialogEvent(itemId);
    }
}
