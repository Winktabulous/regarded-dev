package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.EMPTY_CUP
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.SHEARS
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.CAN_SELL

class BurthorpeSuppliesShop : ShopScript() {
    init {
        "Burthorpe Supplies"(ShopCurrency.COINS, CAN_SELL) {
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(3, 56, 182)
            SHEARS(2, 0, 1)
            BUCKET(2, 0, 2)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            KNIFE(2, 2, 7)
            EMPTY_CUP(10, 0, 2)
        }
    }
}