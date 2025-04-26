package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.EventAppletFocusEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:27:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class EventAppletFocusDecoder implements ClientProtDecoder<EventAppletFocusEvent> {

    @Override
    public EventAppletFocusEvent decode(int opcode, RSBuffer buffer) {
        final boolean active = buffer.readUnsignedByte() == 1;
        return new EventAppletFocusEvent(active);
    }

}
