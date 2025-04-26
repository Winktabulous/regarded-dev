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

class LletyaSeamstressShop : ShopScript() {
    init {
        "Lletya Seamstress"(182, ShopCurrency.COINS, STOCK_ONLY) {
            THREAD(8, 0, 1)
            NEEDLE(3, 0, 1)
            BALL_OF_WOOL(5, 1, 2)
            RED_DYE(10, 2, 6)
            YELLOW_DYE(10, 2, 6)
            BLUE_DYE(10, 2, 6)
            ORANGE_DYE(10, 2, 6)
            GREEN_DYE(10, 2, 6)
            PURPLE_DYE(10, 2, 6)
        }
    }
}