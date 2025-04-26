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

class ThessaliasFineClothesShop : ShopScript() {
    init {
        "Thessalia's Fine Clothes"(165, ShopCurrency.COINS, STOCK_ONLY) {
            WHITE_APRON(3, 1, 2)
            LEATHER_BODY(12, 11, 21)
            LEATHER_GLOVES(10, 3, 6)
            LEATHER_BOOTS(10, 3, 6)
            BROWN_APRON(1, 1, 2)
            PINK_SKIRT(5, 1, 2)
            BLACK_SKIRT(3, 1, 2)
            BLUE_SKIRT(2, 1, 2)
            RED_CAPE(4, 1, 2)
            SILK(5, 16, 30)
            PRIEST_GOWN_428(3, 2, 5)
            PRIEST_GOWN(3, 2, 5)
        }
    }
}