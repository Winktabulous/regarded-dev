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

class ZanarisGeneralStoreShop : ShopScript() {
    init {
        "Zanaris General Store"(252, ShopCurrency.COINS, CAN_SELL) {
            POT(5, 1, 1)
            JUG(2, 1, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, 1, 1)
            BUCKET(3, 1, 2)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 3, 13)
            TINDERBOX(2, 1, 1)
            CHISEL(2, 4, 1)
            HAMMER(5, 3, 1)
            NEWCOMER_MAP(5, 1, 1)
            SECURITY_BOOK(5, 1, 2)
        }
    }
}