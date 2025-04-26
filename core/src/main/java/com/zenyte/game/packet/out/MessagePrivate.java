package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.ChatMessage;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:36:21
 * @author Jire
 */
public final class MessagePrivate implements GamePacketEncoder {

    private final String sender;
    private final ChatMessage message;
    private final int icon;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Sender: " + sender + ", icon: " + icon + ", message: " + message.getChatText());
    }

    public MessagePrivate(String sender, ChatMessage message, int icon) {
        this.sender = sender;
        message.retain();
        this.message = message;
        this.icon = icon;
    }

    @Override
    public GamePacketOut encode() {
        try (message) {
            final GamePacketOut buffer = ServerProt.MESSAGE_PRIVATE.gamePacketOut();
            buffer.writeString(sender);
            for (int i = 0; i < 5; i++) {
                buffer.writeByte(Utils.random(255));
            }
            buffer.writeByte(icon);
            final ByteBuf huffmanBuf = message.content();
            buffer.writeBytes(huffmanBuf, 0, huffmanBuf.readableBytes());
            return buffer;
        }
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

}
