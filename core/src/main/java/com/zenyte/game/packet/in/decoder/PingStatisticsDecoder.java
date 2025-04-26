package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.PingStatisticsEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Kris | 1. apr 2018 : 22:05.58
 * @author Jire
 */
public final class PingStatisticsDecoder implements ClientProtDecoder<PingStatisticsEvent> {

    @Override
    public PingStatisticsEvent decode(int opcode, RSBuffer buffer) {
        final long end = System.nanoTime();

        final long start1 = buffer.readIntME() & 0xFFFF_FFFFL;
        final long start2 = buffer.readIntLE() & 0xFFFF_FFFFL;
        final long start = (start1 << 32) + start2;

        final int fps = buffer.read128Byte() & 0xFF;
        final int gc = buffer.readByte() & 0xFF;

        return new PingStatisticsEvent(gc, fps, end - start);
    }

}
