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

class KaramjaGeneralStoreShop : ShopScript() {
    init {
        "Karamja General Store"(104, ShopCurrency.COINS, CAN_SELL) {
            POT(30, 1, 1)
            JUG(10, 1, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(10, 1, 1)
            BUCKET(30, 1, 2)
            BOWL(10, 1, 4)
            CAKE_TIN(10, 3, 10)
            TINDERBOX(10, 1, 1)
            CHISEL(10, 4, 14)
            HAMMER(10, 4, 13)
        }
    }
}