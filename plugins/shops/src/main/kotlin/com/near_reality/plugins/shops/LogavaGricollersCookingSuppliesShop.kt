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

class LogavaGricollersCookingSuppliesShop : ShopScript() {
    init {
        "Logava Gricoller's Cooking Supplies"(206, ShopCurrency.COINS, STOCK_ONLY) {
            PIE_DISH(5, 1, 3)
            CAKE_TIN(2, 3, 10)
            BOWL(2, 1, 4)
            TINDERBOX(3, 0, 1)
            JUG(3, 0, 1)
            POT(12, 0, 1)
            EMPTY_CUP(3, 1, 2)
            KNIFE(3, 1, 6)
            BALL_OF_WOOL(30, 1, 2)
            POTATO(5, 4, 1)
            COOKING_APPLE(2, 0, 1)
            BREAD(2, 6, 12)
            CHOCOLATE_BAR(1, 6, 10)
            CHEESE(3, 4, 10)
            POT_OF_FLOUR(8, 4, 10)
        }
    }
}