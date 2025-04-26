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

class EdmondsWildernessCapeShopShop : ShopScript() {
    init {
        "Edmond's Wilderness Cape Shop"(240, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM8_CAPE(100, 30, 50)
            TEAM18_CAPE(100, 30, 50)
            TEAM28_CAPE(100, 30, 50)
            TEAM38_CAPE(100, 30, 50)
            TEAM48_CAPE(100, 30, 50)
        }
    }
}