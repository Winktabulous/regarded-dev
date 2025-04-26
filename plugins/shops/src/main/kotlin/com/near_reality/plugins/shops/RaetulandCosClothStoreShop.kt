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

class RaetulandCosClothStoreShop : ShopScript() {
    init {
        "Raetul and Co's Cloth Store"(142, ShopCurrency.COINS, STOCK_ONLY) {
            LINEN(20, 22, 30)
            DESERT_SHIRT(20, 30, 40)
            DESERT_ROBE(20, 30, 40)
            DESERT_BOOTS(20, 15, 20)
            SILK(10, 22, 30)
            THREAD(50, 0, 1)
            NEEDLE(20, 0, 1)
        }
    }
}