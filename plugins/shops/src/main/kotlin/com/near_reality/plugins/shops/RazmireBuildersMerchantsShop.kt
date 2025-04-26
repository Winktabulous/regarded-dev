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

class RazmireBuildersMerchantsShop : ShopScript() {
    init {
        "Razmire Builders Merchants"(175, ShopCurrency.COINS, STOCK_ONLY) {
            LIMESTONE(1000, 9, 10)
            LIMESTONE_BRICK(1000, 19, 21)
            TIMBER_BEAM(1000, 0, 1)
            SWAMP_PASTE(1000, 28, 31)
            PLANK(10, 0, 1)
        }
    }
}