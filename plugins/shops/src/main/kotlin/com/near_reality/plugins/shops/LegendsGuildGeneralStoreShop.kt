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

class LegendsGuildGeneralStoreShop : ShopScript() {
    init {
        "Legends Guild General Store"(60, ShopCurrency.COINS, STOCK_ONLY) {
            SWORDFISH(20, -1, 310)
            APPLE_PIE(5, -1, 46)
            ATTACK_POTION3(3, -1, 18)
            STEEL_ARROW(500, -1, 18)
        }
    }
}