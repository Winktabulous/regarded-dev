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

class HendorsAwesomeOresShop : ShopScript() {
    init {
        "Hendor's Awesome Ores"(63, ShopCurrency.COINS, STOCK_ONLY) {
            COPPER_ORE(0, 2, 3)
            TIN_ORE(0, 2, 3)
            IRON_ORE(0, 11, 17)
            MITHRIL_ORE(0, 113, 162)
            ADAMANTITE_ORE(0, 280, 400)
            RUNITE_ORE(0, 2240, 3200)
            COAL(0, 31, 45)
        }
    }
}