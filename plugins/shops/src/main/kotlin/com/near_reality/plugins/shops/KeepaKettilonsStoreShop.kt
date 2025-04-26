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

class KeepaKettilonsStoreShop : ShopScript() {
    init {
        "Keepa Kettilon's Store"(38, ShopCurrency.COINS, STOCK_ONLY) {
            TUNA(20, 70, 110)
            SALMON(20, 35, 55)
            COD(20, 17, 27)
            LOBSTER(10, 105, 165)
            SWORDFISH(0, 140, 220)
            SHARK(0, 210, 330)
        }
    }
}