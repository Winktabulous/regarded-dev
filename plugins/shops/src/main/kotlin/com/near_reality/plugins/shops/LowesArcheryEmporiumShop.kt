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

class LowesArcheryEmporiumShop : ShopScript() {
    init {
        "Lowe's Archery Emporium"(164, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_ARROW(2000, 0, 2)
            IRON_ARROW(1500, 1, 3)
            STEEL_ARROW(1000, 6, 12)
            MITHRIL_ARROW(800, 17, 32)
            ADAMANT_ARROW(600, 44, 80)
            BRONZE_BOLTS(1500, 0, 1)
            SHORTBOW(4, 27, 50)
            LONGBOW(4, 44, 80)
            OAK_SHORTBOW(3, 55, 100)
            OAK_LONGBOW(3, 88, 160)
            WILLOW_SHORTBOW(2, 110, 200)
            WILLOW_LONGBOW(2, 176, 320)
            MAPLE_SHORTBOW(1, 220, 400)
            MAPLE_LONGBOW(1, 352, 640)
            CROSSBOW(2, 38, 70)
        }
    }
}