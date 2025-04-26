package com.near_reality.tools

import io.netty.buffer.Unpooled
import java.util.*

/**
 * RuneScape uses a 24-byte UUID, however the standard UUID generation algorithms use 16 bytes,
 * it is unknown what algorithm RuneScape uses, but we will stick to the default implementation.
 *
 * This is the value stored in the `random.dat` file in the user's home directory.
 *
 * When it does not exist
 *
 * @author Stan van der Bend
 */
object PlayerUUID {

    val EMPTY = ByteArray(24) { -1 }

    fun generateUUID(): ByteArray =  UUID.randomUUID().run {
        Unpooled.wrappedBuffer(ByteArray(24)).run {
            writerIndex(0)
            writeLong(mostSignificantBits)
            writeLong(leastSignificantBits)
            array()
        }
    }

    fun isEmpty(byteArray: ByteArray) =
        byteArray.contentEquals(EMPTY)

    @JvmStatic
    fun main(args: Array<String>) {
        "".encodeToByteArray()
    }
}
