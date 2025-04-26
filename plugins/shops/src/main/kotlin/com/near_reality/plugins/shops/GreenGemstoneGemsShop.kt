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

class GreenGemstoneGemsShop : ShopScript() {
    init {
        "Green Gemstone Gems"(186, ShopCurrency.COINS, STOCK_ONLY) {
            SAPPHIRE(3, 200, 390)
            EMERALD(1, 400, 750)
            RUBY(1, 800, 1500)
            DIAMOND(0, 1600, 3000)
        }
    }
}