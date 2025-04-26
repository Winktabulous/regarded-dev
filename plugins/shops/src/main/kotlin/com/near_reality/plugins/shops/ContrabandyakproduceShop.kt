package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.CURED_YAKHIDE
import com.zenyte.game.item.ItemId.HAIR
import com.zenyte.game.item.ItemId.RAW_YAK_MEAT
import com.zenyte.game.item.ItemId.YAKHIDE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ContrabandyakproduceShop : ShopScript() {
    init {
        "Contraband yak produce"(36, ShopCurrency.COINS, STOCK_ONLY) {
            YAKHIDE(25, 35, 55)
            RAW_YAK_MEAT(50, 1, 2)
            HAIR(50, 1, 2)
            CURED_YAKHIDE(10, 70, 110)
        }
    }
}