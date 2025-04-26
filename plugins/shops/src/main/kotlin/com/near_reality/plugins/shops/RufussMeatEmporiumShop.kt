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

class RufussMeatEmporiumShop : ShopScript() {
    init {
        "Rufus's Meat Emporium"(172, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_BEEF(10, 0, 1)
            RAW_CHICKEN(10, 0, 1)
            RAW_RAT_MEAT(10, 0, 1)
            RAW_BEAR_MEAT(10, 0, 1)
            RAW_TROUT(5, 14, 26)
            RAW_PIKE(5, 17, 32)
            RAW_SALMON(5, 35, 65)
            RAW_SHARK(1, 210, 390)
        }
    }
}