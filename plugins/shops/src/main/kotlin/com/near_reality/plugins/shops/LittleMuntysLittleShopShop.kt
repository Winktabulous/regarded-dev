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

class LittleMuntysLittleShopShop : ShopScript() {
    init {
        "Little Munty's Little Shop"(210, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(20, 0, 1)
            POT(50000, 0, 1)
            JUG(5, 0, 1)
            EMPTY_JUG_PACK(4, 56, 154)
            BUCKET(5, 0, 1)
            BOWL(5, 1, 4)
            TINDERBOX(2, 0, 1)
            BALL_OF_WOOL(30, 0, 2)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
        }
    }
}