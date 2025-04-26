package com.zenyte.game.world.entity.masks;

import com.near_reality.game.util.HuffmanManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.DefaultByteBufHolder;

/**
 * @author Kris | 6. nov 2017 : 14:28.18
 * @author Jire
 */
public final class ChatMessage extends DefaultByteBufHolder implements AutoCloseable {

    private int effects;
    private String chatText;
    private boolean autotyper;

    public ChatMessage(final ByteBuf huffmanBuf) {
        super(huffmanBuf);
    }

    public ChatMessage(final ByteBuf huffmanBuf,
                       final String chatText,
                       final int effects,
                       final boolean autotyper) {
        this(huffmanBuf);
        this.effects = effects;
        this.chatText = chatText;
        this.autotyper = autotyper;
    }

    public ChatMessage set(final String text,
                           final int effects,
                           final boolean autotyper) {
        this.chatText = text;
        this.effects = effects;
        this.autotyper = autotyper;

        final ByteBuf content = content();
        content.clear();
        HuffmanManager.encodeHuffmanBuf(content, text);
        return this;
    }

    public int getEffects() {
        return effects;
    }

    public String getChatText() {
        return chatText;
    }

    public boolean isAutotyper() {
        return autotyper;
    }

    public static ChatMessage reusable() {
        return new ChatMessage(ByteBufAllocator.DEFAULT.directBuffer(
                HuffmanManager.MAX_HUFFMAN_STRING_LENGTH,
                HuffmanManager.MAX_HUFFMAN_STRING_LENGTH));
    }

    @Override
    public void close() {
        release();
    }

}
