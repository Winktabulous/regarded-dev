package com.zenyte.net.io

import com.near_reality.buffer.BitBuf
import com.zenyte.net.NetworkConstants
import com.zenyte.net.game.ServerProt
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.DefaultByteBufHolder
import io.netty.util.ByteProcessor
import io.netty.util.ReferenceCountUtil
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.channels.GatheringByteChannel
import java.nio.channels.ScatteringByteChannel
import java.nio.charset.Charset

open class RSBuffer(
    data: ByteBuf
) : DefaultByteBufHolder(data), AutoCloseable {

    override fun close() {
        if (refCnt() > 0)
            ReferenceCountUtil.release(this)
    }

    @JvmOverloads
    constructor(
        initialCapacity: Int = NetworkConstants.INITIAL_SERVER_BUFFER_SIZE,
        maximumCapacity: Int = NetworkConstants.MAX_SERVER_BUFFER_SIZE
    ) : this(ByteBufAllocator.DEFAULT.buffer(initialCapacity, maximumCapacity))

    constructor(prot: ServerProt) : this(prot.initialSize, prot.capacity)

    fun writeBytes128(buffer: RSBuffer) {
        val length = buffer.writerIndex()
        for (i in buffer.readerIndex() until length) {
            writeByte128(buffer.getByte(i).toInt())
        }
    }

    fun writeBytes128Reverse(buffer: RSBuffer) {
        val offset = buffer.readerIndex()
        for (i in buffer.writerIndex() - 1 downTo offset) {
            writeByte128(buffer.getByte(i).toInt())
        }
    }

    fun writeBytes128Reverse(payload: ByteArray) {
        for (index in payload.indices.reversed()) {
            writeByte128(payload[index].toInt())
        }
    }

    private companion object {
        class ReverseBytesProcessor : ByteProcessor {
            var count = 0

            lateinit var dest: ByteBuf
            var length = 0

            override fun process(value: Byte): Boolean {
                if (count++ >= length) return false
                dest.writeByte(value.toInt())
                return true
            }

            fun reset(dest: ByteBuf, length: Int) = apply {
                this.count = 0
                this.dest = dest
                this.length = length
            }
        }

        val reverseBytesProcessor: ThreadLocal<ReverseBytesProcessor> =
            ThreadLocal.withInitial { ReverseBytesProcessor() }
    }

    fun writeBytesReverse(src: ByteBuf, length: Int): Int {
        val reverseBytesProcessor = reverseBytesProcessor.get().reset(content(), length)
        return src.forEachByteDesc(src.readerIndex(), length, reverseBytesProcessor)
    }

    fun writeBytesReverse(src: ByteArray, srcIndex: Int, length: Int) {
        for (index in length - 1 downTo srcIndex) {
            writeByte(src[index].toInt())
        }
    }

    fun writeBytes128Reverse(src: ByteArray, srcIndex: Int, length: Int) {
        for (index in length - 1 downTo srcIndex) {
            writeByte128(src[index].toInt())
        }
    }

    fun writeBytesReverse(buffer: RSBuffer) {
        val offset = buffer.readerIndex()
        for (index in buffer.readableBytes().dec() downTo 0) {
            writeByte(buffer.getByte(offset + index).toInt())
        }
    }

    fun writeBytesReverse(src: ByteBuf, srcIndex: Int, length: Int) {
        val offset = src.readerIndex()
        for (index in length - 1 downTo srcIndex) {
            writeByte(src.getByte(offset + index).toInt())
        }
    }

    fun writeBytes128(buffer: ByteArray) {
        for (b in buffer) {
            writeByte128(b.toInt())
        }
    }

    fun writeVersionedString(s: String) {
        writeVersionedString(s, 0.toByte())
    }

    fun write3UShort(i: Int) {
        // Extract individual bytes from the input data
        val byte1: Int = i and 255 // Extracts the least significant byte
        val byte2: Int = i shr 8 and 255 // Extracts the second byte
        val byte3: Int = i shr 16 and 255 // Extracts the most significant byte
        writeByte(byte1)
        writeByte(byte2)
        writeByte(byte3)
    }

    fun writeUnsignedShort(i: Int) {
        val byte1: Int = i and 255
        val byte2: Int = i shr 8 and 255
        writeByte(byte1)
        writeByte(byte2)
    }

    fun writeVersionedString(string: String, version: Byte) {
        writeByte(version.toInt())
        writeString(string)
    }

    fun writeString(string: String) {
        for (i in 0..string.lastIndex) {
            val c = string[i]
            writeByte(c.code)
        }
        writeByte(0)
    }

    fun writeByte128(i: Int) {
        writeByte(i + 128)
    }

    fun writeByteC(i: Int) {
        writeByte(-i)
    }

    fun write128Byte(i: Int) {
        writeByte(128 - i)
    }

    fun writeShortLE128(i: Int) {
        writeByte(i + 128)
        writeByte(i shr 8)
    }

    fun writeUnsignedShortAdd(i: Int) {
        writeByte(i + 128 and 255)
        writeByte(i shr 8 and 255)
    }

    fun writeShort128(i: Int) {
        writeByte(i shr 8)
        writeByte(i + 128)
    }

    fun writeSmart(i: Int) {
        if (i < 0 || i > Short.MAX_VALUE) {
            throw RuntimeException("A smart can one be within the boundaries of a signed short.")
        }
        if (i >= 0x80) {
            writeShort(i + 0x8000)
        } else {
            writeByte(i)
        }
    }

    fun writeBigSmart(i: Int) {
        if (i >= Short.MAX_VALUE) {
            writeInt(i - Int.MAX_VALUE - 1)
        } else {
            writeShort(if (i >= 0) i else 32767)
        }
    }

    fun write24BitInteger(i: Int) {
        writeByte(i shr 16)
        writeByte(i shr 8)
        writeByte(i)
    }

    fun write24BitIntegerV2(i: Int) {
        writeByte(i shr 16)
        writeByte(i)
        writeByte(i shr 8)
    }

    fun write24BitIntegerV3(i: Int) {
        writeByte(i)
        writeByte(i shr 8)
        writeByte(i shr 16)
    }

    fun writeIntME(i: Int) {
        writeByte(i shr 8)
        writeByte(i)
        writeByte(i shr 24)
        writeByte(i shr 16)
    }

    fun writeIntIME(i: Int) {
        writeByte(i shr 16)
        writeByte(i shr 24)
        writeByte(i)
        writeByte(i shr 8)
    }

    fun write5ByteInteger(l: Long) {
        writeByte((l shr 32).toInt())
        writeByte((l shr 24).toInt())
        writeByte((l shr 16).toInt())
        writeByte((l shr 8).toInt())
        writeByte(l.toInt())
    }

    fun writeDynamic(bytes: Int, l: Long) {
        val bytesD = bytes - 1
        require(!(bytesD < 0 || bytesD > 7))
        var shift = 8 * bytesD
        while (shift >= 0) {
            writeByte((l shr shift).toInt())
            shift -= 8
        }
    }

    fun writeUnsignedSmart(value: Int) {
        if (value < 64 && value >= -64) {
            writeByte(value + 64)
        }
        if (value < 16384 && value >= -16384) {
            writeShort(value + 49152)
        } else {
            println("Error psmart out of range: $value")
        }
    }

    fun readString(): String {
        val content = content()
        val stringBuilder = StringBuilder()
        var nextByte: Byte
        while (content.isReadable) {
            nextByte = readByte()
            if (nextByte == 0.toByte())
                break
            stringBuilder.append(Char(nextByte.toUShort()))
        }
        return stringBuilder.toString()
    }

    @JvmOverloads
    fun readVersionedString(versionNumber: Byte = 0.toByte()): String {
        check(readByte() == versionNumber) { "Bad string version number!" }
        return readString()
    }

    /**
     * Reads a type C byte.
     *
     * @return A type C byte.
     */
    fun readByteC(): Byte {
        return (-readByte()).toByte()
    }

    /**
     * Gets a type S byte.
     *
     * @return A type S byte.
     */
    fun read128Byte(): Byte {
        return (128 - readByte()).toByte()
    }

    /**
     * Reads a little-endian type A short.
     *
     * @return A little-endian type A short.
     */
    fun readShortLE128(): Short {
        var i = readByte() - 128 and 0xFF or ((readByte() and 0xFF) shl 8)
        if (i > 32767) {
            i -= 0x10000
        }
        return i.toShort()
    }

    /**
     * Reads a V1 integer.
     *
     * @return A V1 integer.
     */
    fun readIntME(): Int {
        return ((readByte() and 0xff) shl 8) or (readByte() and 0xff) or ((readByte() and 0xff) shl 24) or ((readByte() and 0xff) shl 16)
    }

    /**
     * Reads a V2 integer.
     *
     * @return A V2 integer.
     */
    fun readIntIME(): Int {
        return ((readByte() and 0xff) shl 16) or ((readByte() and 0xff) shl 24) or (readByte() and 0xff) or ((readByte() and 0xff) shl 8)
    }

    /**
     * Reads a 24-bit integer.
     *
     * @return A 24-bit integer.
     */
    fun read24BitInt(): Int {
        return (readByte() shl 16) + (readByte() shl 8) + readByte()
    }

    fun toString(charset: Charset?): String = content().toString(charset)

    fun toString(index: Int, length: Int, charset: Charset?): String = content().toString(index, length, charset)

    fun compareTo(other: ByteBuf?) = content().compareTo(other)

    fun capacity() = content().capacity()

    fun capacity(newCapacity: Int) = content().capacity(newCapacity)

    fun maxCapacity() = content().maxCapacity()

    fun alloc(): ByteBufAllocator = content().alloc()

    @Deprecated("Deprecated in Java", ReplaceWith("content().order()"))
    fun order(): ByteOrder = @Suppress("DEPRECATION") content().order()

    @Deprecated("Deprecated in Java", ReplaceWith("apply { content().order(endianness) }"))
    fun order(endianness: ByteOrder?) = apply { @Suppress("DEPRECATION") content().order(endianness) }

    fun unwrap() = apply { content().unwrap() }

    fun isDirect() = content().isDirect

    fun isReadOnly() = content().isReadOnly

    fun asReadOnly() = apply { content().asReadOnly() }

    fun readerIndex() = content().readerIndex()

    fun readerIndex(readerIndex: Int) = apply { content().readerIndex(readerIndex) }

    fun writerIndex() = content().writerIndex()

    fun writerIndex(writerIndex: Int) = apply { content().writerIndex(writerIndex) }

    fun setIndex(readerIndex: Int, writerIndex: Int) = apply { content().setIndex(readerIndex, writerIndex) }

    fun readableBytes() = content().readableBytes()

    fun writableBytes() = content().writableBytes()

    fun maxWritableBytes() = content().maxWritableBytes()

    fun isReadable() = content().isReadable

    fun isReadable(size: Int) = content().isReadable(size)

    fun isWritable() = content().isWritable

    fun isWritable(size: Int) = content().isWritable(size)

    fun clear() = apply { content().clear() }

    fun markReaderIndex() = apply { content().markReaderIndex() }

    fun resetReaderIndex() = apply { content().resetReaderIndex() }

    fun markWriterIndex() = apply { content().markWriterIndex() }

    fun resetWriterIndex() = apply { content().resetWriterIndex() }

    fun discardReadBytes() = apply { content().discardReadBytes() }

    fun discardSomeReadBytes() = apply { content().discardSomeReadBytes() }

    fun ensureWritable(minWritableBytes: Int) = apply { content().ensureWritable(minWritableBytes) }

    fun ensureWritable(minWritableBytes: Int, force: Boolean) = content().ensureWritable(minWritableBytes, force)

    fun getBoolean(index: Int) = content().getBoolean(index)

    fun getByte(index: Int) = content().getByte(index)

    fun getUnsignedByte(index: Int) = content().getUnsignedByte(index)

    fun getShort(index: Int) = content().getShort(index)

    fun getShortLE(index: Int) = content().getShortLE(index)

    fun getUnsignedShort(index: Int) = content().getUnsignedShort(index)

    fun getUnsignedShortLE(index: Int) = content().getUnsignedShortLE(index)

    fun getMedium(index: Int) = content().getMedium(index)

    fun getMediumLE(index: Int) = content().getMediumLE(index)

    fun getUnsignedMedium(index: Int) = content().getUnsignedMedium(index)

    fun getUnsignedMediumLE(index: Int) = content().getUnsignedMediumLE(index)

    fun getInt(index: Int) = content().getInt(index)

    fun getIntLE(index: Int) = content().getIntLE(index)

    fun getUnsignedInt(index: Int) = content().getUnsignedInt(index)

    fun getUnsignedIntLE(index: Int) = content().getUnsignedIntLE(index)

    fun getLong(index: Int) = content().getLong(index)

    fun getLongLE(index: Int) = content().getLongLE(index)

    fun getChar(index: Int) = content().getChar(index)

    fun getFloat(index: Int) = content().getFloat(index)

    fun getDouble(index: Int) = content().getDouble(index)

    fun getBytes(index: Int, dst: ByteBuf?) = apply { content().getBytes(index, dst) }

    fun getBytes(index: Int, dst: ByteBuf?, length: Int) = apply { content().getBytes(index, dst, length) }

    fun getBytes(index: Int, dst: ByteBuf?, dstIndex: Int, length: Int) = apply {
        content().getBytes(index, dst, dstIndex, length)
    }

    fun getBytes(index: Int, dst: ByteArray?) = apply { content().getBytes(index, dst) }

    fun getBytes(index: Int, dst: ByteArray?, dstIndex: Int, length: Int) = apply {
        content().getBytes(index, dst, dstIndex, length)
    }

    fun getBytes(index: Int, dst: ByteBuffer?) = apply { content().getBytes(index, dst) }

    fun getBytes(index: Int, out: OutputStream?, length: Int) = apply { content().getBytes(index, out, length) }

    fun getBytes(index: Int, out: GatheringByteChannel?, length: Int) = content().getBytes(index, out, length)

    fun getBytes(index: Int, out: FileChannel?, position: Long, length: Int) =
        content().getBytes(index, out, position, length)

    fun getCharSequence(index: Int, length: Int, charset: Charset?): CharSequence =
        content().getCharSequence(index, length, charset)

    fun setBoolean(index: Int, value: Boolean) = apply { content().setBoolean(index, value) }

    fun setByte(index: Int, value: Int) = apply { content().setByte(index, value) }

    fun setShort(index: Int, value: Int) = apply { content().setShort(index, value) }

    fun setShortLE(index: Int, value: Int) = apply { content().setShortLE(index, value) }

    fun setMedium(index: Int, value: Int) = apply { content().setMedium(index, value) }

    fun setMediumLE(index: Int, value: Int) = apply { content().setMediumLE(index, value) }

    fun setInt(index: Int, value: Int) = apply { content().setInt(index, value) }

    fun setIntLE(index: Int, value: Int) = apply { content().setIntLE(index, value) }

    fun setLong(index: Int, value: Long) = apply { content().setLong(index, value) }

    fun setLongLE(index: Int, value: Long) = apply { content().setLongLE(index, value) }

    fun setChar(index: Int, value: Int) = apply { content().setChar(index, value) }

    fun setFloat(index: Int, value: Float) = apply { content().setFloat(index, value) }

    fun setDouble(index: Int, value: Double) = apply { content().setDouble(index, value) }

    fun setBytes(index: Int, src: ByteBuf?) = apply { content().setBytes(index, src) }

    fun setBytes(index: Int, src: ByteBuf?, length: Int) = apply { content().setBytes(index, src, length) }

    fun setBytes(index: Int, src: ByteBuf?, srcIndex: Int, length: Int) =
        apply { content().setBytes(index, src, srcIndex, length) }

    fun setBytes(index: Int, src: ByteArray?) = apply { content().setBytes(index, src) }

    fun setBytes(index: Int, src: ByteArray?, srcIndex: Int, length: Int) =
        apply { content().setBytes(index, src, srcIndex, length) }

    fun setBytes(index: Int, src: ByteBuffer?) = apply { content().setBytes(index, src) }

    fun setBytes(index: Int, `in`: InputStream?, length: Int) = content().setBytes(index, `in`, length)

    fun setBytes(index: Int, `in`: ScatteringByteChannel?, length: Int) = content().setBytes(index, `in`, length)

    fun setBytes(index: Int, `in`: FileChannel?, position: Long, length: Int) =
        content().setBytes(index, `in`, position, length)

    fun setZero(index: Int, length: Int) = apply { content().setZero(index, length) }

    fun setCharSequence(index: Int, sequence: CharSequence?, charset: Charset?) =
        content().setCharSequence(index, sequence, charset)

    fun readBoolean() = content().readBoolean()

    fun readByte() = content().readByte()

    fun readUnsignedByte() = content().readUnsignedByte()

    fun readShort() = content().readShort()

    fun readShortLE() = content().readShortLE()

    fun readUnsignedShort() = content().readUnsignedShort()

    fun readUnsignedShortLE() = content().readUnsignedShortLE()

    fun readMedium() = content().readMedium()

    fun readMediumLE() = content().readMediumLE()

    fun readUnsignedMedium() = content().readUnsignedMedium()

    fun readUnsignedMediumLE() = content().readUnsignedMediumLE()

    fun readInt() = content().readInt()

    fun readIntLE() = content().readIntLE()

    fun readUnsignedInt() = content().readUnsignedInt()

    fun readUnsignedIntLE() = content().readUnsignedIntLE()

    fun readLong() = content().readLong()

    fun readLongLE() = content().readLongLE()

    fun readChar() = content().readChar()

    fun readFloat() = content().readFloat()

    fun readDouble() = content().readDouble()

    fun readBytes(length: Int) = apply { content().readBytes(length) }

    fun readBytes(dst: ByteBuf?) = apply { content().readBytes(dst) }

    fun readBytes(dst: ByteBuf?, length: Int) = apply { content().readBytes(dst, length) }

    fun readBytes(dst: ByteBuf?, dstIndex: Int, length: Int) = apply { content().readBytes(dst, dstIndex, length) }

    fun readBytes(dst: ByteArray?) = apply { content().readBytes(dst) }

    fun readBytes(dst: ByteArray?, dstIndex: Int, length: Int) =
        apply { content().readBytes(dst, dstIndex, length) }

    fun readBytes(dst: ByteBuffer?) = apply { content().readBytes(dst) }

    fun readBytes(out: OutputStream?, length: Int) = apply { content().readBytes(out, length) }

    fun readBytes(out: GatheringByteChannel?, length: Int) = content().readBytes(out, length)

    fun readBytes(out: FileChannel?, position: Long, length: Int) = content().readBytes(out, position, length)

    fun readSlice(length: Int) = apply { content().readSlice(length) }

    fun readRetainedSlice(length: Int) = apply { content().readRetainedSlice(length) }

    fun readCharSequence(length: Int, charset: Charset?): CharSequence = content().readCharSequence(length, charset)

    fun skipBytes(length: Int) = apply { content().skipBytes(length) }

    fun writeBoolean(value: Boolean) = apply { content().writeBoolean(value) }

    fun writeByte(value: Int) = apply { content().writeByte(value) }

    fun writeShort(value: Int) = apply { content().writeShort(value) }

    fun writeShortLE(value: Int) = apply { content().writeShortLE(value) }

    fun writeMedium(value: Int) = apply { content().writeMedium(value) }

    fun writeMediumLE(value: Int) = apply { content().writeMediumLE(value) }

    fun writeInt(value: Int) = apply { content().writeInt(value) }

    fun writeIntLE(value: Int) = apply { content().writeIntLE(value) }

    fun writeLong(value: Long) = apply { content().writeLong(value) }

    fun writeLongLE(value: Long) = apply { content().writeLongLE(value) }

    fun writeChar(value: Int) = apply { content().writeChar(value) }

    fun writeFloat(value: Float) = apply { content().writeFloat(value) }

    fun writeDouble(value: Double) = apply { content().writeDouble(value) }

    fun writeBytes(src: ByteBuf?) = apply { content().writeBytes(src) }

    fun writeBytes(src: ByteBuf?, length: Int) = apply { content().writeBytes(src, length) }

    fun writeBytes(src: ByteBuf?, srcIndex: Int, length: Int) =
        apply { content().writeBytes(src, srcIndex, length) }

    fun writeBytes(src: ByteArray?) = apply { content().writeBytes(src) }

    fun writeBytes(src: ByteArray?, srcIndex: Int, length: Int) =
        apply { content().writeBytes(src, srcIndex, length) }

    fun writeBytes(src: ByteBuffer?) = apply { content().writeBytes(src) }

    fun writeBytes(`in`: InputStream?, length: Int) = content().writeBytes(`in`, length)

    fun writeBytes(`in`: ScatteringByteChannel?, length: Int) = content().writeBytes(`in`, length)

    fun writeBytes(`in`: FileChannel?, position: Long, length: Int) = content().writeBytes(`in`, position, length)

    fun writeZero(length: Int) = apply { content().writeZero(length) }

    fun writeCharSequence(sequence: CharSequence?, charset: Charset?) =
        content().writeCharSequence(sequence, charset)

    fun indexOf(fromIndex: Int, toIndex: Int, value: Byte) = content().indexOf(fromIndex, toIndex, value)

    fun bytesBefore(value: Byte) = content().bytesBefore(value)

    fun bytesBefore(length: Int, value: Byte) = content().bytesBefore(length, value)

    fun bytesBefore(index: Int, length: Int, value: Byte) = content().bytesBefore(index, length, value)

    fun forEachByte(processor: ByteProcessor?) = content().forEachByte(processor)

    fun forEachByte(index: Int, length: Int, processor: ByteProcessor?) =
        content().forEachByte(index, length, processor)

    fun forEachByteDesc(processor: ByteProcessor?) = content().forEachByteDesc(processor)
    fun forEachByteDesc(index: Int, length: Int, processor: ByteProcessor?) =
        content().forEachByteDesc(index, length, processor)

    fun copy(index: Int, length: Int) = apply { content().copy(index, length) }

    fun slice() = apply { content().slice() }

    fun slice(index: Int, length: Int) = apply { content().slice(index, length) }

    fun retainedSlice() = apply { content().retainedSlice() }

    fun retainedSlice(index: Int, length: Int) = apply { content().retainedSlice(index, length) }

    fun nioBufferCount() = content().nioBufferCount()

    fun nioBuffer(): ByteBuffer = content().nioBuffer()

    fun nioBuffer(index: Int, length: Int): ByteBuffer = content().nioBuffer(index, length)

    fun internalNioBuffer(index: Int, length: Int): ByteBuffer = content().internalNioBuffer(index, length)

    fun nioBuffers(): Array<ByteBuffer> = content().nioBuffers()

    fun nioBuffers(index: Int, length: Int): Array<ByteBuffer> = content().nioBuffers(index, length)

    fun hasArray() = content().hasArray()

    fun array(): ByteArray = content().array()

    fun arrayOffset() = content().arrayOffset()

    fun hasMemoryAddress() = content().hasMemoryAddress()

    fun memoryAddress() = content().memoryAddress()

    /**
     * Gets a 3-byte integer.
     *
     * @return The 3-byte integer.
     */
    val triByte: Int
        get() = readByte() shl 16 and 0xFF or (readByte() shl 8 and 0xFF) or (readByte() and 0xFF)

    /**
     * Reads a type A byte.
     *
     * @return A type A byte.
     */
    fun readByte128(): Byte {
        return (readByte() - 128).toByte()
    }

    /**
     * Reads a type A short.
     *
     * @return A type A short.
     */
    fun readShort128(): Short {
        var i: Int = readByte() and 0xFF shl 8 or (readByte() - 128 and 0xFF)
        if (i > 32767) {
            i -= 0x10000
        }
        return i.toShort()
    }

    /**
     * Reads a series of bytes in reverse.
     *
     * @param is     The target byte array.
     * @param offset The offset.
     * @param length The length.
     */
    fun getReverse(`is`: ByteArray, offset: Int, length: Int) {
        for (i in offset + length - 1 downTo offset) {
            `is`[i] = readByte()
        }
    }

    /**
     * Reads a series of type A bytes in reverse.
     *
     * @param is     The target byte array.
     * @param offset The offset.
     * @param length The length.
     */
    fun getReverseA(`is`: ByteArray, offset: Int, length: Int) {
        for (i in offset + length - 1 downTo offset) {
            `is`[i] = readByte128()
        }
    }

    /**
     * Reads a series of bytes.
     *
     * @param is     The target byte array.
     * @param offset The offset.
     * @param length The length.
     */
    operator fun get(`is`: ByteArray, offset: Int, length: Int) {
        for (i in 0 until length) {
            `is`[offset + i] = readByte()
        }
    }
    fun readUnsignedShortSmart(): Int {
        val peek = getUnsignedByte(readerIndex()).toInt()
        return if ((peek and 0x80) == 0) {
            readUnsignedByte().toInt()
        } else {
            readUnsignedShort() and 0x7FFF
        }
    }

    /**
     * Gets a smart.
     *
     * @return The smart.
     */
    val smart: Int
        get() {
            val peek: Int = getByte(readerIndex()) and 0xFF
            return if (peek < 128) {
                readUnsignedByte().toInt()
            } else {
                readUnsignedShort() - 0x8000
            }
        }

    /**
     * Gets a signed smart.
     *
     * @return The signed smart.
     */
    val signedSmart: Int
        get() {
            val peek: Int = getByte(readerIndex()) and 0xFF
            return if (peek < 128) {
                (readByte() and 0xFF) - 64
            } else {
                (readShort() and 0xFFFF) - 49152
            }
        }

    fun writeBits(bitBuf: BitBuf) {
        val byteBuf = bitBuf.buf
        val writer = bitBuf.writerIndex() + 7 shr 3
        for (i in bitBuf.readerIndex() shr 3 until writer) {
            writeByte(byteBuf.getByte(i.toInt()).toInt())
        }
        bitBuf.clear()
    }

}
