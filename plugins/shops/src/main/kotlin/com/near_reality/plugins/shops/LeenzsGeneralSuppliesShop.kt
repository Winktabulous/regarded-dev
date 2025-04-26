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

class LeenzsGeneralSuppliesShop : ShopScript() {
    init {
        "Leenz's General Supplies"(215, ShopCurrency.COINS, STOCK_ONLY) {
            POT(50000, 0, 1)
            JUG(5, 0, 1)
            EMPTY_JUG_PACK(6, -1, 154)
            BUCKET(5, 0, 2)
            BOWL(5, 0, 4)
            TINDERBOX(2, 1, 1)
            HAMMER(5, 0, 1)
            BRONZE_NAILS(500, 0, 2)
            IRON_NAILS(500, 1, 4)
        }
    }
}