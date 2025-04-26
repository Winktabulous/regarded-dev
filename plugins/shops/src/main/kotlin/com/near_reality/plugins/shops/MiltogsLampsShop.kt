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

class MiltogsLampsShop : ShopScript() {
    init {
        "Miltog's Lamps"(146, ShopCurrency.COINS, STOCK_ONLY) {
            UNLIT_TORCH(15, -1, 6)
            EMPTY_OIL_LAMP(4, -1, 37)
            EMPTY_OIL_LANTERN(2, 48, 176)
            BULLSEYE_LANTERN_EMPTY(1, -1, 600)
            MINING_HELMET(1, 540, 900)
            TINDERBOX(10, 0, 1)
            LIGHT_ORB(0, -1, 525)
            OIL_LAMP(0, -1, 42)
            OIL_LANTERN(0, -1, 187)
            BULLSEYE_LANTERN(0, -1, 630)
        }
    }
}