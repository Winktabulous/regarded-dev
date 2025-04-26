package com.near_reality.network.login

import com.zenyte.net.login.AuthType

/**
 * @author Jire
 */
data class AuthenticatorInfo(
    val type: AuthType,
    val code: Int,
    val identifier: Int
)