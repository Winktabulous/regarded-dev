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

class GrandTreeGroceriesShop : ShopScript() {
    init {
        "Grand Tree Groceries"(90, ShopCurrency.COINS, STOCK_ONLY) {
            GIANNE_DOUGH(50, -1, 2)
            EQUA_LEAVES(20, -1, 2)
            POT_OF_FLOUR(10, -1, 10)
            GNOME_SPICE(10, -1, 2)
            ONION(10, -1, 3)
            POTATO(10, -1, 1)
            CABBAGE(10, -1, 1)
            TOMATO(10, -1, 4)
            CHEESE(10, -1, 4)
            SPIDER_ON_STICK(0, -1, 50)
            SPIDER_ON_SHAFT(0, -1, 40)
            LIME(10, -1, 2)
            ORANGE(10, -1, 2)
            LEMON(10, -1, 2)
            PINEAPPLE(10, -1, 2)
            DWELLBERRIES(10, -1, 4)
            COCKTAIL_SHAKER(10, -1, 2)
            CHOCOLATE_BAR(20, -1, 10)
            CHOCOLATE_DUST(10, -1, 2)
            POT_OF_CREAM(5, -1, 2)
            BUCKET_OF_MILK(5, -1, 6)
            KNIFE(5, -1, 6)
            GIANNES_COOK_BOOK(5, -1, 2)
            BATTA_TIN(5, -1, 10)
            CRUNCHY_TRAY(5, -1, 10)
            GNOMEBOWL_MOULD(5, -1, 10)
        }
    }
}