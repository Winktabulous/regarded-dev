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

class RichardsWildernessCapeShopShop : ShopScript() {
    init {
        "Richard's Wilderness Cape Shop"(247, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM6_CAPE(100, 30, 50)
            TEAM16_CAPE(100, 30, 50)
            TEAM26_CAPE(100, 30, 50)
            TEAM36_CAPE(100, 30, 50)
            TEAM46_CAPE(100, 30, 50)
        }
    }
}