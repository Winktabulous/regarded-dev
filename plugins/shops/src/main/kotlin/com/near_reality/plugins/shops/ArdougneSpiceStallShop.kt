package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.GARLIC
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.SPICE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArdougneSpiceStallShop : ShopScript() {
    init {
        "Ardougne Spice Stall"(82, ShopCurrency.COINS, STOCK_ONLY) {
            SPICE(1, 138, 230)
            KNIFE(1, -1, 6)
            GARLIC(1000, 1, 3, 1)
        }
    }
}