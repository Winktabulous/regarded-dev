package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.AMULET_MOULD
import com.zenyte.game.item.ItemId.BOLT_MOULD
import com.zenyte.game.item.ItemId.BRACELET_MOULD
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.HOLY_MOULD
import com.zenyte.game.item.ItemId.NECKLACE_MOULD
import com.zenyte.game.item.ItemId.NEEDLE
import com.zenyte.game.item.ItemId.RING_MOULD
import com.zenyte.game.item.ItemId.SICKLE_MOULD
import com.zenyte.game.item.ItemId.THREAD
import com.zenyte.game.item.ItemId.TIARA_MOULD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DommiksCraftingStoreShop : ShopScript() {
    init {
        "Dommik's Crafting Store"(123, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(2, 1, 1)
            RING_MOULD(10, 1, 5)
            NECKLACE_MOULD(2, 1, 5)
            AMULET_MOULD(10, 1, 5)
            NEEDLE(3, 1, 1)
            THREAD(100, 1, 1)
            HOLY_MOULD(3, 1, 5)
            SICKLE_MOULD(10, 3, 10)
            TIARA_MOULD(10, 33, 100)
            BOLT_MOULD(10, 8, 25)
            BRACELET_MOULD(5, 1, 5)
        }
    }
}