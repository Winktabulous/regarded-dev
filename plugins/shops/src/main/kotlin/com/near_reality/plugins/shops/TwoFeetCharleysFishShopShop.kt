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

class TwoFeetCharleysFishShopShop : ShopScript() {
    init {
        "Two Feet Charley's Fish Shop"(231, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_SHRIMPS(10, 3, 5)
            RAW_SARDINE(10, 6, 10)
            RAW_HERRING(7, 9, 15)
            RAW_MACKEREL(7, 10, 17)
            RAW_COD(6, 15, 25)
            RAW_ANCHOVIES(5, 9, 15)
            RAW_TUNA(4, 60, 100)
            RAW_LOBSTER(2, 90, 150)
            RAW_BASS(2, 72, 120)
            RAW_SWORDFISH(1, 120, 200)
        }
    }
}