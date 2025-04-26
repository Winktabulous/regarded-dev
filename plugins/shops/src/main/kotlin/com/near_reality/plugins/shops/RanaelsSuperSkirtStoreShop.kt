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

class RanaelsSuperSkirtStoreShop : ShopScript() {
    init {
        "Ranael's Super Skirt Store"(126, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PLATESKIRT(5, 24, 80)
            IRON_PLATESKIRT(3, 84, 280)
            STEEL_PLATESKIRT(2, 300, 1000)
            BLACK_PLATESKIRT(1, 576, 1920)
            MITHRIL_PLATESKIRT(1, 780, 2600)
            ADAMANT_PLATESKIRT(1, 1920, 6400)
        }
    }
}