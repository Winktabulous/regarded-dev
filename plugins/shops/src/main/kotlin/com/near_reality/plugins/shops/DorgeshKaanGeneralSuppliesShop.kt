package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BOWL
import com.zenyte.game.item.ItemId.CAKE_TIN
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.GLASSBLOWING_PIPE
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.item.ItemId.UNLIT_TORCH
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DorgeshKaanGeneralSuppliesShop : ShopScript() {
    init {
        "Dorgesh-Kaan General Supplies"(144, ShopCurrency.COINS, STOCK_ONLY) {
            UNLIT_TORCH(5, -1, 5)
            TINDERBOX(2, -1, 1)
            POT(5, -1, 1)
            JUG(2, -1, 1)
            EMPTY_JUG_PACK(6, -1, 182)
            BOWL(2, -1, 5)
            CAKE_TIN(2, -1, 13)
            ROPE(3, -1, 23)
            GLASSBLOWING_PIPE(1, -1, 2)
        }
    }
}