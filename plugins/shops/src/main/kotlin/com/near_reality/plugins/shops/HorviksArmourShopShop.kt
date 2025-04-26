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

class HorviksArmourShopShop : ShopScript() {
    init {
        "Horvik's Armour Shop"(163, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_CHAINBODY(5, 36, 60)
            IRON_CHAINBODY(3, 126, 210)
            STEEL_CHAINBODY(3, 450, 750)
            MITHRIL_CHAINBODY(1, 1170, 1950)
            BRONZE_PLATEBODY(3, 96, 160)
            IRON_PLATEBODY(1, 336, 560)
            STEEL_PLATEBODY(1, 1200, 2000)
            BLACK_PLATEBODY(1, 2304, 3840)
            MITHRIL_PLATEBODY(1, 3120, 5200)
            IRON_PLATELEGS(1, 168, 280)
            STUDDED_BODY(1, 510, 850)
            STUDDED_CHAPS(1, 450, 750)
        }
    }
}