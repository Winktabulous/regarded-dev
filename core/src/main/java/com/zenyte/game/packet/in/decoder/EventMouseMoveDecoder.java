package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.EventMouseMoveEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:55:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class EventMouseMoveDecoder implements ClientProtDecoder<EventMouseMoveEvent> {

    @Override
    public EventMouseMoveEvent decode(int opcode, RSBuffer buffer) {
        //buffer.skipBytes(buffer.readableBytes());
        return new EventMouseMoveEvent();
    }

}
