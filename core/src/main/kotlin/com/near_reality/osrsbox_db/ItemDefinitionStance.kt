package com.near_reality.osrsbox_db

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Jire
 */
data class ItemDefinitionStance(
    @JsonProperty("combat_style") val combatStyle: String? = null,
    @JsonProperty("attack_type") val attackType: String? = null,
    @JsonProperty("attack_style") val attackStyle: String? = null,
    val experience: String? = null,
    val boosts: String? = null
)