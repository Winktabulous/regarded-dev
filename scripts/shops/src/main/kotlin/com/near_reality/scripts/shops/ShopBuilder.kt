package com.near_reality.scripts.shops

import com.near_reality.game.content.universalshop.UnivShopItem
import com.zenyte.game.model.shop.*
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList

/**
 * @author Jire
 */
class ShopBuilder(
    val name: String,
    val uid: Int = JsonShop.INVALID_SHOP_UID,
    val currency: ShopCurrency = ShopCurrency.COINS,
    val policy: ShopPolicy = ShopPolicy.STOCK_ONLY,
    val sellMultiplier: Double = Shop.DEFAULT_SELL_MULTIPLIER,
    val discount: ShopDiscount = ShopDiscount.NONE
) {

    private val items: ObjectList<JsonShop.Item> = ObjectArrayList()

    operator fun Int.invoke(
        amount: Int,
        sellPrice: Int,
        buyPrice: Int,
        restockTimer: Int = Shop.DEFAULT_RESTOCK_TIMER,
        ironmanRestricted: Boolean = false
    ) {
        val item = JsonShop.Item(this, amount, sellPrice, buyPrice, restockTimer, ironmanRestricted)
        items.add(item)
    }

    operator fun UnivShopItem.invoke(
        restockTimer: Int = Shop.DEFAULT_RESTOCK_TIMER,
    ) {
        val quantity = if(this.quantity < 100) 10000 else quantity
        val item = JsonShop.Item(this.id, quantity, this.sellPrice, this.buyPrice, restockTimer, this.ironmanRestricted)
        items.add(item)
    }

    @PublishedApi
    internal fun toJsonShop() = JsonShop(name, uid, currency, policy, sellMultiplier.toFloat(), discount, items)

}