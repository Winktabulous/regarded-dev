package com.near_reality.api.responses

import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.model.CreditStoreCartItem
import com.near_reality.api.model.User
import kotlinx.serialization.Serializable

@Serializable
sealed class StoreOrderUpdateResponse {
    @Serializable data object PurchaseNotFound : StoreOrderUpdateResponse()
    @Serializable data object Updated : StoreOrderUpdateResponse()
}

@Serializable
sealed class StoreOrderSetTransactionIdResponse {
    @Serializable data object PurchaseNotFound : StoreOrderSetTransactionIdResponse()
    @Serializable data object Updated : StoreOrderSetTransactionIdResponse()
}

@Serializable
sealed class StoreOrderCreateResponse {
    @Serializable data object AccountNotFound : StoreOrderCreateResponse()
    @Serializable data object ProductNotFound : StoreOrderCreateResponse()
    @Serializable data class Created(val order: CreditPackageOrder) : StoreOrderCreateResponse()
    @Serializable data class AwaitingPayment(val order: CreditPackageOrder, val payUrl: String) : StoreOrderCreateResponse()
    @Serializable data class Failed(val reason: String): StoreOrderCreateResponse()
}

@Serializable
sealed class StoreCheckoutResponse {
    @Serializable data object AccountNotFound : StoreCheckoutResponse()
    @Serializable data class InsufficientFunds(val difference: Int) : StoreCheckoutResponse()
    @Serializable data class Success(
        val user: User,
        val purchased: List<CreditStoreCartItem>,
        val outOfStock: List<CreditStoreCartItem>,
        val missing: List<CreditStoreCartItem>,
        val insufficientFunds: List<CreditStoreCartItem>,
    ) : StoreCheckoutResponse()
    @Serializable data class Failed(val reason: String): StoreCheckoutResponse()
}
