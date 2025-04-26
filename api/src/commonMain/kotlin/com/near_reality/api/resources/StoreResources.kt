@file:Suppress("unused")

package com.near_reality.api.resources

import io.ktor.resources.*
import kotlinx.serialization.SerialName

@Resource("/store")
class Store {

    @Resource("checkout")
    class Checkout(val parent: Store = Store())

    @Resource("order")
    class Order(val parent: Store = Store()) {

        /**
         * Server sided callback route.
         */
        @Resource("callback")
        class Callback(val parent: Order = Order())

        @Resource("update")
        class Update(val parent: Order = Order())

        @Resource("info")
        class Info(val parent: Order = Order(), val accountName: String)

        @Resource("create")
        class Create(val parent: Order = Order())
    }

    @Resource("paypal")
    class Paypal(val parent: Store = Store()) {

        @Resource("capture")
        class Capture(val parent: Paypal = Paypal(), @SerialName("order-id") val orderId: Int, val uid: String)

        @Resource("cancel")
        class Cancel(val parent: Paypal = Paypal(), @SerialName("order-id") val orderId: Int, val uid: String)
    }

    @Resource("coinbase")
    class Coinbase(val parent: Store = Store()) {

        @Resource("capture")
        class Capture(val parent: Coinbase = Coinbase(), @SerialName("order-id") val orderId: Int, val uid: String)

        @Resource("cancel")
        class Cancel(val parent: Coinbase = Coinbase(), @SerialName("order-id") val orderId: Int, val uid: String)

        @Resource("webhook")
        class Webhook(val parent: Coinbase = Coinbase())
    }
}

