package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BOLT_OF_CLOTH
import com.zenyte.game.item.ItemId.BRONZE_NAILS
import com.zenyte.game.item.ItemId.IRON_NAILS
import com.zenyte.game.item.ItemId.SAW
import com.zenyte.game.item.ItemId.STEEL_NAILS
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ConstructionsuppliesShop : ShopScript() {
    init {
        "Construction supplies"(161, ShopCurrency.COINS, STOCK_ONLY) {
            SAW(1000, 3, 13)
            BOLT_OF_CLOTH(1000, 195, 650)
            BRONZE_NAILS(95, 1, 2)
            IRON_NAILS(95, 2, 5)
            STEEL_NAILS(95, 1, 3)
        }
    }
}