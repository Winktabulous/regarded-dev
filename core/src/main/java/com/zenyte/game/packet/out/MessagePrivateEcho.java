package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.masks.ChatMessage;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:38:04
 * @author Jire
 */
public final class MessagePrivateEcho implements GamePacketEncoder {

    private final String toUser;
    private final ChatMessage message;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "To: " + toUser + ", message: " + message.getChatText());
    }

    public MessagePrivateEcho(String toUser, ChatMessage message) {
        this.toUser = toUser;
        message.retain();
        this.message = message;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public GamePacketOut encode() {
        try (message) {
            final GamePacketOut buffer = ServerProt.MESSAGE_PRIVATE_ECHO.gamePacketOut();
            buffer.writeString(toUser);
            final ByteBuf huffmanBuf = message.content();
            buffer.writeBytes(huffmanBuf, 0, huffmanBuf.readableBytes());
            return buffer;
        }
    }

}
