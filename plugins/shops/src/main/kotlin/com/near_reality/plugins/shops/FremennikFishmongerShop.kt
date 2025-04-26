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

class FremennikFishmongerShop : ShopScript() {
    init {
        "Fremennik Fishmonger"(51, ShopCurrency.COINS, STOCK_ONLY) {
            SMALL_FISHING_NET(5, 3, 6)
            FISHING_ROD(5, 3, 6)
            FLY_FISHING_ROD(5, 3, 6)
            HARPOON(2, 3, 6)
            LOBSTER_POT(2, 14, 26)
            FISHING_BAIT(1500, 2, 3)
            BAIT_PACK(80, 210, 390)
            FEATHER(1000, 1, 2)
            FEATHER_PACK(50, 1, 260)
            BIG_FISHING_NET(5, 14, 26)
            RAW_SHRIMPS(0, 3, 6)
            RAW_SARDINE(200, 7, 13)
            RAW_HERRING(0, 10, 19)
            RAW_MACKEREL(0, 12, 22)
            RAW_COD(0, 17, 32)
            RAW_ANCHOVIES(0, 10, 19)
            RAW_TROUT(0, 14, 26)
            RAW_PIKE(0, 17, 32)
            RAW_SALMON(0, 35, 65)
            RAW_TUNA(0, 70, 130)
            RAW_LOBSTER(0, 105, 195)
            RAW_BASS(0, 84, 156)
            RAW_SWORDFISH(0, 140, 260)
            RAW_SHARK(0, 210, 390)
        }
    }
}