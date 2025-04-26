package com.near_reality.buffer.generator

enum class ByteOrder(val suffix: String) {
    BIG(""),
    LITTLE("LE"),
    ALT3("Alt3"),
    ALT3_REVERSE("Alt3Reverse");

    fun getShift(i: Int, width: Int): Int {
        return when (this) {
            BIG -> (width - i - 1) * 8
            LITTLE -> i * 8
            ALT3 -> {
                require(width == 4)
                when (i) {
                    0 -> 16
                    1 -> 24
                    2 -> 0
                    else -> 8
                }
            }
            ALT3_REVERSE -> {
                require(width == 4)
                when (i) {
                    0 -> 8
                    1 -> 0
                    2 -> 24
                    else -> 16
                }
            }
        }
    }
}
