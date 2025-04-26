package com.near_reality.api.requests

import com.near_reality.api.model.Skill
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(val username: String, val password: String, val ip: String, val uuid: ByteArray? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UserLoginRequest

        if (username != other.username) return false
        if (password != other.password) return false
        if (ip != other.ip) return false
        if (uuid != null) {
            if (other.uuid == null) return false
            if (!uuid.contentEquals(other.uuid)) return false
        } else if (other.uuid != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + ip.hashCode()
        result = 31 * result + (uuid?.contentHashCode() ?: 0)
        return result
    }
}

@Serializable
data class UserUpdateHiScoresRequest(val skills: List<Skill>)
