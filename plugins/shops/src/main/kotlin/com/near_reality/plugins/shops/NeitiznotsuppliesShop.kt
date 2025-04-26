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

class NeitiznotsuppliesShop : ShopScript() {
    init {
        "Neitiznot supplies"(50, ShopCurrency.COINS, STOCK_ONLY) {
            KNIFE(10, 1, 6)
            HAMMER(10, 0, 1)
            THREAD(10, 0, 1)
            NEEDLE(10, 0, 1)
            BRONZE_AXE(10, 4, 16)
            BALL_OF_WOOL(100, 0, 2)
        }
    }
}