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

class DusurisStarShopShop : ShopScript() {
    init {
        "Dusuri's Star Store"(1005, ShopCurrency.STARDUST, STOCK_ONLY) {
            CELESTIAL_RING_UNCHARGED(50, 1600, 2000)
            STAR_FRAGMENT(50, 2400, 3000)
            32082(50, 1600, 2000)
            BAG_FULL_OF_GEMS(100, 240, 300)
            SOFT_CLAY_PACK(1000, 120, 150)
            TOME_OF_EXPERIENCE_30215(500, 0, 300)
            452(5000, 0, 50) // noted runite ore
            450(5000, 0, 35) // noted adamantite ore
        }
    }
}