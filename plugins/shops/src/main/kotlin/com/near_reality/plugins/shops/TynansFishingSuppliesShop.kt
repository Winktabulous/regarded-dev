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

class TynansFishingSuppliesShop : ShopScript() {
    init {
        "Tynan's Fishing Supplies"(216, ShopCurrency.COINS, STOCK_ONLY) {
            BUCKET(1000, 1, 2)
            SMALL_FISHING_NET(5, 3, 5)
            BIG_FISHING_NET(5, 14, 20)
            FISHING_ROD(5, 4, 5)
            HARPOON(2, 3, 5)
            LOBSTER_POT(2, 14, 20)
            FISHING_BAIT(1200, 2, 3)
            SANDWORMS(500, 76, 90)
            BAIT_PACK(80, 210, 300)
            SANDWORMS_PACK(50, 7650, 9000)
        }
    }
}