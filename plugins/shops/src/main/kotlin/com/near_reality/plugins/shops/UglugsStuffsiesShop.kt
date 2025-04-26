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

class UglugsStuffsiesShop : ShopScript() {
    init {
        "Uglug's Stuffsies"(28, ShopCurrency.COINS, STOCK_ONLY) {
            RELICYMS_BALM3(100, 60, 200)
            SANFEW_SERUM3(0, 72, 240)
            ACHEY_TREE_LOGS(100, 2, 4)
            BOW_STRING(10, 6, 10)
            RAW_CHOMPY(0, 25, 85)
            COOKED_CHOMPY(10, 39, 130)
            OGRE_COFFIN_KEY(0, 30, 100)
            KNIFE(5, 3, 6)
            BRONZE_BRUTAL(0, 1, 5)
            IRON_BRUTAL(0, 3, 10)
            STEEL_BRUTAL(0, 6, 20)
            BLACK_BRUTAL(0, 10, 35)
            MITHRIL_BRUTAL(0, 15, 50)
            ADAMANT_BRUTAL(0, 28, 95)
            RUNE_BRUTAL(0, 135, 450)
            COMP_OGRE_BOW(0, 54, 180)
        }
    }
}