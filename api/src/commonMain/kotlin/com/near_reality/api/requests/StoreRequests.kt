package com.near_reality.api.requests

import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.model.CreditStoreCartItem
import kotlinx.serialization.Serializable

@Serializable
data class CreditPackageOrderCreateRequest(
    val userId: Long,
    val paymentMethod: CreditPackageOrder.PaymentMethod,
    val creditPackageId: Int,
    val creditPackageAmount: Int
)

@Serializable
data class StoreCheckoutRequest(
    val userId: Long,
    val cart: List<CreditStoreCartItem>
)
