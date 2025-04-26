package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.FriendListDelEvent;
import com.zenyte.net.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 20:09:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FriendListDelDecoder implements ClientProtDecoder<FriendListDelEvent> {
    @Override
    public FriendListDelEvent decode(int opcode, RSBuffer buffer) {
        final String name = buffer.readString();
        return new FriendListDelEvent(name);
    }
}
