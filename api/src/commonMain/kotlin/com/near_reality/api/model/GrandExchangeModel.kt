package com.near_reality.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeItemPrice(
    val id: Int,
    val price: Int,
    val name: String
)

@Serializable
data class ExchangeOffer(
    val item: Item,
    val slot: Int,
    val price: Int,
    val type: ExchangeType,
    val username: String,
    val amount: Int,
    val updated: Boolean,
    val aborted: Boolean,
    val cancelled: Boolean,
    val container: ItemContainer,
) {
    val remainder get() = item.amount - amount
}

@Serializable
enum class ExchangeType {
    BUYING,
    SELLING
}
