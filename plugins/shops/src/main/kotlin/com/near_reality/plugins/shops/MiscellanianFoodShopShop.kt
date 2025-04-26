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

class MiscellanianFoodShopShop : ShopScript() {
    init {
        "Miscellanian Food Shop"(49, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(5, 6, 12)
            CHEESE(5, 2, 4)
            CABBAGE(5, 0, 1)
            POTATO(5, 0, 1)
            ONION(5, 1, 3)
            POT_OF_FLOUR(5, 5, 10)
            CHOCOLATE_BAR(2, 5, 10)
            BUCKET_OF_MILK(5, 3, 6)
        }
    }
}