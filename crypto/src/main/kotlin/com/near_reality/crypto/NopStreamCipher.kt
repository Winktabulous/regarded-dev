package com.near_reality.crypto

object NopStreamCipher : StreamCipher {
    override fun nextInt() = 0
}