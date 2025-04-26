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

class SimonsWildernessCapeShopShop : ShopScript() {
    init {
        "Simon's Wilderness Cape Shop"(249, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM9_CAPE(100, 30, 50)
            TEAM19_CAPE(100, 30, 50)
            TEAM29_CAPE(100, 30, 50)
            TEAM39_CAPE(100, 30, 50)
            TEAM49_CAPE(100, 30, 50)
        }
    }
}