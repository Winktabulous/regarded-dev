package com.near_reality.network.login.packet

import com.near_reality.net.HardwareInfo
import com.near_reality.network.login.AuthenticatorInfo
import com.near_reality.network.login.ClientSettings
import com.near_reality.network.login.LoginCiphers
import com.zenyte.CacheManager
import com.zenyte.game.GameConstants
import com.zenyte.game.world.World
import com.zenyte.net.ClientResponse
import com.zenyte.net.NetworkConstants
import com.zenyte.net.PacketIn
import com.zenyte.net.login.LoginType
import com.zenyte.utils.TextUtils
import com.zenyte.utils.TimeUnit

/**
 * @author Jire
 */
data class LoginPacketIn(
    val type: LoginType,
    val version: Int,
    val subVersion: Int,

    val username: String,
    val password: String,

    val clientSettings: ClientSettings,
    val uniqueId: ByteArray,
    val hardwareInfo: HardwareInfo,

    val authInfo: AuthenticatorInfo?,

    val crcs: IntArray,

    val sessionToken: String,

    val previousXTEAKeys: IntArray,

    val ciphers: LoginCiphers,
) : PacketIn {

    fun getResponse(): ClientResponse? {

        if (!GameConstants.WORLD_PROFILE.isBeta()) {
            for (i in 0..crcs.lastIndex) {
                if (i == 16) continue // Index 16 is never updated so ignore it entirely.
                val crc = crcs[i]
                if (crc != 0 && crc != CacheManager.getCRC(i))
                    return ClientResponse.SERVER_UPDATED
            }
        }

        if (NetworkConstants.SESSION_TOKEN != sessionToken)
            return ClientResponse.BAD_SESSION_ID

        if (World.getPlayers().size >= NetworkConstants.PLAYER_CAP)
            return ClientResponse.WORLD_FULL

        if (World.isUpdating() && World.getUpdateTimer() < TimeUnit.MINUTES.toTicks(1))
            return ClientResponse.UPDATE_IN_PROGRESS

        val formattedUsername = TextUtils.formatNameForProtocol(username)
        if (formattedUsername.isEmpty() || formattedUsername.length > 12 || !TextUtils.isValidName(formattedUsername))
            return ClientResponse.INVALID_USERNAME_OR_PASSWORD

        return null
    }
}
