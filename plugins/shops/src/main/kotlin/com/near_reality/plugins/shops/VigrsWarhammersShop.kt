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

class VigrsWarhammersShop : ShopScript() {
    init {
        "Vigr's Warhammers"(198, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_WARHAMMER(5, 25, 61)
            IRON_WARHAMMER(4, 94, 224)
            STEEL_WARHAMMER(3, 352, 832)
            BLACK_WARHAMMER(3, 539, 1274)
            MITHRIL_WARHAMMER(2, 913, 2158)
            ADAMANT_WARHAMMER(1, 2266, 5356)
        }
    }
}