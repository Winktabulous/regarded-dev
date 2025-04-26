package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class TraderStansTradingPostPortTyrasShop : ShopScript() {
    init {
        "Trader Stan's Trading Post<Port Tyras>"(254, ShopCurrency.COINS, CAN_SELL) {
            POT(5, 0, 2)
            JUG(2, 0, 2)
            SHEARS(2, 0, 2)
            BUCKET(3, 0, 5)
            BOWL(2, 1, 10)
            CAKE_TIN(2, 4, 25)
            TINDERBOX(3, 0, 2)
            CHISEL(2, 0, 2)
            HAMMER(5, 0, 2)
            NEWCOMER_MAP(5, 0, 2)
            SECURITY_BOOK(5, 0, 15)
            ROPE(2, 3, 45)
            KNIFE(2, 0, 5)
            PINEAPPLE(15, 0, 5)
            BANANA(15, 0, 5)
            ORANGE(10, 0, 5)
            BUCKET_OF_SLIME(50, 0, 2)
            GLASSBLOWING_PIPE(10, 0, 5)
            BUCKET_OF_SAND(150, 0, 5, 5)
            SEAWEED(300, 0, 5, 5)
            SODA_ASH(150, 0, 5, 5)
            LOBSTER_POT(20, 3, 50)
            FISHING_ROD(20, 0, 12)
            SWAMP_PASTE(500, 4, 75, 5)
            TYRAS_HELM(25, 82, 1375)
            RAW_RABBIT(20, 3, 50)
            RIGHT_EYE_PATCH(5, 0, 5)
        }
    }
}