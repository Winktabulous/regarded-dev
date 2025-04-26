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

class GemTraderShop : ShopScript() {
    init {
        "Gem Trader"(124, ShopCurrency.COINS, STOCK_ONLY) {
            UNCUT_SAPPHIRE(1, 8, 25)
            UNCUT_EMERALD(1, 36, 50)
            UNCUT_RUBY(0, 70, 100)
            UNCUT_DIAMOND(0, 140, 200)
            SAPPHIRE(1, 69, 250)
            EMERALD(1, 350, 500)
            RUBY(0, 555, 1000)
            DIAMOND(0, 666, 2000)
        }
    }
}