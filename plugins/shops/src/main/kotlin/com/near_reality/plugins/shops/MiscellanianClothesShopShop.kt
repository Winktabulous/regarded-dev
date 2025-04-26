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

class MiscellanianClothesShopShop : ShopScript() {
    init {
        "Miscellanian Clothes Shop"(48, ShopCurrency.COINS, STOCK_ONLY) {
            FREMENNIK_BROWN_SHIRT(5, 137, 250)
            FREMENNIK_GREY_SHIRT(5, 137, 250)
            FREMENNIK_BEIGE_SHIRT(5, 137, 250)
            FREMENNIK_RED_SHIRT(5, 137, 250)
            FREMENNIK_BLUE_SHIRT(5, 137, 250)
            FREMENNIK_ROBE(5, 275, 500)
            FREMENNIK_SKIRT(5, 275, 500)
            SKIRT_5050(3, 302, 550)
            SKIRT_5052(3, 343, 625)
            TROUSERS_5038(3, 385, 700)
            TROUSERS_5040(3, 412, 750)
            SHORTS_5044(3, 198, 360)
            SHORTS_5046(3, 214, 390)
            WOVEN_TOP_5026(3, 343, 625)
            WOVEN_TOP_5028(3, 357, 650)
            SHIRT_5032(3, 330, 600)
            SHIRT_5034(3, 343, 625)
        }
    }
}