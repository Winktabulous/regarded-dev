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

class LouiesArmouredLegsBazaarShop : ShopScript() {
    init {
        "Louie's Armoured Legs Bazaar"(125, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PLATELEGS(5, 52, 80)
            IRON_PLATELEGS(3, 182, 280)
            STEEL_PLATELEGS(2, 650, 1000)
            BLACK_PLATELEGS(1, 1248, 1920)
            MITHRIL_PLATELEGS(1, 1690, 2600)
            ADAMANT_PLATELEGS(1, 4160, 6400)
        }
    }
}