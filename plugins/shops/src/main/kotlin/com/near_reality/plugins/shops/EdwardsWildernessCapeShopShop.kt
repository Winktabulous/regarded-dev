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

class EdwardsWildernessCapeShopShop : ShopScript() {
    init {
        "Edward's Wilderness Cape Shop"(241, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM5_CAPE(100, 30, 50)
            TEAM15_CAPE(100, 30, 50)
            TEAM25_CAPE(100, 30, 50)
            TEAM35_CAPE(100, 30, 50)
            TEAM45_CAPE(100, 30, 50)
        }
    }
}