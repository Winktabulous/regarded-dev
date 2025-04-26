package com.near_reality.network.proofofwork

import org.apache.commons.codec.digest.DigestUtils

/**
 * @author Kris
 * @author Jire
 */
data class ProofOfWork(
    val difficulty: Int,
    val text: String
) {

    fun validate(nonce: Long): Boolean {
        val stringToHash = 1.toHexString() + difficulty.toHexString() + text + nonce.toInt().toHexString()
        val hashedString = DigestUtils.sha256(stringToHash)
        return hashedString.getLeadingZeroBits() >= difficulty
    }

    private fun Int.toHexString(): String = Integer.toHexString(this)

    private fun ByteArray.getLeadingZeroBits(): Int {
        var bits = 0
        forEach { value ->
            val numberOfTrailingBits = value.getLeadingZeroBits().also { bits += it }
            if (numberOfTrailingBits != 8) return bits
        }
        return bits
    }

    private fun Byte.getLeadingZeroBits(): Int = when (val value = this.toInt()) {
        0 -> 8
        else -> {
            var bits = 0
            var shiftedValue = value and 0xFF
            while ((shiftedValue and 0x80) == 0) {
                bits++
                shiftedValue = shiftedValue shl 1
            }
            bits
        }
    }

}