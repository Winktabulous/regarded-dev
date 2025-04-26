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

class FlosisFishmongersShop : ShopScript() {
    init {
        "Flosi's Fishmongers"(37, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_LOBSTER(5, 105, 165)
            RAW_TUNA(20, 70, 110)
            RAW_SALMON(20, 35, 55)
            RAW_COD(20, 17, 27)
            RAW_SHARK(0, 210, 330)
        }
    }
}