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

class FrincosFabulousHerbStoreShop : ShopScript() {
    init {
        "Frincos' Fabulous Herb Store"(8, ShopCurrency.COINS, STOCK_ONLY) {
            VIAL(50, 1, 5)
            EMPTY_VIAL_PACK(10, 140, 200)
            PESTLE_AND_MORTAR(3, 2, 4)
            EYE_OF_NEWT(50, 2, 3)
            EYE_OF_NEWT_PACK(20, 210, 300)
        }
    }
}