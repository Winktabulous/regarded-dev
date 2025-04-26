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

class GabootysTaiBwoWannaiDrinkyStoreShop : ShopScript() {
    init {
        "Gabooty's Tai Bwo Wannai Drinky Store"(110, ShopCurrency.COINS, STOCK_ONLY) {
            FRUIT_BLAST(0, 10, 30)
            DRUNK_DRAGON(0, 10, 30)
            PINEAPPLE_PUNCH(0, 10, 30)
            WIZARD_BLIZZARD(0, 10, 30)
            BLURBERRY_SPECIAL(0, 10, 30)
            CHOC_SATURDAY(0, 10, 30)
        }
    }
}