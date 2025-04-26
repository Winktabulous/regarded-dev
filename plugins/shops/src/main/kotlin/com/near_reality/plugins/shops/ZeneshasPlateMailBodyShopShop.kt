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

class ZeneshasPlateMailBodyShopShop : ShopScript() {
    init {
        "Zenesha's Plate Mail Body Shop"(83, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PLATEBODY(3, 53, 160)
            IRON_PLATEBODY(1, 186, 560)
            STEEL_PLATEBODY(1, 666, 2000)
            BLACK_PLATEBODY(1, 1280, 3840)
            MITHRIL_PLATEBODY(1, 1733, 5200)
        }
    }
}