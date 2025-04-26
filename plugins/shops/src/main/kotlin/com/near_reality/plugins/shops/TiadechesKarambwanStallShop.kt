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

class TiadechesKarambwanStallShop : ShopScript() {
    init {
        "Tiadeche's Karambwan Stall"(112, ShopCurrency.COINS, STOCK_ONLY) {
            RAW_KARAMBWAN(10, 90, 110)
            RAW_KARAMBWANJI(50, 0, 10)
            KARAMBWAN_VESSEL(2, 2, 2)
        }
    }
}