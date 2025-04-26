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

class OreSellerShop : ShopScript() {
    init {
        "Ore Seller"(189, ShopCurrency.COINS, STOCK_ONLY) {
            COPPER_ORE(1000, 1, 4, 5)
            TIN_ORE(1000, 1, 4, 5)
            IRON_ORE(1000, 6, 25, 5)
            MITHRIL_ORE(1000, 64, 243, 5)
            SILVER_ORE(1000, 30, 112, 5)
            GOLD_ORE(1000, 60, 225, 5)
            COAL(1000, 18, 67, 5)
        }
    }
}