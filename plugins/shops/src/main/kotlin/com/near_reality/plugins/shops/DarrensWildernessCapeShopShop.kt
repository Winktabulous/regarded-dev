package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.TEAM14_CAPE
import com.zenyte.game.item.ItemId.TEAM24_CAPE
import com.zenyte.game.item.ItemId.TEAM34_CAPE
import com.zenyte.game.item.ItemId.TEAM44_CAPE
import com.zenyte.game.item.ItemId.TEAM4_CAPE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DarrensWildernessCapeShopShop : ShopScript() {
    init {
        "Darren's Wilderness Cape Shop"(239, ShopCurrency.COINS, STOCK_ONLY) {
            TEAM4_CAPE(100, 30, 50)
            TEAM14_CAPE(100, 30, 50)
            TEAM24_CAPE(100, 30, 50)
            TEAM34_CAPE(100, 30, 50)
            TEAM44_CAPE(100, 30, 50)
        }
    }
}