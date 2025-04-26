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

class GracesGracefulClothingShop : ShopScript() {
    init {
        "Grace's Graceful Clothing"(2, ShopCurrency.MARK_OF_GRACE, STOCK_ONLY) {
            GRACEFUL_HOOD(100, 11, 14)
            GRACEFUL_CAPE(100, 12, 16)
            GRACEFUL_TOP(100, 17, 22)
            GRACEFUL_LEGS(100, 19, 24)
            GRACEFUL_GLOVES(100, 9, 12)
            GRACEFUL_BOOTS(100, 12, 16)
            AMYLASE_PACK(1000, 1, 4)
        }
    }
}