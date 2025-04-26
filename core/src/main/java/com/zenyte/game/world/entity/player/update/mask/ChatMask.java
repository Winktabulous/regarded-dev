package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.masks.ChatMessage;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;
import io.netty.buffer.ByteBuf;

/**
 * @author Kris | 7. mai 2018 : 17:15:09
 * @author Jire
 */
public final class ChatMask extends UpdateMask {

    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.CHAT;
    }

    @Override
    public void writePlayer(final RSBuffer buffer, final Player player, final Player processedPlayer) {
        final ChatMessage message = processedPlayer.getChatMessage();

        buffer.writeShortLE(message.getEffects());
        buffer.writeByteC(processedPlayer.getRankIcon());
        buffer.write128Byte((byte) (message.isAutotyper() ? 1 : 0));

        final ByteBuf huffmanBuf = message.content().copy();
        final int length = huffmanBuf.readableBytes();
        buffer.write128Byte(length);
        buffer.writeBytes(huffmanBuf, length);
    }

}
