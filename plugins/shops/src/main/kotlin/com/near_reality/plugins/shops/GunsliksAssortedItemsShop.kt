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

class GunsliksAssortedItemsShop : ShopScript() {
    init {
        "Gunslik's Assorted Items"(193, ShopCurrency.COINS, CAN_SELL) {
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(4, 56, 182)
            BUCKET(2, 0, 2)
            TINDERBOX(2, 1, 1)
            CHISEL(2, 0, 14)
            HAMMER(5, 0, 1)
            CANDLE(10, 1, 3)
            UNLIT_TORCH(10, 1, 5)
            CHARCOAL(50, 18, 58)
            LEATHER_GLOVES(10, 2, 7)
            VIAL(10, 0, 2)
            EMPTY_VIAL_PACK(10, 80, 260)
            PESTLE_AND_MORTAR(3, 1, 5)
            ROPE(10, 7, 23)
        }
    }
}