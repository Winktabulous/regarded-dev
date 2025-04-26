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

class KeldagrimsBestBreadShop : ShopScript() {
    init {
        "Keldagrim's Best Bread"(187, ShopCurrency.COINS, STOCK_ONLY) {
            BREAD(10, 9, 18)
            CAKE(3, 40, 75)
            CHOCOLATE_SLICE(8, 24, 45)
        }
    }
}