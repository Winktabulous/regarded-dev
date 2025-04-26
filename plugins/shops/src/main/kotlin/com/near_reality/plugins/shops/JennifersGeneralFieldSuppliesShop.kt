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

class JennifersGeneralFieldSuppliesShop : ShopScript() {
    init {
        "Jennifer's General Field Supplies"(221, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, -1, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, -1, 1)
            BUCKET(3, -1, 2)
            BOWL(2, -1, 5)
            CAKE_TIN(2, -1, 13)
            TINDERBOX(2, -1, 1)
            CHISEL(2, -1, 1)
            BRONZE_AXE(10, -1, 390)
            FIELD_RATION(25, -1, 390)
        }
    }
}