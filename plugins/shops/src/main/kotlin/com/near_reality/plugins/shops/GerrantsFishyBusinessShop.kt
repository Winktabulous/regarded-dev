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

class GerrantsFishyBusinessShop : ShopScript() {
    init {
        "Gerrant's Fishy Business"(17, ShopCurrency.COINS, STOCK_ONLY) {
            SMALL_FISHING_NET(5, 3, 5)
            FISHING_ROD(5, 3, 5)
            FLY_FISHING_ROD(5, 3, 5)
            HARPOON(2, 3, 5)
            LOBSTER_POT(2, 14, 20)
            FISHING_BAIT(1500, 2, 3)
            BAIT_PACK(80, 210, 300)
            FEATHER(1000, 1, 2)
            FEATHER_PACK(100, 140, 200)
            RAW_SHRIMPS(0, -1, 5)
            RAW_SARDINE(200, 7, 10)
            RAW_HERRING(0, 10, 15)
            RAW_ANCHOVIES(0, -1, 10)
            RAW_TROUT(0, -1, 20)
            RAW_PIKE(0, -1, 25)
            RAW_SALMON(0, -1, 50)
            RAW_TUNA(0, -1, 100)
            RAW_LOBSTER(0, 105, 150)
            RAW_SWORDFISH(0, 140, 200)
        }
    }
}