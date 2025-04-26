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

class TheDeeperLodeShop : ShopScript() {
    init {
        "The Deeper Lode"(209, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(10, -1, 2)
            DWARVEN_STOUT(1, -1, 2)
            DRAGON_BITTER(3, -1, 2)
            KEBAB(2, -1, 4)
            BEER_GLASS(0, -1, 2)
        }
    }
}