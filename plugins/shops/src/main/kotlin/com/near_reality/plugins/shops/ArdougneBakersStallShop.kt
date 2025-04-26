package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BREAD
import com.zenyte.game.item.ItemId.CAKE
import com.zenyte.game.item.ItemId.CHOCOLATE_BAR
import com.zenyte.game.item.ItemId.CHOCOLATE_SLICE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArdougneBakersStallShop : ShopScript() {
    init {
        "Ardougne Baker's Stall"(78, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(10, 9, 12)
            CAKE(3, 40, 50)
            CHOCOLATE_SLICE(8, 24, 30)
            CHOCOLATE_BAR(7, 8, 10)
        }
    }
}