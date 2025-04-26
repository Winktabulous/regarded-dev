package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.FEATHER
import com.zenyte.game.item.ItemId.FEATHER_PACK
import com.zenyte.game.item.ItemId.IRON_ARROW
import com.zenyte.game.item.ItemId.IRON_ARROWTIPS
import com.zenyte.game.item.ItemId.STEEL_ARROW
import com.zenyte.game.item.ItemId.STEEL_ARROWTIPS
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class AvasOddsandEndsShop : ShopScript() {
    init {
        "Ava's Odds and Ends"(148, ShopCurrency.COINS, STOCK_ONLY) {
            FEATHER(1000, 0, 2)
            FEATHER_PACK(100, 80, 260)
            IRON_ARROW(40, 1, 3)
            STEEL_ARROW(10, 5, 16)
            IRON_ARROWTIPS(30, 0, 2)
            STEEL_ARROWTIPS(20, 2, 7)
        }
    }
}