package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BRONZE_AXE
import com.zenyte.game.item.ItemId.RAW_MACKEREL
import com.zenyte.game.item.ItemId.THIN_SNAIL
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class AurelsSuppliesShop : ShopScript() {
    init {
        "Aurel's Supplies"(169, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_AXE(10, 6, 20)
            TINDERBOX(10, 0, 1)
            THIN_SNAIL(10, 2, 6)
            RAW_MACKEREL(10, 6, 22)
        }
    }
}