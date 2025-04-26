package com.near_reality.game.model.ui.credit_store

import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.service.store.StorePlayerHandler
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.typeVarbit

private var Player.creditPackagePaymentMethodSelected: CreditPackageOrder.PaymentMethod by typeVarbit(
    id = 17008,
    get= { CreditPackageOrder.PaymentMethod.entries[it] },
    set= { it.ordinal }
)

private var Player.creditPackageSelected: CreditPackage? by attribute("credit_package_selected")

@Suppress("unused")
class CreditPackageInterface : Interface() {


    override fun attach() {
        CreditPackage.values.forEach {
            put(it.component, it::class.simpleName)
        }
        put(3, "go_back")
        put(21, "checkout")
        put(19, "payment_options")
    }

    override fun build() {
        CreditPackage.values.forEach {
            bind(it::class.simpleName) { player ->
                player.sendDeveloperMessage(it::class.simpleName)
                player.creditPackageSelected = it
            }
        }
        bind("go_back") { player ->
            GameInterface.CREDIT_STORE.open(player)
        }
        bind("checkout") { player ->
            val creditPackage = player.creditPackageSelected
            if (creditPackage == null)
                player.sendMessage("You must select a package to purchase.")
            else
                StorePlayerHandler.startNewOrder(player, creditPackage, player.creditPackagePaymentMethodSelected)
        }
        bind("payment_options") { player, slotId, _, option ->
            player.creditPackagePaymentMethodSelected = CreditPackageOrder.PaymentMethod.fromComponent(slotId)
        }
    }

    override fun getInterface(): GameInterface =
        GameInterface.CREDIT_PACKAGES

    override fun open(player: Player) {
        super.open(player)
        with(player.packetDispatcher){
            // Go back button
            sendComponentSettings(id, id component 3, 0, 9, AccessMask.CLICK_OP1)
            // Checkout button
            sendComponentSettings(id, id component 21, 0, 9, AccessMask.CLICK_OP1)
            // payment options
            sendComponentSettings(id, id component 19, 0, 4, AccessMask.CLICK_OP1)
        }
    }
}



sealed class CreditPackage(val id: Int, val component: Int, val amount: Int, val bonusCredits: Int, val cost: Float) {
    data object FiveHundred : CreditPackage(0, 7, 500, 0, 4.99F)
    data object OneThousand : CreditPackage(1, 8, 1000, 5, 9.99F)
    data object TwoThousand : CreditPackage(2, 9, 2000, 10, 19.99F)
    data object FiveThousand : CreditPackage(3, 10, 5000, 20, 49.99F)
    data object TenThousand : CreditPackage(4, 11, 10000, 50, 99.99F)
    data object TwentyFiveThousand : CreditPackage(5, 12, 25000, 100, 249.99F);
    companion object {
        val values by lazy {
            arrayOf(
                FiveHundred,
                OneThousand,
                TwoThousand,
                FiveThousand,
                TenThousand,
                TwentyFiveThousand
            )
        }
    }
}



val CreditPackageOrder.PaymentMethod.bitValue: Int
    get() = ordinal
val CreditPackageOrder.PaymentMethod.componentRange: IntRange
    get() = when (this) {
        CreditPackageOrder.PaymentMethod.PAYPAL -> 0..1
        CreditPackageOrder.PaymentMethod.COINBASE -> 2..3
    }
fun CreditPackageOrder.PaymentMethod.Companion.fromComponent(component: Int): CreditPackageOrder.PaymentMethod =
    CreditPackageOrder.PaymentMethod.entries.first { it.componentRange.contains(component) }
