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

class MoonClanGeneralStoreShop : ShopScript() {
    init {
        "Moon Clan General Store"(42, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, 0, 1)
            JUG(2, 0, 1)
            SHEARS(2, 0, 1)
            BUCKET(3, 0, 2)
            BOWL(2, 2, 4)
            CAKE_TIN(2, 5, 10)
            TINDERBOX(3, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            SECURITY_BOOK(5, 0, 2)
        }
    }
}