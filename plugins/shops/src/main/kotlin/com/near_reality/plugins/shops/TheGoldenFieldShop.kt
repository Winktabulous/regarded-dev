package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class TheGoldenFieldShop : ShopScript() {
    init {
        "The Golden Field"(203, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(10, -1, 2)
            CIDER(10, -1, 2)
            JUG_OF_WINE(5, -1, 1)
            CUP_OF_TEA(5, -1, 10)
        }
    }
}