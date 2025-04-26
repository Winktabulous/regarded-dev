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

class TheShrimpandParrotShop : ShopScript() {
    init {
        "The Shrimp and Parrot"(103, ShopCurrency.COINS, STOCK_ONLY) {
            HERRING(5, 11, 19)
            COD(5, 18, 32)
            TUNA(5, 75, 130)
            LOBSTER(3, 112, 195)
            SWORDFISH(2, 150, 260)
            COOKED_KARAMBWAN(3, 187, 325)
        }
    }
}