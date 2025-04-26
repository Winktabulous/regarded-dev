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

class HarrysFishingShopShop : ShopScript() {
    init {
        "Harry's Fishing Shop"(73, ShopCurrency.COINS, STOCK_ONLY) {
            SMALL_FISHING_NET(5, 3, 5)
            FISHING_ROD(5, 3, 5)
            HARPOON(2, 3, 5)
            LOBSTER_POT(2, 14, 20)
            FISHING_BAIT(1200, 2, 3)
            BAIT_PACK(80, 210, 300)
            BIG_FISHING_NET(5, 14, 20)
            RAW_SHRIMPS(0, 3, 5)
            RAW_SARDINE(0, 7, 10)
            RAW_HERRING(0, 10, 15)
            RAW_MACKEREL(0, 11, 17)
            RAW_COD(0, 17, 25)
            RAW_ANCHOVIES(0, 10, 15)
            RAW_TUNA(0, 70, 100)
            RAW_LOBSTER(0, 105, 150)
            RAW_BASS(0, 84, 120)
            RAW_SWORDFISH(0, 140, 200)
            RAW_SHARK(0, 210, 300)
        }
    }
}