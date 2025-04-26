package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BALL_OF_WOOL
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.NECKLACE_MOULD
import com.zenyte.game.item.ItemId.NEEDLE
import com.zenyte.game.item.ItemId.RING_MOULD
import com.zenyte.game.item.ItemId.THREAD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class CarefreeCraftingStallShop : ShopScript() {
    init {
        "Carefree Crafting Stall"(185, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(2, 0, 1)
            RING_MOULD(4, 4, 7)
            NECKLACE_MOULD(2, 4, 7)
            NEEDLE(3, 0, 1)
            THREAD(100, 0, 1)
            BALL_OF_WOOL(100, 1, 3)
        }
    }
}