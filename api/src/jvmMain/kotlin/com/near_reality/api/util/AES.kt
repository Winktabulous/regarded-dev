package com.near_reality.api.util

import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
object AES {

    private var secretKey: SecretKeySpec? = null
    private lateinit var key: ByteArray
    private val semaphore = Semaphore(1)

    fun setKey(myKey: String) {
        var sha: MessageDigest? = null
        try {
            key = myKey.toByteArray(StandardCharsets.UTF_8)
            sha = MessageDigest.getInstance("SHA-1")
            key = sha.digest(key)
            key = Arrays.copyOf(key, 16)
            secretKey = SecretKeySpec(key, "AES")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    suspend fun encrypt(strToEncrypt: String, secret: String): String? = semaphore.withPermit {
        try {
            setKey(secret)
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8)))
        } catch (e: Exception) {
            println("Error while encrypting: $e")
        }
        return null
    }

    suspend fun decrypt(strToDecrypt: String, secret: String): String = semaphore.withPermit {
        setKey(secret)
        val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))
    }
}
