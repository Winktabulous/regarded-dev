package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class KaramjaWinesSpiritsandBeersShop : ShopScript() {
    init {
        "Karamja Wines, Spirits, and Beers"(105, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(3, 1, 2)
            KARAMJAN_RUM(3, 0, 30)
            JUG_OF_WINE(1, 0, 1)
        }
    }
}