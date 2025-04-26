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

class FrenitasCookeryShopShop : ShopScript() {
    init {
        "Frenita's Cookery Shop"(99, ShopCurrency.COINS, STOCK_ONLY) {
            PIE_DISH(5, 1, 3)
            COOKING_APPLE(2, 0, 1)
            CAKE_TIN(2, 3, 10)
            BOWL(2, 1, 4)
            POTATO(5, 4, 1)
            TINDERBOX(4, 0, 1)
            JUG(1, 0, 1)
            POT(8, 0, 1)
            CHOCOLATE_BAR(2, 6, 10)
            POT_OF_FLOUR(8, 4, 10)
            EMPTY_CUP(20, 1, 2)
        }
    }
}