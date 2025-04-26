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

class VoidKnightArcheryStoreShop : ShopScript() {
    init {
        "Void Knight Archery Store"(235, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_JAVELIN(10, 2, 4)
            IRON_JAVELIN(10, 3, 6)
            STEEL_JAVELIN(10, 14, 24)
            MITHRIL_JAVELIN(5, 38, 64)
            ADAMANT_JAVELIN(5, 96, 160)
            RUNE_JAVELIN(5, 240, 400)
            BRONZE_ARROWTIPS(10, 0, 1)
            IRON_ARROWTIPS(10, 1, 2)
            STEEL_ARROWTIPS(10, 3, 6)
            MITHRIL_ARROWTIPS(5, 9, 16)
            ADAMANT_ARROWTIPS(5, 24, 40)
            RUNE_ARROWTIPS(5, 120, 200)
        }
    }
}