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

class PollnivneachgeneralstoreShop : ShopScript() {
    init {
        "Pollnivneach general store"(137, ShopCurrency.COINS, CAN_SELL) {
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 140)
            WATERSKIN3(20, 9, 27)
            DESERT_SHIRT(3, 13, 40)
            DESERT_BOOTS(2, 6, 20)
            BUCKET(12, 1, 2)
            FAKE_BEARD(11, 0, 1)
            KHARIDIAN_HEADPIECE(12, 0, 1)
            CHEESE(5, -1, 4)
            LIME(5, -1, 2)
            TOMATO(5, -1, 4)
            JUG_OF_WATER(5, 1, 1)
            BOWL_OF_WATER(7, 1, 4)
            BUCKET_OF_WATER(8, 2, 6)
        }
    }
}