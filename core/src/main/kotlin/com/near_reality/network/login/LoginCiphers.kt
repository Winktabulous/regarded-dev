package com.near_reality.network.login

import com.near_reality.crypto.StreamCipher

/**
 * @author Jire
 */
data class LoginCiphers(
    val encode: StreamCipher,
    val decode: StreamCipher
)