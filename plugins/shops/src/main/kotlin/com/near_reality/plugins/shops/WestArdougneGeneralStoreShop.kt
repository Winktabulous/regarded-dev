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

class WestArdougneGeneralStoreShop : ShopScript() {
    init {
        "West Ardougne General Store"(96, ShopCurrency.COINS, CAN_SELL, 0.550000011920929) {
            POT(3, 0, 1)
            ROPE(3, 9, 21)
            BRONZE_PICKAXE(2, 0, 1)
            BUCKET(2, 1, 2)
            TINDERBOX(2, 0, 1)
            HAMMER(3, 0, 1)
            LEATHER_BOOTS(2, 3, 7)
            LONGBOW(2, 44, 96)
            BRONZE_ARROW(20, 0, 1)
            SALMON(10, 27, 60)
            MEAT_PIE(10, 8, 18)
            BREAD(5, 6, 14)
            COOKED_MEAT(10, 2, 4)
        }
    }
}