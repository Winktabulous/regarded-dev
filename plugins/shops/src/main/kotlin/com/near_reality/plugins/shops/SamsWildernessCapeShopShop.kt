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

class SamsWildernessCapeShopShop : ShopScript() {
    init {
        "Sam's Wilderness Cape Shop"(248, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM10_CAPE(100, 30, 50)
            TEAM20_CAPE(100, 30, 50)
            TEAM30_CAPE(100, 30, 50)
            TEAM40_CAPE(100, 30, 50)
            TEAM50_CAPE(100, 30, 50)
        }
    }
}