package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class PerrysChopchopShopShop : ShopScript() {
    init {
        "Perry's Chop-chop Shop"(207, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_AXE(4, 9, 51)
            IRON_AXE(3, 33, 179)
            STEEL_AXE(2, 120, 640)
            MITHRIL_AXE(1, 312, 1664)
            ADAMANT_AXE(1, 768, 4096)
            RUNE_AXE(1, 7680, 40960)
            TINDERBOX(2, 0, 3)
        }
    }
}