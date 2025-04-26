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

class ShantayPassShopShop : ShopScript() {
    init {
        "Shantay Pass Shop"(127, ShopCurrency.COINS, CAN_SELL) {
            WATERSKIN4(100, 21, 30)
            WATERSKIN0(100, 10, 15)
            JUG_OF_WATER(10, 0, 1)
            BOWL_OF_WATER(10, 2, 4)
            BUCKET_OF_WATER(10, 4, 6)
            KNIFE(10, 4, 6)
            DESERT_SHIRT(10, 28, 40)
            DESERT_ROBE(10, 28, 40)
            DESERT_BOOTS(10, 14, 20)
            BRONZE_BAR(10, 5, 8)
            FEATHER(500, 1, 2)
            FEATHER_PACK(50, 140, 200)
            HAMMER(10, 0, 1)
            BUCKET(0, 1, 2)
            BOWL(0, 2, 4)
            JUG(0, 0, 1)
            EMPTY_JUG_PACK(0, 98, 140)
            SHANTAY_PASS(500, 3, 5)
            ROPE(20, 12, 18)
        }
    }
}