package com.near_reality.api.service.store

import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.responses.StoreOrderCreateResponse

internal fun StoreOrderCreateResponse.asUpdate() = when(this) {
    is StoreOrderCreateResponse.Created -> StoreOrderUpdate.Submitted
    is StoreOrderCreateResponse.AwaitingPayment -> StoreOrderUpdate.AwaitingPayment(payUrl)
    is StoreOrderCreateResponse.Failed -> StoreOrderUpdate.Failed(reason)
    StoreOrderCreateResponse.AccountNotFound -> StoreOrderUpdate.Failed("Account not found")
    StoreOrderCreateResponse.ProductNotFound -> StoreOrderUpdate.Failed("Product not found")
}

internal fun CreditPackageOrder.asUpdate(payUrl: String? = null) = when(status) {
    CreditPackageOrder.Status.CREATED -> StoreOrderUpdate.Submitted
    CreditPackageOrder.Status.PENDING -> StoreOrderUpdate.AwaitingPayment(payUrl!!)
    CreditPackageOrder.Status.CANCELED -> StoreOrderUpdate.Canceled
    CreditPackageOrder.Status.PAID -> StoreOrderUpdate.Claimed(this)
    CreditPackageOrder.Status.DISPUTED -> StoreOrderUpdate.Failed("Failed! Order was disputed...")
}

sealed class StoreOrderUpdate {
    data object Submitted : StoreOrderUpdate()
    data class AwaitingPayment(val url: String) : StoreOrderUpdate()
    data object Canceled : StoreOrderUpdate()
    data class Claimed(val order: CreditPackageOrder) : StoreOrderUpdate()
    data class Failed(val reason: String) : StoreOrderUpdate()
}

internal val CreditPackageOrder.totalCredits: Int
    get() = (creditPackage.credits + creditPackage.bonusCredits) * amount
