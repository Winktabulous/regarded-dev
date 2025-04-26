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

class GulluckandSonsShop : ShopScript() {
    init {
        "Gulluck and Sons"(91, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_ARROW(200, 0, 1)
            BRONZE_BOLTS(150, 0, 1)
            PEARL_BOLTS(1, 3, 13)
            SHORTBOW(4, 12, 50)
            LONGBOW(2, 20, 80)
            CROSSBOW(2, 17, 70)
            BRONZE_ARROWTIPS(200, 0, 1)
            IRON_ARROWTIPS(180, 0, 2)
            STEEL_ARROWTIPS(160, 1, 6)
            MITHRIL_ARROWTIPS(140, 4, 16)
            IRON_AXE(5, 14, 56)
            STEEL_AXE(3, 50, 200)
            IRON_BATTLEAXE(5, 45, 182)
            STEEL_BATTLEAXE(2, 162, 650)
            MITHRIL_BATTLEAXE(1, 422, 1690)
            BRONZE_2H_SWORD(4, 20, 80)
            IRON_2H_SWORD(3, 70, 280)
            STEEL_2H_SWORD(2, 250, 1000)
            BLACK_2H_SWORD(1, 480, 1920)
            MITHRIL_2H_SWORD(1, 650, 2600)
            ADAMANT_2H_SWORD(1, 1600, 6400)
        }
    }
}