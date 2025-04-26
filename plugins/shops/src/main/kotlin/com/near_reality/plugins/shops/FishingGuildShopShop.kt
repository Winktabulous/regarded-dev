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

class FishingGuildShopShop : ShopScript() {
    init {
        "Fishing Guild Shop"(58, ShopCurrency.COINS, STOCK_ONLY) {
            FISHING_BAIT(2000, -1, 3)
            BAIT_PACK(150, -1, 300)
            FEATHER(1500, -1, 2)
            FEATHER_PACK(150, -1, 200)
            RAW_COD(0, -1, 25)
            RAW_MACKEREL(0, -1, 17)
            RAW_BASS(0, -1, 120)
            RAW_TUNA(0, -1, 100)
            RAW_LOBSTER(0, -1, 150)
            RAW_SWORDFISH(0, -1, 200)
            COD(0, -1, 25)
            MACKEREL(0, -1, 17)
            BASS(0, -1, 120)
            TUNA(0, -1, 100)
            LOBSTER(0, -1, 150)
            SWORDFISH(0, -1, 200)
        }
    }
}