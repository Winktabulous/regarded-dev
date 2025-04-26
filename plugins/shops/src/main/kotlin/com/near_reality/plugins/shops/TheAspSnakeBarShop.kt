package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class TheAspSnakeBarShop : ShopScript() {
    init {
        "The Asp & Snake Bar"(138, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(83, 1, 2)
            WHISKY(10, 1, 5)
            JUG_OF_WINE(13, 0, 1)
            VODKA(5, 1, 5)
            BRANDY(4, 1, 5)
            GROG(12, 1, 3)
        }
    }
}