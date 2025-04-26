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

class RommiksCraftySuppliesShop : ShopScript() {
    init {
        "Rommik's Crafty Supplies"(22, ShopCurrency.COINS, STOCK_ONLY) {
            CHISEL(2, 0, 1)
            RING_MOULD(4, 3, 5)
            NECKLACE_MOULD(2, 3, 5)
            AMULET_MOULD(2, 3, 5)
            NEEDLE(3, 0, 1)
            THREAD(100, 0, 1)
            HOLY_MOULD(3, 3, 5)
            SICKLE_MOULD(6, 6, 10)
            TIARA_MOULD(10, 65, 100)
            BOLT_MOULD(10, 16, 25)
            BRACELET_MOULD(5, 3, 5)
        }
    }
}