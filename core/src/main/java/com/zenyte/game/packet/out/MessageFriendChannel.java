package com.zenyte.game.packet.out;

import com.near_reality.game.util.HuffmanManager;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import com.zenyte.utils.TextUtils;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:33:11
 * @author Jire
 */
public final class MessageFriendChannel implements GamePacketEncoder {

    private final String channelName;
    private final int icon;

    private final String message;

    private final String senderName;
    private final int message_uid;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Channel: " + channelName + ", icon: " + icon + ", sender: " + senderName + ", message:" +
                " " + message);
    }

    public MessageFriendChannel(Player sender, String channelName, int icon,
                                String message) {
        this.channelName = channelName;
        this.icon = icon;

        this.message = message;

        senderName = sender.getTitleName();
        message_uid = sender.getSocialManager().getNextUniqueId();
    }

    @Override
    public GamePacketOut encode() {
        final GamePacketOut buffer = ServerProt.MESSAGE_FRIENDCHANNEL.gamePacketOut();
        buffer.writeString(senderName);
        buffer.writeLong(TextUtils.stringToLong(channelName));
        buffer.writeShort(1);
        buffer.write24BitInteger(message_uid);
        buffer.writeByte(0);
        final ByteBuf huffmanBuf = HuffmanManager.encodeHuffmanBuf(message);
        try {
            buffer.writeBytes(huffmanBuf);
        } finally {
            huffmanBuf.release();
        }
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

}
