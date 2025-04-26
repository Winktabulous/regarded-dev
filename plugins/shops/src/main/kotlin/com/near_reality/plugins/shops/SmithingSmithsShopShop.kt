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

class SmithingSmithsShopShop : ShopScript() {
    init {
        "Smithing Smith's Shop"(230, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SCIMITAR(5, 10, 32)
            IRON_SCIMITAR(3, 37, 112)
            STEEL_SCIMITAR(2, 133, 400)
            MITHRIL_SCIMITAR(1, 346, 1040)
            HAMMER(5, 0, 1)
        }
    }
}