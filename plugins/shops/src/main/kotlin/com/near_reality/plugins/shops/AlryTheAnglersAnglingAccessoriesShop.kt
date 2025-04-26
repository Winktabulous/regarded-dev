package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ANGLER_BOOTS
import com.zenyte.game.item.ItemId.ANGLER_HAT
import com.zenyte.game.item.ItemId.ANGLER_TOP
import com.zenyte.game.item.ItemId.ANGLER_WADERS
import com.zenyte.game.item.ItemId.FISH_SACK
import com.zenyte.game.item.ItemId.PEARL_BARBARIAN_ROD
import com.zenyte.game.item.ItemId.PEARL_FISHING_ROD
import com.zenyte.game.item.ItemId.PEARL_FLY_FISHING_ROD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class AlryTheAnglersAnglingAccessoriesShop : ShopScript() {
    init {
        "Alry The Angler's Angling Accessories"(293, ShopCurrency.MOLCH_PEARL, STOCK_ONLY) {
            PEARL_FISHING_ROD(1000, 0, 100)
            PEARL_FLY_FISHING_ROD(1000, 0, 120)
            PEARL_BARBARIAN_ROD(1000, 0, 150)
            FISH_SACK(1000, 0, 1000)
            ANGLER_HAT(1000, 0, 100)
            ANGLER_TOP(1000, 0, 100)
            ANGLER_WADERS(1000, 0, 100)
            ANGLER_BOOTS(1000, 0, 100)
        }
    }
}