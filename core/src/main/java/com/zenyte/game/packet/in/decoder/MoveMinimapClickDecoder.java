package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.MoveMinimapClickEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Jire
 */
public final class MoveMinimapClickDecoder implements ClientProtDecoder<MoveMinimapClickEvent> {

    @Override
    public MoveMinimapClickEvent decode(int opcode, RSBuffer buffer) {
        final int offsetX = buffer.readShort();
        final int type = buffer.read128Byte() & 0xFF;
        final int offsetY = buffer.readShort();
        buffer.readByte(); // last pressed offset X
        buffer.readByte(); // last pressed offset Y
        buffer.readShort(); // cam angle Y
        buffer.readByte(); // always 57
        buffer.readByte(); // always 0
        buffer.readByte(); // always 0
        buffer.readByte(); // always 89
        buffer.readShort(); // local player X
        buffer.readShort(); // local player Y
        buffer.readByte(); // always 63
        return new MoveMinimapClickEvent(type, offsetX, offsetY);
    }

}
