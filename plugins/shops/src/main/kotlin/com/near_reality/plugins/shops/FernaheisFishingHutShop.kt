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

class FernaheisFishingHutShop : ShopScript() {
    init {
        "Fernahei's Fishing Hut"(107, ShopCurrency.COINS, STOCK_ONLY) {
            FISHING_ROD(5, 3, 5)
            FLY_FISHING_ROD(5, 3, 5)
            FISHING_BAIT(200, 2, 3)
            BAIT_PACK(30, 210, 300)
            FEATHER(800, 1, 2)
            FEATHER_PACK(50, 142, 200)
            RAW_TROUT(0, 14, 20)
            RAW_PIKE(0, 15, 25)
            RAW_SALMON(0, 35, 50)
        }
    }
}