package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BOWL
import com.zenyte.game.item.ItemId.BOWL_OF_WATER
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.BUCKET_OF_WATER
import com.zenyte.game.item.ItemId.DESERT_BOOTS
import com.zenyte.game.item.ItemId.DESERT_ROBE
import com.zenyte.game.item.ItemId.DESERT_SHIRT
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.JUG_OF_WATER
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.WATERSKIN0
import com.zenyte.game.item.ItemId.WATERSKIN4
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.CAN_SELL

class BanditBargainsShop : ShopScript() {
    init {
        "Bandit Bargains"(129, ShopCurrency.COINS, CAN_SELL) {
            WATERSKIN4(5, -1, 30)
            WATERSKIN0(5, -1, 15)
            JUG_OF_WATER(5, -1, 1)
            BOWL_OF_WATER(5, -1, 4)
            BUCKET_OF_WATER(5, -1, 6)
            JUG(5, -1, 1)
            EMPTY_JUG_PACK(8, -1, 140)
            BOWL(5, -1, 4)
            BUCKET(5, -1, 2)
            DESERT_BOOTS(5, -1, 20)
            DESERT_SHIRT(5, -1, 40)
            DESERT_ROBE(5, -1, 40)
            KNIFE(5, -1, 6)
        }
    }
}