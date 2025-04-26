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

class WydinsFoodStoreShop : ShopScript() {
    init {
        "Wydin's Food Store"(19, ShopCurrency.COINS, STOCK_ONLY) {
            POT_OF_FLOUR(3, 7, 10)
            RAW_BEEF(1, 0, 1)
            RAW_CHICKEN(1, 0, 1)
            CABBAGE(3, 0, 1)
            BANANA(3, 1, 2)
            REDBERRIES(1, 2, 3)
            BREAD(0, 8, 12)
            CHOCOLATE_BAR(1, 7, 10)
            CHEESE(3, 2, 4)
            TOMATO(3, 2, 4)
            POTATO(1, 0, 1)
        }
    }
}