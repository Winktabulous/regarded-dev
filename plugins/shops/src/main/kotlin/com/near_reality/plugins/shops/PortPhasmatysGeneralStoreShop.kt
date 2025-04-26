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

class PortPhasmatysGeneralStoreShop : ShopScript() {
    init {
        "Port Phasmatys General Store"(176, ShopCurrency.COINS, CAN_SELL) {
            POT(100, 1, 1)
            BUCKET(100, 1, 2)
            SHEARS(10, 1, 1)
            JUG(10, 1, 1)
            EMPTY_JUG_PACK(5, 56, 182)
            TINDERBOX(10, 1, 1)
            CHISEL(10, 4, 14)
            HAMMER(10, 3, 13)
        }
    }
}