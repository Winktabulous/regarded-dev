package com.near_reality.osrsbox_db

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Jire
 */
data class MonsterDrop(
    val id: Int? = null,
    val name: String,
    val members: Boolean,
    val quantity: String? = null,
    val noted: Boolean,
    val rarity: String? = null,
    val rolls: Int? = null,
    @JsonProperty("drop_requirements") val dropRequirements: String? = null
)
