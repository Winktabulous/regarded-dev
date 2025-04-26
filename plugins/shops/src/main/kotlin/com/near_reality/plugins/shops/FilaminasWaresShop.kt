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

class FilaminasWaresShop : ShopScript() {
    init {
        "Filamina's Wares"(200, ShopCurrency.COINS, STOCK_ONLY) {
            STAFF(5, 9, 15)
            MAGIC_STAFF(5, 120, 200)
            STAFF_OF_AIR(2, 900, 1500)
            STAFF_OF_WATER(2, 900, 1500)
            STAFF_OF_EARTH(2, 900, 1500)
            STAFF_OF_FIRE(2, 900, 1500)
        }
    }
}