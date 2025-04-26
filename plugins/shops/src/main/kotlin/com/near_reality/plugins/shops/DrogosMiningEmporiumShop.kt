package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BRONZE_BAR
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.COAL
import com.zenyte.game.item.ItemId.COPPER_ORE
import com.zenyte.game.item.ItemId.GOLD_BAR
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.IRON_BAR
import com.zenyte.game.item.ItemId.IRON_ORE
import com.zenyte.game.item.ItemId.TIN_ORE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DrogosMiningEmporiumShop : ShopScript() {
    init {
        "Drogo's Mining Emporium"(5, ShopCurrency.COINS, STOCK_ONLY) {
            HAMMER(4, 0, 1)
            BRONZE_PICKAXE(4, 0, 1)
            COPPER_ORE(0, 1, 3)
            TIN_ORE(0, 1, 3)
            IRON_ORE(0, 5, 17)
            COAL(0, 13, 45)
            BRONZE_BAR(0, 2, 8)
            IRON_BAR(0, 8, 28)
            GOLD_BAR(0, 90, 300)
        }
    }
}