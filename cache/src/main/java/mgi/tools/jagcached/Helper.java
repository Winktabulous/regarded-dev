package mgi.tools.jagcached;

import mgi.utilities.ByteBuffer;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Helper {

    private static final CRC32 crc32 = new CRC32();

    public static final int COMPRESSION_NONE = 0;
    public static final int COMPRESSION_BZIP2 = 1;
    public static final int COMPRESSION_GZIP = 2;

    public static void decryptContainer(byte[] container, int[] keys) {
        decryptContainer(new ByteBuffer(container), keys);
    }

    public static void decryptContainer(ByteBuffer container, int[] keys) {
        container.xteaDecrypt(keys, 5, container.getBuffer().length - 2);
    }

    public static void encryptContainer(byte[] container, int[] keys) {
        encryptContainer(new ByteBuffer(container), keys);
    }

    public static void encryptContainer(ByteBuffer container, int[] keys) {
        container.encryptXTEA(keys, 5, container.getBuffer().length - 2);
    }

    public static byte[] decodeFilesContainer(byte[] data) {
        return decodeFilesContainer(new ByteBuffer(data)).getBuffer();
    }

    public static ByteBuffer decodeFilesContainer(ByteBuffer container) {
        return decodeContainer(container, false);
    }

    public static byte[] decodeFITContainer(byte[] data) {
        return decodeFITContainer(new ByteBuffer(data)).getBuffer();
    }

    public static ByteBuffer decodeFITContainer(ByteBuffer container) {
        return decodeContainer(container, true);
    }

    private static ByteBuffer decodeContainer(ByteBuffer container, boolean isFITContainer) {
        container.setPosition(0);

        final int compressionType = container.readUnsignedByte();
        if (compressionType > COMPRESSION_GZIP)
            throw new RuntimeException("Unknown compression type:" + compressionType);

        final int length = container.readInt();
        if (length < 0) return null;

        switch (compressionType) {
            default: {
                final byte[] data = new byte[length];
                container.readBytes(data, 0, length);
                return new ByteBuffer(data);
            }
            case COMPRESSION_BZIP2: {
                final int decompressedLength = container.readInt();
                if (decompressedLength < 0) return null;

                final byte[] data = new byte[decompressedLength];

                // we need to add header
                final int relocOffset = 2;
                final byte[] reloc = new byte[length + relocOffset];
                reloc[0] = 'h';
                reloc[1] = '1';
                System.arraycopy(container.getBuffer(), container.getPosition(), reloc, relocOffset, length);
                try (DataInputStream stream = new DataInputStream(
                        new CBZip2InputStream(new ByteArrayInputStream(reloc)))) {
                    stream.readFully(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return new ByteBuffer(data);
            }
            case COMPRESSION_GZIP: {
                final int decompressedLength = container.readInt();
                if (decompressedLength < 0) return null;

                final byte[] data = new byte[decompressedLength];

                try (DataInputStream stream = new DataInputStream(
                        new GZIPInputStream(new ByteArrayInputStream(
                                container.getBuffer(), container.getPosition(), length)))) {
                    stream.readFully(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return new ByteBuffer(data);
            }
        }
    }

    /**
     * Does basic verification and decided if container is valid.
     */
    public static boolean validFilesContainer(ByteBuffer container) {
        if (container.getBuffer().length < 7)
            return false;
        container.setPosition(0);
        int compressionType = container.readUnsignedByte();
        if (compressionType > COMPRESSION_GZIP)
            return false;
        int length = container.readInt();
        if (compressionType != COMPRESSION_NONE) {
            if ((container.getBuffer().length - container.getPosition()) < 4)
                return false;
            container.readInt();
        }
        return length >= 0 && (container.getBuffer().length - container.getPosition() - length - 2) == 0;

    }

    public static int decodeVersion(ByteBuffer container) {
        container.setPosition(0);
        int compressionType = container.readUnsignedByte();
        if (compressionType > COMPRESSION_GZIP)
            throw new RuntimeException("Unknown compression type:" + compressionType);
        int length = container.readInt();
        if (compressionType != COMPRESSION_NONE)
            container.readInt(); // decompressed length
        container.setPosition(container.getPosition() + length);
        return container.readUnsignedShort();
    }

    public static byte[] encodeFilesContainer(byte[] file, int fileVersion) {
        return encodeFilesContainer(new ByteBuffer(file), fileVersion).getBuffer();
    }

    public static byte[] encodeFilesContainer(byte[] file, int fileVersion, int compressionType) {
        return encodeFilesContainer(new ByteBuffer(file), fileVersion, compressionType).getBuffer();
    }

    public static ByteBuffer encodeFilesContainer(ByteBuffer file, int fileVersion) {
        return encodeFilesContainer(file, fileVersion, decideCompression(file));
    }

    public static ByteBuffer encodeFilesContainer(ByteBuffer file, int fileVersion, int compressionType) {
        return encodeContainer(file, fileVersion, compressionType, false);
    }

    public static byte[] encodeFITContainer(byte[] file, int fileVersion) {
        return encodeFITContainer(new ByteBuffer(file), fileVersion).getBuffer();
    }

    public static byte[] encodeFITContainer(byte[] file, int fileVersion, int compressionType) {
        return encodeFITContainer(new ByteBuffer(file), fileVersion, compressionType).getBuffer();
    }

    public static ByteBuffer encodeFITContainer(ByteBuffer file, int fileVersion) {
        return encodeFITContainer(file, fileVersion, decideCompression(file));
    }

    public static ByteBuffer encodeFITContainer(ByteBuffer file, int fileVersion, int compressionType) {
        return encodeContainer(file, fileVersion, compressionType, true);
    }

    private static ByteBuffer encodeContainer(ByteBuffer file, int fileVersion, int compressionType,
                                              boolean isFITContainer) {
        if (compressionType > COMPRESSION_GZIP)
            throw new RuntimeException("Unknown compression type:" + compressionType);

        int allocLength = 1 + 4 + (!isFITContainer ? 2 : 0);
        int fileLength = file.getBuffer().length;
        if (compressionType != COMPRESSION_NONE) {
            allocLength += 4;
            try {
                if (compressionType == COMPRESSION_GZIP) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    GZIPOutputStream out = new GZIPOutputStream(baos);
                    out.write(file.getBuffer());
                    out.finish();
                    out.close();
                    file = new ByteBuffer(baos.toByteArray());
                    baos.close();
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    CBZip2OutputStream out = new CBZip2OutputStream(baos, 1);
                    out.write(file.getBuffer());
                    out.close();
                    byte[] data = baos.toByteArray(); // we need to remove h1
                    byte[] reloc = new byte[data.length - 2];
                    System.arraycopy(data, 2, reloc, 0, reloc.length);
                    file = new ByteBuffer(reloc);
                    baos.close();
                }
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        }
        allocLength += file.getBuffer().length;

        ByteBuffer container = new ByteBuffer(allocLength);
        container.writeByte(compressionType);
        container.writeInt(file.getBuffer().length);
        if (compressionType != COMPRESSION_NONE)
            container.writeInt(fileLength);
        container.writeBytes(file.getBuffer(), 0, file.getBuffer().length);
        if (!isFITContainer) {
            container.writeShort(fileVersion);
        }
        return container;
    }

    public static int crc32(byte[] data, int offset, int length) {
        return crc32(new ByteBuffer(data), offset, length);
    }

    public static int crc32(ByteBuffer data, int offset, int length) {
        crc32.update(data.getBuffer(), offset, length);
        int v = (int) crc32.getValue();
        crc32.reset();
        return v;
    }

    public static int strToI(String str) {
        if (str == null)
            return -1;

        // same as return str.hashcode();
        int i = 0;
        for (int a = 0; a < str.length(); a++)
            i = (31 * i) + str.charAt(a);
        return i;
    }

    private static int decideCompression(ByteBuffer data) {
        if (data.getBuffer().length < 100)
            return COMPRESSION_NONE;
        return COMPRESSION_GZIP; // bzip2 is unreliable
    }
}
