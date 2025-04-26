package com.near_reality.crypto

import java.security.SecureRandom

/**
 * @author Jire
 */
object ThreadLocalSecureRandom {

    private val threadLocal = ThreadLocal.withInitial { SecureRandom() }

    operator fun invoke(): SecureRandom = threadLocal.get()

}