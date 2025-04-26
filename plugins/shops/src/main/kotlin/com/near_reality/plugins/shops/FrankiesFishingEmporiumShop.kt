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

class FrankiesFishingEmporiumShop : ShopScript() {
    init {
        "Frankie's Fishing Emporium"(213, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_SHRIMPS(50, -1, 7)
            RAW_SARDINE(50, -1, 15)
            RAW_HERRING(25, -1, 22)
            RAW_MACKEREL(25, -1, 25)
            RAW_COD(0, -1, 50)
            RAW_ANCHOVIES(0, -1, 30)
            RAW_TUNA(0, -1, 220)
            RAW_LOBSTER(0, -1, 300)
            RAW_BASS(0, -1, 240)
            RAW_SWORDFISH(0, -1, 400)
            RAW_SHARK(50, -1, 600)
        }
    }
}