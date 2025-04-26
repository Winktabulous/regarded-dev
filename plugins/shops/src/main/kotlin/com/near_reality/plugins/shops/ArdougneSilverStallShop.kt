package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.SILVER_BAR
import com.zenyte.game.item.ItemId.SILVER_ORE
import com.zenyte.game.item.ItemId.UNSTRUNG_SYMBOL
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArdougneSilverStallShop : ShopScript() {
    init {
        "Ardougne Silver Stall"(81, ShopCurrency.COINS, STOCK_ONLY) {
            UNSTRUNG_SYMBOL(2, 66, 200)
            SILVER_ORE(1, 25, 75)
            SILVER_BAR(1, 50, 150)
        }
    }
}