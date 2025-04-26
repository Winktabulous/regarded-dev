@file:Suppress("unused")

package com.near_reality.api.service.store

import com.near_reality.api.APIClient
import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.model.CreditStoreCartItem
import com.near_reality.api.model.User
import com.near_reality.api.requests.CreditPackageOrderCreateRequest
import com.near_reality.api.requests.StoreCheckoutRequest
import com.near_reality.api.resources.Store
import com.near_reality.api.responses.StoreCheckoutResponse
import com.near_reality.api.responses.StoreOrderCreateResponse
import com.near_reality.api.service.APIService
import com.near_reality.game.model.ui.credit_store.CreditPackage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

/**
 * Handles the Store API service.
 *
 * @author Stan van der Bend
 */
internal object StoreAPIService : APIService() {

    private var orderUpdateChannels = ConcurrentHashMap<Int, Channel<StoreOrderUpdate>>()
    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun submitOrder(
        user: User,
        creditPackage: CreditPackage,
        paymentMethod: CreditPackageOrder.PaymentMethod,
    ) : Channel<StoreOrderUpdate> {
        val orderUpdateChannel = Channel<StoreOrderUpdate>()
        APIClient.post<CreditPackageOrderCreateRequest, StoreOrderCreateResponse, Store.Order.Create>(
            Store.Order.Create(),
            CreditPackageOrderCreateRequest(
                userId = user.id,
                paymentMethod = paymentMethod,
                creditPackageId = creditPackage.id,
                creditPackageAmount = 1,
            ),
            async = true,
            onSuccess = {
                ioScope.launch {
                    orderUpdateChannel.send(asUpdate())
                }
                if (this is StoreOrderCreateResponse.AwaitingPayment)
                    orderUpdateChannels[order.id] = orderUpdateChannel
            },
            onFailed = {
                ioScope.launch {
                    orderUpdateChannel.send(StoreOrderUpdate.Failed("Failed! Please try again later."))
                }
            }
        )
        return orderUpdateChannel
    }

    fun Routing.orderCallback() {
        post<Store.Order.Callback> {
            try {
                val order = call.receive<CreditPackageOrder>()
                logger.info("Received store order callback: $order")
                val updateChannel = orderUpdateChannels[order.id]
                ioScope.launch {
                    updateChannel?.send(order.asUpdate())
                    if (order.status == CreditPackageOrder.Status.PAID) {
                        logger.info("Claiming store order: $order")
                        StorePlayerHandler.tryClaim(order)
                        orderUpdateChannels.remove(order.id)
                        updateChannel?.close()
                    }
                }
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                logger.error("Failed to handle store order callback", e)
            }
        }
    }

    fun checkoutCart(user: User, cart: List<CreditStoreCartItem>, onResponse: (StoreCheckoutResponse?) -> Unit) {
        if (!APIClient.post<StoreCheckoutRequest, StoreCheckoutResponse, Store.Checkout>(
                resource = Store.Checkout(),
                request = StoreCheckoutRequest(user.id, cart),
                onSuccess = { onResponse(this) },
                onFailed = {
                    logger.error("Failed to checkout store cart: $cart (api_status_received=$this)", it)
                    onResponse(null)
                }
            )
        ) onResponse(null)
    }
}

