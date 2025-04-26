package com.near_reality.osrsbox_db

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Jire
 */
data class ItemEquipment(
    @JsonProperty("attack_stab") val attackStab: Int,
    @JsonProperty("attack_slash") val attackSlash: Int,
    @JsonProperty("attack_crush") val attackCrush: Int,
    @JsonProperty("attack_magic") val attackMagic: Int,
    @JsonProperty("attack_ranged") val attackRanged: Int,
    @JsonProperty("defence_stab") val defenceStab: Int,
    @JsonProperty("defence_slash") val defenceSlash: Int,
    @JsonProperty("defence_crush") val defenceCrush: Int,
    @JsonProperty("defence_magic") val defenceMagic: Int,
    @JsonProperty("defence_ranged") val defenceRanged: Int,
    @JsonProperty("melee_strength") val meleeStrength: Int,
    @JsonProperty("ranged_strength") val rangedStrength: Int,
    @JsonProperty("magic_damage") val magicDamage: Int,
    val prayer: Int,
    val slot: String,
    val requirements: Map<String, String>? = null
)