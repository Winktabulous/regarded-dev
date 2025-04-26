package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.CHRONICLE
import com.zenyte.game.item.ItemId.TELEPORT_CARD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DiangosToyStoreShop : ShopScript() {
    init {
        "Diango's Toy Store"(149, ShopCurrency.COINS, STOCK_ONLY) {
            CHRONICLE(1, 0, 300)
            TELEPORT_CARD(100, 50, 150)
        }
    }
}