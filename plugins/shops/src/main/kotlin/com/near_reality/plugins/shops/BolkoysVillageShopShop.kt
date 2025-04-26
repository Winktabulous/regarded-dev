package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BRONZE_ARROW
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.COOKED_MEAT
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.SHEARS
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.CAN_SELL

class BolkoysVillageShopShop : ShopScript() {
    init {
        "Bolkoy's Village Shop"(95, ShopCurrency.COINS, CAN_SELL) {
            POT(3, 0, 1)
            BRONZE_PICKAXE(5, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, 0, 1)
            BUCKET(2, 0, 2)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            BRONZE_ARROW(30, 0, 1)
            COOKED_MEAT(2, 1, 5)
        }
    }
}