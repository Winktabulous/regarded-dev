package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.EventMouseClickEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:55:14
 * @author Jire
 */
public final class EventMouseClickDecoder implements ClientProtDecoder<EventMouseClickEvent> {

    @Override
    public EventMouseClickEvent decode(int opcode, RSBuffer buffer) {
        final int lastPressedBitpack = buffer.readUnsignedShort();
        final int lastButton = lastPressedBitpack & 1;
        final int lastPressedMillis = lastPressedBitpack >>> 1;

        final int lastPressedX = buffer.readUnsignedShort();
        final int lastPressedY = buffer.readUnsignedShort();

        return new EventMouseClickEvent(lastButton, lastPressedMillis,
                lastPressedX, lastPressedY);
    }

}
