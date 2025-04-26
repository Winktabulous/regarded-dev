package com.near_reality.osrsbox_db

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Jire
 */
data class ItemWeapon(
    @JsonProperty("attack_speed") val attackSpeed: Int,
    @JsonProperty("weapon_type") val weaponType: String,
    val stances: Array<ItemDefinitionStance>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemWeapon

        if (attackSpeed != other.attackSpeed) return false
        if (weaponType != other.weaponType) return false
        if (!stances.contentEquals(other.stances)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = attackSpeed
        result = 31 * result + weaponType.hashCode()
        result = 31 * result + stances.contentHashCode()
        return result
    }

}