package com.near_reality.cache_tool

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Jire
 */
data class XTEA(
    val archive: Int,
    val group: Int,
    @JsonProperty("name_hash") val nameHash: Int,
    val name: String,
    @JsonProperty("mapsquare") val regionID: Int,
    val key: IntArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XTEA

        if (archive != other.archive) return false
        if (group != other.group) return false
        if (nameHash != other.nameHash) return false
        if (name != other.name) return false
        if (regionID != other.regionID) return false
        if (!key.contentEquals(other.key)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = archive
        result = 31 * result + group
        result = 31 * result + nameHash
        result = 31 * result + name.hashCode()
        result = 31 * result + regionID
        result = 31 * result + key.contentHashCode()
        return result
    }

}