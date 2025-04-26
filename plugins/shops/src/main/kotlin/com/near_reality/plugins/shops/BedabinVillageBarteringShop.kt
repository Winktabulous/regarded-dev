package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BOWL_OF_WATER
import com.zenyte.game.item.ItemId.BUCKET_OF_WATER
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG_OF_WATER
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.WATERSKIN0
import com.zenyte.game.item.ItemId.WATERSKIN4
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.CAN_SELL

class BedabinVillageBarteringShop : ShopScript() {
    init {
        "Bedabin Village Bartering"(131, ShopCurrency.COINS, CAN_SELL) {
            WATERSKIN4(5, -1, 36)
            WATERSKIN0(5, -1, 18)
            JUG_OF_WATER(5, -1, 1)
            BOWL_OF_WATER(5, -1, 4)
            BUCKET_OF_WATER(5, -1, 7)
            KNIFE(5, -1, 7)
            HAMMER(5, -1, 1)
        }
    }
}