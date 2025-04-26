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

class GreengrocerofMiscellaniaShop : ShopScript() {
    init {
        "Greengrocer of Miscellania"(46, ShopCurrency.COINS, STOCK_ONLY) {
            CABBAGE(10, 0, 1)
            POTATO(10, 0, 1)
            ONION(10, 2, 3)
            TOMATO(10, 3, 4)
            GARLIC(2, 2, 3)
        }
    }
}