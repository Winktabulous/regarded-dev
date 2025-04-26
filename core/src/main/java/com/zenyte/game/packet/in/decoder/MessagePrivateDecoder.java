package com.zenyte.game.packet.in.decoder;

import com.near_reality.game.util.HuffmanManager;
import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.MessagePrivateEvent;
import com.zenyte.game.world.entity.masks.ChatMessage;
import com.zenyte.net.io.RSBuffer;
import io.netty.buffer.ByteBuf;

import static com.near_reality.game.util.HuffmanManager.readHuffmanString;

/**
 * @author Tommeh | 28 jul. 2018 | 20:05:47
 * @author Jire
 */
public final class MessagePrivateDecoder implements ClientProtDecoder<MessagePrivateEvent> {

    @Override
    public MessagePrivateEvent decode(int opcode, RSBuffer buffer) {
        final String recipient = buffer.readString();
        final String message = readHuffmanString(buffer);
        final ByteBuf huffmanBuf = HuffmanManager.encodeHuffmanBuf(message);
        final ChatMessage chatMessage = new ChatMessage(huffmanBuf, message, 0, false);
        return new MessagePrivateEvent(recipient, chatMessage);
    }

}
