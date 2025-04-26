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

class GrudsHerbloreStallShop : ShopScript() {
    init {
        "Grud's Herblore Stall"(27, ShopCurrency.COINS, STOCK_ONLY) {
            VIAL(50, 0, 2)
            EMPTY_VIAL_PACK(10, 80, 260)
            PESTLE_AND_MORTAR(3, 1, 5)
            EYE_OF_NEWT(50, 1, 3)
            EYE_OF_NEWT_PACK(20, 120, 390)
        }
    }
}