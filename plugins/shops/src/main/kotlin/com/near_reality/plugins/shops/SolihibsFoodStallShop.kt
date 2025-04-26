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

class SolihibsFoodStallShop : ShopScript() {
    init {
        "Solihib's Food Stall"(226, ShopCurrency.COINS, STOCK_ONLY) {
            MONKEY_NUTS(200, 1, 3)
            BANANA(1000, 1, 2)
            BANANA_STEW(10, 150, 300)
            MONKEY_BAR(20, 25, 50)
        }
    }
}