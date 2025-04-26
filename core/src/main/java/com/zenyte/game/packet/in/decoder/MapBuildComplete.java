package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.MapBuildCompleteEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:25:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MapBuildComplete implements ClientProtDecoder<MapBuildCompleteEvent> {

    @Override
    public MapBuildCompleteEvent decode(int opcode, RSBuffer buffer) {
        return new MapBuildCompleteEvent();
    }
}
