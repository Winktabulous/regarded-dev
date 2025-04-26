package com.zenyte.game.util;

import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 17. march 2018 : 20:59.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>
 */
public final class ZenyteHuffman {

    private static final Logger log = LoggerFactory.getLogger(ZenyteHuffman.class);
    private static int[] MASKS;
    private static byte[] BITS;
    private static int[] KEYS;

    public static final void load(Cache cache) {
        try {
            final Archive archive = cache.getArchive(ArchiveType.BINARY);
            final Group group = archive.findGroupByName("huffman");
            final byte[] sizes = group.findFileByID(0).getData().getBuffer();
            final int length = sizes.length;
            MASKS = new int[length];
            BITS = sizes;
            final int[] ints_0 = new int[33];
            int[] keys = new int[8];
            int int_1 = 0;
            for (int i = 0; i < length; i++) {
                final byte size = sizes[i];
                if (size != 0) {
                    final int int_3 = 1 << (32 - size);
                    final int mask = ints_0[size];
                    MASKS[i] = mask;
                    int int_5;
                    int int_6;
                    int int_7;
                    int int_8;
                    if ((mask & int_3) != 0) {
                        int_5 = ints_0[size - 1];
                    } else {
                        int_5 = mask | int_3;
                        for (int_6 = size - 1; int_6 >= 1; --int_6) {
                            int_7 = ints_0[int_6];
                            if (int_7 != mask) {
                                break;
                            }
                            int_8 = 1 << (32 - int_6);
                            if ((int_7 & int_8) != 0) {
                                ints_0[int_6] = ints_0[int_6 - 1];
                                break;
                            }
                            ints_0[int_6] = int_7 | int_8;
                        }
                    }
                    ints_0[size] = int_5;
                    for (int_6 = size + 1; int_6 <= 32; int_6++) {
                        if (mask == ints_0[int_6]) {
                            ints_0[int_6] = int_5;
                        }
                    }
                    int_6 = 0;
                    for (int_7 = 0; int_7 < size; int_7++) {
                        int_8 = Integer.MIN_VALUE >>> int_7;
                        if ((mask & int_8) != 0) {
                            if (keys[int_6] == 0) {
                                keys[int_6] = int_1;
                            }
                            int_6 = keys[int_6];
                        } else {
                            ++int_6;
                        }
                        if (int_6 >= keys.length) {
                            final int[] ints_1 = new int[keys.length * 2];
                            System.arraycopy(keys, 0, ints_1, 0, keys.length);
                            keys = ints_1;
                        }
                    }
                    keys[int_6] = ~i;
                    if (int_6 >= int_1) {
                        int_1 = int_6 + 1;
                    }
                }
            }
            KEYS = keys;
        } catch (final Exception e) {
            log.error("Failed to load Huffman", e);
        }
    }

    /**
     * Decompresses a compressed message sent by the client.
     *
     * @param buffer        the buffer to read.
     * @param bufferOffset  the offset to start reading message at.
     * @param messageBytes  the array of bytes to write to.
     * @param messageOffset the offset at which to start writing.
     * @param messageLength the length of the written message.
     * @return total number of bytes written.
     */
    public static final int decompress(final byte[] buffer, final int bufferOffset, final byte[] messageBytes, int messageOffset, int messageLength) {
        if (messageLength == 0) {
            return 0;
        } else {
            int int_3 = 0;
            messageLength += messageOffset;
            int int_4 = bufferOffset;
            while (true) {
                final byte byte_0 = buffer[int_4];
                if (byte_0 < 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                int int_5;
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                if ((byte_0 & 64) != 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                if ((byte_0 & 32) != 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                if ((byte_0 & 16) != 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                if ((byte_0 & 8) != 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                if ((byte_0 & 4) != 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                if ((byte_0 & 2) != 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                if ((byte_0 & 1) != 0) {
                    int_3 = KEYS[int_3];
                } else {
                    ++int_3;
                }
                if ((int_5 = KEYS[int_3]) < 0) {
                    messageBytes[messageOffset++] = (byte) (~int_5);
                    if (messageOffset >= messageLength) {
                        break;
                    }
                    int_3 = 0;
                }
                ++int_4;
            }
            return (int_4 + 1) - bufferOffset;
        }
    }

    /**
     * Compresses an array of bytes into the client format.
     *
     * @param messageBytes  the array of bytes to compress.
     * @param messageOffset the offset at which to start reading and compressing.
     * @param messageLength the length of the message, aka where to stop compressing.
     * @param buffer        the array to which to write the compressed message.
     * @param bufferOffset  the offset at which to start writing the compressed message.
     * @return total amount of bytes written.
     */
    public static final int compress(final byte[] messageBytes, int messageOffset, int messageLength, final byte[] buffer, final int bufferOffset) {
        int int_3 = 0;
        int int_4 = bufferOffset << 3;
        for (messageLength += messageOffset; messageOffset < messageLength; messageOffset++) {
            final int int_5 = messageBytes[messageOffset] & 255;
            final int int_6 = MASKS[int_5];
            final byte byte_0 = BITS[int_5];
            if (byte_0 == 0) {
                throw new RuntimeException("");
            }
            int int_7 = int_4 >> 3;
            int int_8 = int_4 & 7;
            int_3 &= -int_8 >> 31;
            final int int_9 = (((byte_0 + int_8) - 1) >> 3) + int_7;
            int_8 += 24;
            buffer[int_7] = (byte) (int_3 |= int_6 >>> int_8);
            if (int_7 < int_9) {
                ++int_7;
                int_8 -= 8;
                buffer[int_7] = (byte) (int_3 = int_6 >>> int_8);
                if (int_7 < int_9) {
                    ++int_7;
                    int_8 -= 8;
                    buffer[int_7] = (byte) (int_3 = int_6 >>> int_8);
                    if (int_7 < int_9) {
                        ++int_7;
                        int_8 -= 8;
                        buffer[int_7] = (byte) (int_3 = int_6 >>> int_8);
                        if (int_7 < int_9) {
                            ++int_7;
                            int_8 -= 8;
                            buffer[int_7] = (byte) (int_3 = int_6 << -int_8);
                        }
                    }
                }
            }
            int_4 += byte_0;
        }
        return ((int_4 + 7) >> 3) - bufferOffset;
    }
}
