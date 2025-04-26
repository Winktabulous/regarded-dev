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

class RemnantShopShop : ShopScript() {
    init {
        "Remnant Shop"(6969, EXCHANGE_POINTS, STOCK_ONLY) {
            REMNANT_POINT_VOUCHER_1(100_000_000, 1, 1)
            ORB_OF_XERIC(1000, 650, 1000)
            ORB_OF_BLOOD(1000, 650, 1000)
            WORLD_BOOST_TOKEN(1000, (-1), 6500)
            OVERLOAD_4(1000, (-1), 200)
        //    REMNANT_POINT_VOUCHER_5(100_000_000, 5, 5)
        //    REMNANT_POINT_VOUCHER_10(100_000_000, 10, 10)
        }
    }
}