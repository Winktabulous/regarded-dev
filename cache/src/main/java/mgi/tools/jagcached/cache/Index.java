package mgi.tools.jagcached.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Manages the reading and writing for a particular index in the cache.
 */
public class Index {

    private static final int INDEX_ENTRY_SIZE = 6;
    private static final int BLOCK_HEADER_SIZE = 8;
    private static final int EXTENDED_BLOCK_HEADER_SIZE = 10;
    private static final int BLOCK_DATA_SIZE = 512;
    private static final int EXTENDED_BLOCK_DATA_SIZE = 510;
    private static final int BLOCK_SIZE = BLOCK_HEADER_SIZE + BLOCK_DATA_SIZE;

    private static final int TEMP_BUFFER_SIZE = Math.max(BLOCK_HEADER_SIZE, EXTENDED_BLOCK_HEADER_SIZE);
    private static final int INDEX_BUFFER_SIZE = INDEX_ENTRY_SIZE * 1000;
    private static final int DATA_BUFFER_SIZE = BLOCK_SIZE * 10;
    private static final ThreadLocal<ByteBuffer> tempBuffer =
            ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(BLOCK_SIZE));

    private final int id;

    private final FileChannel indexChannel;
    private final long indexBufferSize;
    private final ByteBuffer indexBuffer;

    private final FileChannel dataChannel;
    private final long dataBufferSize;
    private final MappedByteBuffer dataBuffer;

    private int maxSize;

    /**
     * Creates a new Index object.
     *
     * @param id           The index of this index.
     * @param dataChannel  The channel of the data file for this index.
     * @param indexChannel The channel of the index file for this index.
     * @param maxSize      The maximum size of a file in this index.
     */
    public Index(int id, FileChannel dataChannel,
                 FileChannel indexChannel, int maxSize) throws IOException {
        this.id = id;
        this.dataChannel = dataChannel;
        this.indexChannel = indexChannel;
        this.maxSize = maxSize;

        dataBufferSize = dataChannel.size();
        dataBuffer = dataChannel.map(FileChannel.MapMode.READ_ONLY, 0, dataBufferSize);

        indexBufferSize = indexChannel.size();
        indexBuffer = ByteBuffer.allocateDirect((int) indexBufferSize);
        indexChannel.read(indexBuffer);
        indexBuffer.flip();
    }

    /**
     * Gets the number of groups stored in this index.
     *
     * @return This index's group count.
     */
    public int groupCount() {
        return (int) (indexBufferSize / INDEX_ENTRY_SIZE);
    }

    /**
     * Reads a group from the index.
     *
     * @param group The group to read.
     * @return The group's data, or null if the group was invalid.
     */
    public mgi.utilities.ByteBuffer get(final int group) {
        final int indexPos = group * INDEX_ENTRY_SIZE;
        if (indexPos + INDEX_ENTRY_SIZE > indexBufferSize) return null;

        final int size = getMediumInt(indexBuffer, indexPos);
        if (size <= 0 || size > maxSize) return null;

        final int block = getMediumInt(indexBuffer, indexPos + 3);
        if (block == 0) return null;

        final boolean extended = group > 0xFFFF;
        final int headerSize = extended ? EXTENDED_BLOCK_HEADER_SIZE : BLOCK_HEADER_SIZE;
        final int dataSize = extended ? EXTENDED_BLOCK_DATA_SIZE : BLOCK_DATA_SIZE;

        ByteBuffer buf = null;

        int num = 0;
        int nextBlock = block;
        while (nextBlock > 0) {
            int pos = nextBlock * BLOCK_SIZE;
            if (pos + headerSize > dataBufferSize) return null;

            final int actualGroup = extended
                    ? dataBuffer.getInt(pos)
                    : dataBuffer.getShort(pos) & 0xFFFF;
            if (actualGroup != group) {
                System.err.println("index " + id + " expected group " + group + " but was " + actualGroup);
                return null;
            }
            pos += extended ? 4 : 2;

            final int actualNum = dataBuffer.getShort(pos) & 0xFFFF;
            if (actualNum != num) {
                System.err.println("index " + id + " expected num " + num + " but was " + actualNum);
                return null;
            }
            pos += 2;

            nextBlock = getMediumInt(dataBuffer, pos);
            pos += 3;

            final int actualArchive = dataBuffer.get(pos++) & 0xFF; // - archiveOffset
            if (actualArchive != id) {
                System.err.println("index " + id + " but was " + actualArchive);
                return null;
            }

            if (buf == null) buf = ByteBuffer.allocateDirect(size);
            final int bufPos = buf.position();
            final int rem = Math.max(0, buf.limit() - bufPos);
            final int len = Math.min(rem, dataSize);
            buf.put(bufPos, dataBuffer, pos, len);
            buf.position(bufPos + len);

            num++;
        }

        if (buf == null) return null;

        buf.flip();
        final byte[] array = new byte[buf.remaining()];
        buf.get(array);

        return new mgi.utilities.ByteBuffer(array);
    }

    /**
     * Writes a group to the index.
     *
     * @param group The group to write.
     * @param data  The group's data.
     * @param size  The size of the group.
     * @return true if the group was written, false otherwise.
     */
    public boolean put(int group, mgi.utilities.ByteBuffer data, int size) {
        if (size < 0 || size > maxSize) {
            throw new IllegalArgumentException("Group too big: " + group + " size: " + size);
        }

        boolean success = put(group, ByteBuffer.wrap(data.getBuffer()), size, true);
        if (!success) {
            success = put(group, ByteBuffer.wrap(data.getBuffer()), size, false);
        }

        return success;
    }

    private boolean put(int group, ByteBuffer data, int size, boolean exists) {
        try {
            int block;
            if (exists) {
                if (group * INDEX_ENTRY_SIZE + INDEX_ENTRY_SIZE > indexChannel.size()) {
                    return false;
                }

                final ByteBuffer tempBuffer = Index.tempBuffer.get();

                tempBuffer.position(0).limit(INDEX_ENTRY_SIZE);
                indexChannel.read(tempBuffer, group * INDEX_ENTRY_SIZE);
                tempBuffer.flip().position(3);
                block = getMediumInt(tempBuffer);

                if (block <= 0 || block > dataChannel.size() / BLOCK_SIZE) {
                    return false;
                }
            } else {
                block = (int) (dataChannel.size() + BLOCK_SIZE - 1) / BLOCK_SIZE;
                if (block == 0) {
                    block = 1;
                }
            }

            final ByteBuffer tempBuffer = Index.tempBuffer.get();

            tempBuffer.position(0);
            putMediumInt(tempBuffer, size);
            putMediumInt(tempBuffer, block);
            tempBuffer.flip();
            indexChannel.write(tempBuffer, group * INDEX_ENTRY_SIZE);

            int remaining = size;
            int chunk = 0;
            int blockLen = group <= 0xffff ? BLOCK_DATA_SIZE : EXTENDED_BLOCK_DATA_SIZE;
            int headerLen = group <= 0xffff ? BLOCK_HEADER_SIZE : EXTENDED_BLOCK_HEADER_SIZE;
            while (remaining > 0) {
                int nextBlock = 0;
                if (exists) {
                    tempBuffer.position(0).limit(headerLen);
                    dataChannel.read(tempBuffer, block * BLOCK_SIZE);
                    tempBuffer.flip();

                    int currentGroup, currentChunk, currentIndexId;
                    if (group <= 0xFFFF) {
                        currentGroup = tempBuffer.getShort() & 0xFFFF;
                        currentChunk = tempBuffer.getShort() & 0xFFFF;
                        nextBlock = getMediumInt(tempBuffer);
                        currentIndexId = tempBuffer.get() & 0xFF;
                    } else {
                        currentGroup = tempBuffer.getInt();
                        currentChunk = tempBuffer.getShort() & 0xFFFF;
                        nextBlock = getMediumInt(tempBuffer);
                        currentIndexId = tempBuffer.get() & 0xFF;
                    }

                    if (group != currentGroup || chunk != currentChunk || id != currentIndexId) {
                        return false;
                    }
                    if (nextBlock < 0 || nextBlock > dataChannel.size() / BLOCK_SIZE) {
                        return false;
                    }
                }

                if (nextBlock == 0) {
                    exists = false;
                    nextBlock = (int) ((dataChannel.size() + BLOCK_SIZE - 1) / BLOCK_SIZE);
                    if (nextBlock == 0) {
                        nextBlock = 1;
                    }
                    if (nextBlock == block) {
                        nextBlock++;
                    }
                }

                if (remaining <= blockLen) {
                    nextBlock = 0;
                }
                tempBuffer.position(0).limit(BLOCK_SIZE);
                if (group <= 0xFFFF) {
                    tempBuffer.putShort((short) group);
                    tempBuffer.putShort((short) chunk);
                    putMediumInt(tempBuffer, nextBlock);
                    tempBuffer.put((byte) id);
                } else {
                    tempBuffer.putInt(group);
                    tempBuffer.putShort((short) chunk);
                    putMediumInt(tempBuffer, nextBlock);
                    tempBuffer.put((byte) id);
                }

                int blockSize = remaining > blockLen ? blockLen : remaining;
                data.limit(data.position() + blockSize);
                tempBuffer.put(data);
                tempBuffer.flip();

                dataChannel.write(tempBuffer, block * BLOCK_SIZE);
                remaining -= blockSize;
                block = nextBlock;
                chunk++;
            }

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static int getMediumInt(ByteBuffer buffer, int position) {
        return ((buffer.get(position) & 0xFF) << 16)
                | ((buffer.get(position + 1) & 0xFF) << 8)
                | (buffer.get(position + 2) & 0xFF);
    }

    private static int getMediumInt(ByteBuffer buffer) {
        return ((buffer.get() & 0xFF) << 16)
                | ((buffer.get() & 0xFF) << 8)
                | (buffer.get() & 0xFF);
    }

    private static void putMediumInt(ByteBuffer buffer, int val) {
        buffer.put((byte) (val >> 16));
        buffer.put((byte) (val >> 8));
        buffer.put((byte) val);
    }

    public void setMaxSize(int size) {
        this.maxSize = size;
    }

    /**
     * Close's this store.
     */
    public void close() {
        try {
            this.dataChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.indexChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
