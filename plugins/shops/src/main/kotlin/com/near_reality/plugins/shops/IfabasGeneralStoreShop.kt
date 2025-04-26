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

class IfabasGeneralStoreShop : ShopScript() {
    init {
        "Ifaba's General Store"(222, ShopCurrency.COINS, CAN_SELL) {
            POT(3, 1, 1)
            JUG(2, 1, 1)
            EMPTY_JUG_PACK(8, -1, 140)
            ROPE(8, 5, 18)
            BUCKET(2, 1, 2)
            TINDERBOX(2, 1, 1)
            HAMMER(5, 3, 13)
        }
    }
}