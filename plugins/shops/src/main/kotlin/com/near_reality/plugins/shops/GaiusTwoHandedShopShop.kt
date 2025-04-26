package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class GaiusTwoHandedShopShop : ShopScript() {
    init {
        "Gaius' Two-Handed Shop"(24, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_2H_SWORD(4, 48, 80)
            IRON_2H_SWORD(3, 168, 280)
            STEEL_2H_SWORD(2, 600, 1000)
            BLACK_2H_SWORD(1, 1152, 1920)
            MITHRIL_2H_SWORD(1, 1560, 2600)
            ADAMANT_2H_SWORD(1, 3840, 6400)
        }
    }
}