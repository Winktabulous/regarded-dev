package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.MoveGameClickEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 22:21:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class MoveGameClickDecoder implements ClientProtDecoder<MoveGameClickEvent> {

    @Override
    public MoveGameClickEvent decode(int opcode, RSBuffer buffer) {
        final short offsetX = buffer.readShort();
        final int type = buffer.read128Byte() & 0xFF;
        final short offsetY = buffer.readShort();
        return new MoveGameClickEvent(type, offsetX, offsetY);
    }

}
