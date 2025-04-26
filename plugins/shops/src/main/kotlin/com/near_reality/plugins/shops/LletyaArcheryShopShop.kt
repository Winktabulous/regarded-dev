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

class LletyaArcheryShopShop : ShopScript() {
    init {
        "Lletya Archery Shop"(180, ShopCurrency.COINS, STOCK_ONLY) {
            IRON_ARROW(2000, 1, 3)
            STEEL_ARROW(500, 4, 15)
            MITHRIL_ARROW(500, 12, 41)
            ADAMANT_ARROW(450, 32, 104)
            RUNE_ARROW(400, 160, 520)
            BRONZE_BOLTS(1500, 0, 1)
            OAK_SHORTBOW(5, 40, 130)
            OAK_LONGBOW(5, 64, 208)
            CROSSBOW(5, 28, 91)
            WILLOW_SHORTBOW(5, 80, 260)
            WILLOW_LONGBOW(5, 128, 416)
        }
    }
}