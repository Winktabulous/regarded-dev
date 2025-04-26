package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BOWL
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CAKE_TIN
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.CAN_SELL

class ArheinStoreShop : ShopScript() {
    init {
        "Arhein Store"(71, ShopCurrency.COINS, CAN_SELL) {
            BUCKET(10, 1, 3)
            BRONZE_PICKAXE(2, 0, 1)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 4, 13)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            ROPE(2, 7, 23)
            POT(2, 0, 1)
            KNIFE(2, 2, 7)
        }
    }
}