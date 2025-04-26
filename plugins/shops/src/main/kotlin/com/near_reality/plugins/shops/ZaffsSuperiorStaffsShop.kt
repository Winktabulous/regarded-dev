package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class ZaffsSuperiorStaffsShop : ShopScript() {
    init {
        "Zaff's Superior Staffs!"(167, ShopCurrency.COINS, STOCK_ONLY) {
            BATTLESTAFF(5, 3850, 7000)
            STAFF(5, 8, 15)
            MAGIC_STAFF(5, 110, 200)
            STAFF_OF_AIR(2, 825, 1500)
            STAFF_OF_WATER(2, 825, 1500)
            STAFF_OF_EARTH(2, 825, 1500)
            STAFF_OF_FIRE(2, 825, 1500)
        }
    }
}