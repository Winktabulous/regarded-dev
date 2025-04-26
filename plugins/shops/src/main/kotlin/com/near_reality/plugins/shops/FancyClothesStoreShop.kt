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

class FancyClothesStoreShop : ShopScript() {
    init {
        "Fancy Clothes Store"(162, ShopCurrency.COINS, STOCK_ONLY) {
            CHEFS_HAT(0, 1, 2)
            BLUE_WIZARD_HAT(3, 0, 2)
            YELLOW_CAPE(1, 12, 41)
            GREY_WOLF_FUR(3, 20, 150)
            BEAR_FUR(3, 4, 13)
            NEEDLE(3, 1, 1)
            THREAD(100, 1, 1)
            LEATHER_GLOVES(10, 2, 7)
            LEATHER_BOOTS(10, 2, 7)
            PRIEST_GOWN_428(3, 1, 6)
            PRIEST_GOWN(3, 1, 6)
            BROWN_APRON(10, 0, 2)
            PINK_SKIRT(10, 0, 2)
            BLACK_SKIRT(3, 0, 2)
            BLUE_SKIRT(2, 0, 2)
            RED_CAPE(10, 0, 2)
            RIGHT_EYE_PATCH(3, 0, 2)
            RED_FEATHER(0, 4, 13)
            YELLOW_FEATHER(0, 7, 16)
            ORANGE_FEATHER(0, 5, 19)
            BLUE_FEATHER(0, 9, 20)
            STRIPY_FEATHER(0, 8, 26)
        }
    }
}