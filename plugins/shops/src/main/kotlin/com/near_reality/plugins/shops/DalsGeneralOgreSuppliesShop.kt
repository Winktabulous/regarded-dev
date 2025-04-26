package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DalsGeneralOgreSuppliesShop : ShopScript() {
    init {
        "Dal's General Ogre Supplies"(26, ShopCurrency.COINS, STOCK_ONLY) {
            POT(30, 1, 1)
            JUG(10, 1, 1)
            KNIFE(10, 7, 25)
            BUCKET(30, 1, 2)
            TINDERBOX(10, 1, 1)
            CHISEL(10, 4, 14)
            HAMMER(10, 3, 13)
        }
    }
}